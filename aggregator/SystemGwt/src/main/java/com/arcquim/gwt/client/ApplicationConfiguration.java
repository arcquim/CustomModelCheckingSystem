package com.arcquim.gwt.client;

import com.arcquim.gwt.client.project.ProjectsCompositeWidget;
import com.arcquim.gwt.client.project.check.ProjectCheckCompositeWidget;
import com.arcquim.gwt.client.project.history.HistoryCompositeWidget;
import com.arcquim.gwt.client.start.EmailCompositeWidget;
import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ApplicationConfiguration {
    
    private static final ApplicationConfiguration INSTANCE = new ApplicationConfiguration();
    
    public static final String APPLICATION_ID = "application";
    
    public static final String WELCOME_PAGE = "index";
    public static final String PROJECTS_PAGE = "projects";
    public static final String HISTORY_PAGE = "history";
    public static final String CHECKING_PAGE = "checking";
    
    private String applicationId = APPLICATION_ID;
    
    private ApplicationConfiguration() {
        
    }
    
    public static ApplicationConfiguration getInstance() {
        return INSTANCE;
    }
    
    public ConfigurableComposite getPage(String token) {
        return getPage(token, null);
    }
    
    public ConfigurableComposite getPage(String token, Record record) {
        ConfigurableComposite composite;
        switch (token) {
            case WELCOME_PAGE:
                composite = new EmailCompositeWidget();
                break;
            case PROJECTS_PAGE:
                composite = new ProjectsCompositeWidget();
                break;
            case HISTORY_PAGE:
                composite = new HistoryCompositeWidget();
                break;
            case CHECKING_PAGE:
                composite = new ProjectCheckCompositeWidget();
                break;
            default:
                composite = null;
        }
        if (composite != null) {
            composite.setCurrentRecord(record);
        }
        return composite;
    }
    
    public String getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
}
