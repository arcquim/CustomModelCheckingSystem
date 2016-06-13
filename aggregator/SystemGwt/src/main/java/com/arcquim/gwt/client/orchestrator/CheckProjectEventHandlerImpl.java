package com.arcquim.gwt.client.orchestrator;

import com.arcquim.gwt.client.event.CheckProjectEvent;
import com.arcquim.gwt.client.event.handler.CheckProjectEventHandler;
import com.arcquim.gwt.client.transport.Requester;
import com.arcquim.gwt.client.transport.ResponseReceivedHandler;
import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CheckProjectEventHandlerImpl implements CheckProjectEventHandler,
        ResponseReceivedHandler {
    
    private final Requester requester;
    
    public CheckProjectEventHandlerImpl(Requester requester) {
        this.requester = requester;
        this.requester.setResponseReceivedHandler(this);
    }

    @Override
    public void handleCheckProjectEvent(CheckProjectEvent event) {
        Record record = new Record();
        record.setProjectDescriptor(event.getProjectDescriptor());
        requester.sendRequest(record);
    }

    @Override
    public void onSuccess(Record record) {
        Orchestrator.getInstance().doModelChecking(record);
    }

    @Override
    public void onFailure(Record record) {
        Orchestrator.getInstance().doModelChecking(record);
    }
    
}
