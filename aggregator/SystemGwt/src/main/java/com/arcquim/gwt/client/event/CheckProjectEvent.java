package com.arcquim.gwt.client.event;

import com.arcquim.gwt.client.event.handler.CheckProjectEventHandler;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.google.web.bindery.event.shared.Event;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CheckProjectEvent extends Event<CheckProjectEventHandler> {
    
    public static final Type<CheckProjectEventHandler> TYPE = new Type<>();
    
    private final ProjectDescriptor projectDescriptor;

    public CheckProjectEvent(ProjectDescriptor projectDescriptor) {
        this.projectDescriptor = projectDescriptor;
    }

    @Override
    public Type<CheckProjectEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CheckProjectEventHandler handler) {
        handler.handleCheckProjectEvent(this);
    }

    public ProjectDescriptor getProjectDescriptor() {
        return projectDescriptor;
    }
    
}
