package com.arcquim.gwt.client.orchestrator;

import com.arcquim.gwt.client.event.PrepareCheckProjectEvent;
import com.arcquim.gwt.client.event.handler.PrepareCheckProjectEventHandler;
import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class PrepareCheckProjectEventHandlerImpl implements PrepareCheckProjectEventHandler {

    @Override
    public void handlePrepareCheckProjectEvent(PrepareCheckProjectEvent event) {
        Record record = new Record();
        record.setProjectDescriptor(event.getProjectDescriptor());
        record.setClientDescriptor(event.getClientDescriptor());
        Orchestrator.getInstance().doPrepareModelChecking(record);
    }
    
}
