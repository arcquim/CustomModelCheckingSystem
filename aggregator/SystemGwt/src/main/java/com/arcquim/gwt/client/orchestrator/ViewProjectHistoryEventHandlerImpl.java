package com.arcquim.gwt.client.orchestrator;

import com.arcquim.gwt.client.event.ViewProjectHistoryEvent;
import com.arcquim.gwt.client.event.handler.ViewProjectHistoryEventHandler;
import com.arcquim.gwt.client.transport.Requester;
import com.arcquim.gwt.client.transport.ResponseReceivedHandler;
import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ViewProjectHistoryEventHandlerImpl implements ViewProjectHistoryEventHandler,
        ResponseReceivedHandler {
    
    private final Requester requester;
    
    public ViewProjectHistoryEventHandlerImpl(Requester requester) {
        this.requester = requester;
        this.requester.setResponseReceivedHandler(this);
    }

    @Override
    public void handleViewHistoryEvent(ViewProjectHistoryEvent event) {
        String id = event.getProjectDescriptor().getId().toString();
        requester.setQueryParametersString(id);
        requester.sendRequest(new Record());
    }

    @Override
    public void onSuccess(Record record) {
        Orchestrator.getInstance().doViewProjectHistory(record);
    }

    @Override
    public void onFailure(Record record) {
        Orchestrator.getInstance().doViewProjectHistory(record);
    }
    
}
