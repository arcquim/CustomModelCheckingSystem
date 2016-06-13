package com.arcquim.gwt.client.orchestrator;

import com.arcquim.gwt.client.event.CreateProjectEvent;
import com.arcquim.gwt.client.event.handler.CreateProjectEventHandler;
import com.arcquim.gwt.client.transport.Requester;
import com.arcquim.gwt.client.transport.ResponseReceivedHandler;
import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CreateProjectEventHandlerImpl implements CreateProjectEventHandler,
        ResponseReceivedHandler {
    
    private final Requester requester;
    
    public CreateProjectEventHandlerImpl(Requester requester) {
        this.requester = requester;
        this.requester.setResponseReceivedHandler(this);
    }

    @Override
    public void handleCreateProjectEvent(CreateProjectEvent event) {
        Record record = new Record();
        record.setClientDescriptor(event.getClientDescriptor());
        record.setProjectDescriptor(event.getProjectDescriptor());
        requester.sendRequest(record);
    }

    @Override
    public void onSuccess(Record record) {
        Orchestrator.getInstance().doCreateProject(record);
    }

    @Override
    public void onFailure(Record record) {
        Orchestrator.getInstance().doNotCreateProject(record);
    }
    
}
