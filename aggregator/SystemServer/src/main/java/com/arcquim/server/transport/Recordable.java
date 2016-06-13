package com.arcquim.server.transport;

import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public interface Recordable {
    
    Record translateIntoRecord();
    
    void translateFromRecord(Record record);
    
}
