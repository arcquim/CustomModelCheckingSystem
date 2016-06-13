package com.arcquim.server.service;

import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.gwt.shared.ProjectHistoryDescriptor;
import com.arcquim.gwt.shared.Record;
import com.arcquim.server.entity.Project;
import com.arcquim.server.entity.ProjectHistory;
import com.arcquim.server.entity.SystemClient;
import com.arcquim.server.entity.Result;
import com.arcquim.server.transport.MailSender;
import com.arcquim.server.transport.SmtpMailSender;
import com.system.CTLVerificator;
import com.system.Controller;
import com.system.VerificationResult;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
@Stateless
@Path("/project")
public class ProjectFacade extends AbstractFacade<Project> {
    
    private static final int DEFAULT_PROJECT_INDEX = 0;
    
    @PersistenceContext(unitName = "com.arcquim_SystemServer_war_1.0-SNAPSHOTPU")
    private EntityManager entityManager;
    
    @EJB
    private SystemClientFacade clientFacade;
    
    @EJB
    private ProjectHistoryFacade projectHistoryFacade;
    
    @EJB
    private VerificationResultFacade resultFacade;
    
    public ProjectFacade() {
        this(Project.class);
    }

    public ProjectFacade(Class<Project> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProject(String json) {
        Record record = parseMessage(json);
        if (record == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        ClientDescriptor clientDescriptor = record.getClientDescriptor();
        if (clientDescriptor == null || clientDescriptor.getClientId() == null || clientDescriptor.getClientEmail() == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        Long clientId = clientDescriptor.getClientId();
        String email = clientDescriptor.getClientEmail();
        SystemClient client = clientFacade.getClientByIdAndEmail(clientId, email);
        if (client == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        Project project = new Project();
        project.translateFromRecord(record);
        project.setSystemClient(client);
        try {
            create(project);
            clientFacade.edit(project.getSystemClient());
        }
        catch (Throwable throwable) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        return Response.ok(createMessage(project.translateIntoRecord()), 
                MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProject(@PathParam("id") String id) {
        if (id == null || id.isEmpty()) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        Long projectId;
        try {
            projectId = Long.parseLong(id);
        }
        catch(NumberFormatException ex) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        Project desiredProject = findById(projectId);
        if (desiredProject == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        return Response.ok(
                createMessage(desiredProject.translateIntoRecord()), 
                MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/get/history/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHistory(@PathParam("id") String id) {
        if (id == null || id.isEmpty()) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        Long projectId;
        try {
            projectId = Long.parseLong(id);
        }
        catch(NumberFormatException ex) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        Project desiredProject = findById(projectId);
        if (desiredProject == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        List<ProjectHistory> attempts = desiredProject.getAttempts();
        Record record = desiredProject.translateIntoRecord();
        record.setClientDescriptor(desiredProject.getSystemClient().translateIntoRecord().getClientDescriptor());
        List<ProjectHistoryDescriptor> historyDescriptors = new ArrayList<>(attempts.size());
        for (ProjectHistory attempt : attempts) {
            historyDescriptors.add(attempt.translateIntoRecord().getProjectHistoryDescriptor());
        }
        record.getProjectDescriptor().setHistory(historyDescriptors);
        return Response.ok(createMessage(record), MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/check")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runModelChecking(String json) {
        Record record = parseMessage(json);
        if (record == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        ProjectDescriptor projectDescriptor = record.getProjectDescriptor();
        if (projectDescriptor == null || projectDescriptor.getId() == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        Long projectId = projectDescriptor.getId();
        Project project = findById(projectId);
        if (project == null || !project.getActive()) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        if (project.getSystemClient() == null || project.getSystemClient().getBusy()) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        project.getSystemClient().setBusy(Boolean.TRUE);
        clientFacade.edit(project.getSystemClient());
        resultFacade.runModelChecking(projectDescriptor, project);
        return Response.ok(createMessage(record), MediaType.APPLICATION_JSON).build();
    }
    
    @PUT
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editProject(String json) {
        Record record = parseMessage(json);
        if (record == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        ProjectDescriptor projectDescriptor = record.getProjectDescriptor();
        if (projectDescriptor == null || projectDescriptor.getId() == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        Project project = findById(projectDescriptor.getId());
        if (project == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        project.translateFromRecord(record);
        edit(project);
        return Response.ok(createMessage(project.translateIntoRecord()), MediaType.APPLICATION_JSON).build();
    }
    
    public Project findById(Long id) {
        List<Project> projects = 
                entityManager.createNamedQuery("Project.findById")
                .setParameter(Project.ID_QUERY_PARAMETER, id)
                .getResultList();
        if (projects == null || projects.isEmpty()) {
            return null;
        }
        return projects.get(DEFAULT_PROJECT_INDEX);
    }
    
}
