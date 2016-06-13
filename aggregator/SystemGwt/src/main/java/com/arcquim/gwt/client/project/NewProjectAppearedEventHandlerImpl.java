package com.arcquim.gwt.client.project;

import com.arcquim.gwt.client.event.NewProjectAppearedEvent;
import com.arcquim.gwt.client.event.handler.NewProjectAppearedEventHandler;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class NewProjectAppearedEventHandlerImpl implements NewProjectAppearedEventHandler {
    
    private ProjectsCompositeWidget.Row row;
    private final ProjectsCompositeWidget projectsWidget;
    private HandlerRegistration handlerRegistration;

    public NewProjectAppearedEventHandlerImpl(ProjectsCompositeWidget projectsWidget) {
        this.projectsWidget = projectsWidget;
        row = new ProjectsCompositeWidget.Row();
    }

    @Override
    public boolean handleNewProjectAppearedEvent(NewProjectAppearedEvent event) {
        handlerRegistration.removeHandler();
        ProjectDescriptor projectDescriptor = event.getProjectDescriptor();
        if (projectDescriptor == null) {
            return false;
        }
        Long projectId = projectDescriptor.getId();
        if (projectId == null) {
            return false;
        }
        row.projectId = projectId;
        row.title = projectDescriptor.getTitle();
        row.active = projectDescriptor.getActive();
        projectsWidget.addProject(row);
        return true;
    }
    
    public void setHandlerRegistration(HandlerRegistration handlerRegistration) {
        this.handlerRegistration = handlerRegistration;
    }
    
}
