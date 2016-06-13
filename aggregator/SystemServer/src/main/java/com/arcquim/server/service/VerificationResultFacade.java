package com.arcquim.server.service;

import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.server.entity.Project;
import com.arcquim.server.entity.ProjectHistory;
import com.arcquim.server.entity.Result;
import com.arcquim.server.transport.MailSender;
import com.arcquim.server.transport.SmtpMailSender;
import com.system.CTLVerificator;
import com.system.Controller;
import com.system.VerificationResult;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
@Stateless
public class VerificationResultFacade extends AbstractFacade<Result> {
    
    private static final int DEFAULT_RESULT_INDEX = 0;
    
    private static final int DEFAULT_NUMBER_OF_COUNTER_EXAMPLES = 10;
    
    private final MailSender mailSender = new SmtpMailSender();
    
    private static final String EMAIL_SUBJECT = "The verification has finished just now!";
    private static final String EMAIL_MODEL_BODY_TEMPLATE = "The model is: \n";
    private static final String EMAIL_PROPERTY_BODY_TEMPLATE = "The property is: \n";
    private static final String EMAIL_RESULT_BODY_TEMPLATE = "The result is: \n";
    private static final String EMAIL_COUNTER_EXAMPLES_BODY_TEMPLATE = "The counterexamples are: \n";
    
    @PersistenceContext(unitName = "com.arcquim_SystemServer_war_1.0-SNAPSHOTPU")
    private EntityManager entityManager;
    
    @EJB
    private ProjectHistoryFacade projectHistoryFacade;
    
    @EJB
    private SystemClientFacade clientFacade;
    
    @EJB
    private ProjectFacade projectFacade;
    
    public VerificationResultFacade() {
        this(Result.class);
    }

    public VerificationResultFacade(Class<Result> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    @Asynchronous
    public void runModelChecking(ProjectDescriptor descriptor, Project project) {
        Controller systemController = new Controller();
        if (descriptor == null || descriptor.getProgram() == null 
                || descriptor.getProperty() == null
                || descriptor.getProperty().isEmpty()) 
        {
            List<Result> result = entityManager.createNamedQuery("Result.findByName")
                    .setParameter(Result.NAME_QUERY_PARAMETER, VerificationResult.WRONG_INPUT)
                    .getResultList();
            Result resultOfVerification = result.get(DEFAULT_RESULT_INDEX);
            notifyUser(project.getSystemClient().getEmail(), 
                    descriptor == null || descriptor.getProgram() == null ? "" : descriptor.getProgram(), 
                    descriptor == null || descriptor.getProperty() == null ? "" : descriptor.getProperty(), 
                    resultOfVerification.getResultName(), null);
            ProjectHistory newAttempt = new ProjectHistory();
            newAttempt.setProperty(descriptor == null || descriptor.getProperty() == null 
                    ? "" : descriptor.getProperty());
            newAttempt.setListing(descriptor == null || descriptor.getProgram() == null 
                    ? "" : descriptor.getProgram());
            newAttempt.setResult(resultOfVerification);
            newAttempt.setProject(project);
            newAttempt.setCounterExamples(null);
            projectHistoryFacade.create(newAttempt);
            project.getSystemClient().setBusy(Boolean.FALSE);
            clientFacade.edit(project.getSystemClient());
            projectFacade.edit(project);
            return;
        }
        List<String> properties = descriptor.getAtomicPredicates();
        systemController.setAtomicPredicates(properties);
        systemController.setCTLFormula(descriptor.getProperty());
        systemController.setProgram(descriptor.getProgram());
        CTLVerificator verificator = systemController.startVerification();
        VerificationResult verificationResult;
        String result;
        if (verificator == null) {
            verificationResult = null;
            result = VerificationResult.WRONG_INPUT;
        }
        else {
            verificationResult = verificator.getVerificationResult();
            result = verificationResult.toDatabaseString();
        }
        
        List<Result> resultList = entityManager.createNamedQuery("Result.findByName")
                    .setParameter(Result.NAME_QUERY_PARAMETER, result)
                    .getResultList();
        List<String> counterExamplesList = null; 
        if (verificator != null) {
            counterExamplesList = verificator.getCounterexamples(DEFAULT_NUMBER_OF_COUNTER_EXAMPLES);
        }
        String counterExamples = listToString(counterExamplesList);
        String email = project.getSystemClient().getEmail();
        String dbProperty = createProperty(descriptor.getProperty(), 
                descriptor.getAtomicPredicates());
        notifyUser(email, descriptor.getProgram(), dbProperty, 
                result, counterExamples);
        Result resultOfVerification = resultList.get(DEFAULT_RESULT_INDEX);
        ProjectHistory newAttempt = new ProjectHistory();
        newAttempt.setProperty(dbProperty);
        newAttempt.setListing(descriptor.getProgram());
        newAttempt.setResult(resultOfVerification);
        newAttempt.setProject(project);
        newAttempt.setCounterExamples(counterExamples);
        projectHistoryFacade.create(newAttempt);
        project.getSystemClient().setBusy(Boolean.FALSE);
        clientFacade.edit(project.getSystemClient());
        projectFacade.edit(project);
    }
    
    private String listToString(List<String> list) {
        if (list == null) {
            return null;
        }
        String result = "";
        for (String entry : list) {
            result += entry + "\n";
        }
        return result;
    }
    
    private String createProperty(String property, List<String> predicates) {
        if (property == null || property.trim().isEmpty()) {
            return property;
        }
        for (int i = 0; i < property.length(); ) {
            char nextChar = property.charAt(i);
            if (isDigit(nextChar)) {
                String stringToReplace = "";
                int j = i;
                do {
                    stringToReplace += nextChar;
                    j++;
                }
                while(j < property.length() && isDigit(nextChar = property.charAt(j)));
                int index = Integer.parseInt(stringToReplace);
                String replacingString = index < predicates.size() && index >= 0 ? predicates.get(index) : "??";
                property = property.substring(0, i) + 
                        replacingString 
                        + property.substring(j);
                i += replacingString.length() - stringToReplace.length() + 1;
            }
            else {
                i++;
            }
        }
        return property;
    }
    
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    
    private void notifyUser(String email, String program, String property, 
            String result, String counterExamples) 
    {
        String text = EMAIL_MODEL_BODY_TEMPLATE + program + "\n";
        text += EMAIL_PROPERTY_BODY_TEMPLATE + property + "\n";
        text += EMAIL_RESULT_BODY_TEMPLATE + result;
        if (counterExamples != null && !counterExamples.isEmpty()) {
            text += "\n" + EMAIL_COUNTER_EXAMPLES_BODY_TEMPLATE + counterExamples;
        }
        mailSender.sendEmail(email, EMAIL_SUBJECT, text);
    }
    
}
