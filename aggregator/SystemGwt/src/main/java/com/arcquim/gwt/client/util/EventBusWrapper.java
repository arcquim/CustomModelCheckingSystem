package com.arcquim.gwt.client.util;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class EventBusWrapper {
    
    private static final EventBus SIMPLE_EVENT_BUS_INSTANCE = new SimpleEventBus();
    
    private EventBusWrapper() {
        
    }
    
    public static EventBus getInstance() {
        return SIMPLE_EVENT_BUS_INSTANCE;
    }
    
}
