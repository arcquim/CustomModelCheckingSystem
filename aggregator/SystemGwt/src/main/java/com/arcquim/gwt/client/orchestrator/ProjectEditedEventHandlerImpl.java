package com.arcquim.gwt.client.orchestrator;

import com.arcquim.gwt.client.event.ProjectEditedEvent;
import com.arcquim.gwt.client.event.handler.ProjectEditedEventHandler;
import com.arcquim.gwt.client.transport.Requester;
import com.arcquim.gwt.client.transport.ResponseReceivedHandler;
import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ProjectEditedEventHandlerImpl implements ProjectEditedEventHandler, 
        ResponseReceivedHandler {
    
    private final Requester requester;
    
    public ProjectEditedEventHandlerImpl(Requester requester) {
        this.requester = requester;
        this.requester.setResponseReceivedHandler(this);
    }

    @Override
    public void handleProjectEditedEvent(ProjectEditedEvent event) {
        Record record = new Record();
        record.setProjectDescriptor(event.getProjectDescriptor());
        requester.sendRequest(record);
    }

    @Override
    public void onSuccess(Record record) {
        
    }

    @Override
    public void onFailure(Record record) {
        
    }
    
}
