package com.arcquim.gwt.client.event;

import com.arcquim.gwt.client.event.handler.CreateProjectEventHandler;
import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.google.web.bindery.event.shared.Event;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CreateProjectEvent extends Event<CreateProjectEventHandler> {
    
    public static final Type<CreateProjectEventHandler> TYPE = new Type<>();
    
    private final ProjectDescriptor projectDescriptor;
    private final ClientDescriptor clientDescriptor;

    public CreateProjectEvent(ProjectDescriptor projectDescriptor, ClientDescriptor clientDescriptor) {
        this.projectDescriptor = projectDescriptor;
        this.clientDescriptor = clientDescriptor;
    }

    @Override
    public Type<CreateProjectEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CreateProjectEventHandler handler) {
        handler.handleCreateProjectEvent(this);
    }

    public ProjectDescriptor getProjectDescriptor() {
        return projectDescriptor;
    }

    public ClientDescriptor getClientDescriptor() {
        return clientDescriptor;
    }
    
}
