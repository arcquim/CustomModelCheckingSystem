package com.arcquim.gwt.client.event;

import com.arcquim.gwt.client.event.handler.ViewProjectHistoryEventHandler;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.google.web.bindery.event.shared.Event;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ViewProjectHistoryEvent extends Event<ViewProjectHistoryEventHandler> {
    
    public static final Type<ViewProjectHistoryEventHandler> TYPE = new Type<>();
    
    private final ProjectDescriptor projectDescriptor;

    public ViewProjectHistoryEvent(ProjectDescriptor projectDescriptor) {
        this.projectDescriptor = projectDescriptor;
    }

    @Override
    public Type<ViewProjectHistoryEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ViewProjectHistoryEventHandler handler) {
        handler.handleViewHistoryEvent(this);
    }

    public ProjectDescriptor getProjectDescriptor() {
        return projectDescriptor;
    }
    
}
