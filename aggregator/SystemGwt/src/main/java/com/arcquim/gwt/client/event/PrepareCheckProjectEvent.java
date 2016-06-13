package com.arcquim.gwt.client.event;

import com.arcquim.gwt.client.event.handler.PrepareCheckProjectEventHandler;
import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.google.web.bindery.event.shared.Event;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class PrepareCheckProjectEvent extends Event<PrepareCheckProjectEventHandler> {
    
    public static final Type<PrepareCheckProjectEventHandler> TYPE = new Type<>();
    
    private final ProjectDescriptor projectDescriptor;
    private final ClientDescriptor clientDescriptor;

    public PrepareCheckProjectEvent(ProjectDescriptor projectDescriptor, ClientDescriptor clientDescriptor) {
        this.projectDescriptor = projectDescriptor;
        this.clientDescriptor = clientDescriptor;
    }

    @Override
    public Type<PrepareCheckProjectEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PrepareCheckProjectEventHandler handler) {
        handler.handlePrepareCheckProjectEvent(this);
    }

    public ProjectDescriptor getProjectDescriptor() {
        return projectDescriptor;
    }

    public ClientDescriptor getClientDescriptor() {
        return clientDescriptor;
    }
    
}
