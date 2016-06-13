package com.arcquim.server.service;

import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.Record;
import com.arcquim.server.entity.SystemClient;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
@Stateless
@Path("/user")
public class SystemClientFacade extends AbstractFacade<SystemClient> {
    
    private static final int DEFAULT_CLIENT_INDEX = 0;
    
    public SystemClientFacade() {
        this(SystemClient.class);
    }
    
    public SystemClientFacade(Class<SystemClient> entityClass) {
        super(entityClass);
    }
    
    @PersistenceContext(unitName = "com.arcquim_SystemServer_war_1.0-SNAPSHOTPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    @POST
    @Path("/enter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response enterSystem(String json) {
        Record record = parseMessage(json);
        if (record == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        ClientDescriptor clientDescriptor = record.getClientDescriptor();
        if (clientDescriptor == null) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        String email = clientDescriptor.getClientEmail();
        if (email == null || email.isEmpty()) {
            return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
        }
        List<SystemClient> clientsWithEmail = getClientsWithEmail(email);
        SystemClient client;
        if (clientsWithEmail == null || clientsWithEmail.isEmpty()) {
            client = new SystemClient();
            client.translateFromRecord(record);
            try {
                create(client);
            }
            catch(Throwable throwable) {
                return Response.ok(createMessage(null), MediaType.APPLICATION_JSON).build();
            }
        }
        else {
            client = clientsWithEmail.get(DEFAULT_CLIENT_INDEX);
        }
        return Response.ok(createMessage(client.translateIntoRecord()), 
                MediaType.APPLICATION_JSON).build();
    }
    
    public List<SystemClient> getClientsWithEmail(String email) {
        List<SystemClient> clients = 
                entityManager.createNamedQuery("SystemClient.findByEmail")
                .setParameter(SystemClient.EMAIL_QUERY_PARAMETER, email)
                .getResultList();
        return clients;
    }
    
    public SystemClient getClientById(Long id) {
        List<SystemClient> clients = 
                entityManager.createNamedQuery("SystemClient.findById")
                .setParameter(SystemClient.ID_QUERY_PARAMETER, id)
                .getResultList();
        if (clients == null || clients.isEmpty()) {
            return null;
        }
        return clients.get(DEFAULT_CLIENT_INDEX);
    }
    
    public SystemClient getClientByIdAndEmail(Long id, String email) {
        List<SystemClient> clients = 
                entityManager.createNamedQuery("SystemClient.findByIdAndEmail")
                .setParameter(SystemClient.ID_QUERY_PARAMETER, id)
                .setParameter(SystemClient.EMAIL_QUERY_PARAMETER, email)
                .getResultList();
        if (clients == null || clients.isEmpty()) {
            return null;
        }
        return clients.get(DEFAULT_CLIENT_INDEX);
    }
    
}
