package com.arcquim.gwt.client.orchestrator;

import com.arcquim.gwt.client.event.SystemEnteredEvent;
import com.arcquim.gwt.client.event.handler.SystemEnteredEventHandler;
import com.arcquim.gwt.client.transport.Requester;
import com.arcquim.gwt.client.transport.ResponseReceivedHandler;
import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
class SystemEnteredEventHandlerImpl implements SystemEnteredEventHandler, 
        ResponseReceivedHandler {
    
    private final Requester requester;
    
    public SystemEnteredEventHandlerImpl(Requester requester) {
        this.requester = requester;
        this.requester.setResponseReceivedHandler(this);
    }

    @Override
    public void handleSystemEnteredEvent(SystemEnteredEvent event) {
        ClientDescriptor clientDescriptor = event.getClientDescriptor();
        Record requestRecord = new Record();
        requestRecord.setClientDescriptor(clientDescriptor);
        requester.sendRequest(requestRecord);
    }

    @Override
    public void onSuccess(Record record) {
        Orchestrator.getInstance().doEnterSystem(record);
    }

    @Override
    public void onFailure(Record record) {
        Orchestrator.getInstance().doEnterSystem(record);
    }
    
}
