package com.arcquim.system.android.transport;

public class EditProjectRequester extends Requester {

    private static final String REST_PATH =
            "http://10.0.3.2:2231/system/webresources/project/edit";

    @Override
    public String getPath() {
        return REST_PATH;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.PUT;
    }

}
