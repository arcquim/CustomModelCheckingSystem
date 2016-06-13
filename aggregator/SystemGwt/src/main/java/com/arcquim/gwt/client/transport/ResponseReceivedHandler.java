package com.arcquim.gwt.client.transport;

import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public interface ResponseReceivedHandler {
    
    void onSuccess(Record record);
    void onFailure(Record record);
    
}
