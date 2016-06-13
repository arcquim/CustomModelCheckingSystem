package com.arcquim.gwt.client.event;

import com.arcquim.gwt.client.event.handler.NewProjectAppearedEventHandler;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.google.web.bindery.event.shared.Event;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class NewProjectAppearedEvent extends Event<NewProjectAppearedEventHandler> {
    
    public static final Type<NewProjectAppearedEventHandler> TYPE = new Type<>();
    
    private final ProjectDescriptor projectDescriptor;

    public NewProjectAppearedEvent(ProjectDescriptor projectDescriptor) {
        this.projectDescriptor = projectDescriptor;
    }

    @Override
    public Type<NewProjectAppearedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NewProjectAppearedEventHandler handler) {
        handler.handleNewProjectAppearedEvent(this);
    }

    public ProjectDescriptor getProjectDescriptor() {
        return projectDescriptor;
    }
    
}
