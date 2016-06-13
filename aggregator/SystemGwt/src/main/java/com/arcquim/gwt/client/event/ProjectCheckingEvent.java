package com.arcquim.gwt.client.event;

import com.arcquim.gwt.client.event.handler.ProjectCheckingEventHandler;
import com.arcquim.gwt.shared.Record;
import com.google.web.bindery.event.shared.Event;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ProjectCheckingEvent extends Event<ProjectCheckingEventHandler> {
    
    public static final Type<ProjectCheckingEventHandler> TYPE = new Type<>();
    
    private final Record record;

    public ProjectCheckingEvent(Record record) {
        this.record = record;
    }

    @Override
    public Type<ProjectCheckingEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ProjectCheckingEventHandler handler) {
        handler.handleProjectCheckingEvent(this);
    }

    public Record getRecord() {
        return record;
    }
    
}
