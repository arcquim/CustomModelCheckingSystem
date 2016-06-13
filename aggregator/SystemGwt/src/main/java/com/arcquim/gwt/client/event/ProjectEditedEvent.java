package com.arcquim.gwt.client.event;

import com.arcquim.gwt.client.event.handler.ProjectEditedEventHandler;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.google.web.bindery.event.shared.Event;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ProjectEditedEvent extends Event<ProjectEditedEventHandler> {
    
    public static final Type<ProjectEditedEventHandler> TYPE = new Type<>();
    
    private final ProjectDescriptor projectDescriptor;

    public ProjectEditedEvent(ProjectDescriptor projectDescriptor) {
        this.projectDescriptor = projectDescriptor;
    }

    @Override
    public Type<ProjectEditedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ProjectEditedEventHandler handler) {
        handler.handleProjectEditedEvent(this);
    }

    public ProjectDescriptor getProjectDescriptor() {
        return projectDescriptor;
    }
    
}
