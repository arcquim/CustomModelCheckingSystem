package com.arcquim.gwt.client.event;

import com.arcquim.gwt.client.event.handler.SystemEnteredEventHandler;
import com.arcquim.gwt.shared.ClientDescriptor;
import com.google.web.bindery.event.shared.Event;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class SystemEnteredEvent extends Event<SystemEnteredEventHandler> {
    
    public static final Type<SystemEnteredEventHandler> TYPE = new Type<>();
    
    private final ClientDescriptor clientDescriptor;
    
    public SystemEnteredEvent(ClientDescriptor clientDescriptor) {
        this.clientDescriptor = clientDescriptor;
    }

    @Override
    public Type<SystemEnteredEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SystemEnteredEventHandler handler) {
        handler.handleSystemEnteredEvent(this);
    }

    public ClientDescriptor getClientDescriptor() {
        return clientDescriptor;
    }
    
}
