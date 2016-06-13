package com.arcquim.system.android.transport;


import com.arcquim.gwt.shared.Record;

public interface ResponseReceivedHandler {

    void onSuccess(Record record);
    void onFailure(Record record);

}
