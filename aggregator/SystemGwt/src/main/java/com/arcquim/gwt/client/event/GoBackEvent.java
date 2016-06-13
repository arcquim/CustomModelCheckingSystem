package com.arcquim.gwt.client.event;

import com.arcquim.gwt.client.event.handler.GoBackEventHandler;
import com.arcquim.gwt.shared.Record;
import com.google.web.bindery.event.shared.Event;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class GoBackEvent extends Event<GoBackEventHandler> {
    
    public static final Type<GoBackEventHandler> TYPE = new Type<>();
    
    private final Record record;
    private final String sourcePageTitle;

    public GoBackEvent(Record record, String sourcePageTitle) {
        this.record = record;
        this.sourcePageTitle = sourcePageTitle;
    }

    @Override
    public Type<GoBackEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GoBackEventHandler handler) {
        handler.handleGoBackEvent(this);
    }

    public Record getRecord() {
        return record;
    }

    public String getSourcePageTitle() {
        return sourcePageTitle;
    }
    
}
