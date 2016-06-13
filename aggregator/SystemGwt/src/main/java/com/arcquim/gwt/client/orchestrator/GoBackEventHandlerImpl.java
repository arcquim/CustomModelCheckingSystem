package com.arcquim.gwt.client.orchestrator;

import com.arcquim.gwt.client.ApplicationConfiguration;
import com.arcquim.gwt.client.event.GoBackEvent;
import com.arcquim.gwt.client.event.SystemEnteredEvent;
import com.arcquim.gwt.client.event.handler.GoBackEventHandler;
import com.arcquim.gwt.client.util.EventBusWrapper;
import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class GoBackEventHandlerImpl implements GoBackEventHandler {

    @Override
    public void handleGoBackEvent(GoBackEvent event) {
        Record record = event.getRecord();
        String source = event.getSourcePageTitle();
        switch (source) {
            case ApplicationConfiguration.CHECKING_PAGE:
                
            case ApplicationConfiguration.HISTORY_PAGE:
                EventBusWrapper.getInstance().fireEvent(new SystemEnteredEvent(record.getClientDescriptor()));
                break;
                
            case ApplicationConfiguration.PROJECTS_PAGE:
                Orchestrator.getInstance().doStart(record);
                
        }
    }
    
}
