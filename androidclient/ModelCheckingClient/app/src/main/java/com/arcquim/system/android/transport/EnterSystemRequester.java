package com.arcquim.system.android.transport;

public class EnterSystemRequester extends Requester {

    private static final String REST_PATH =
            "http://10.0.3.2:2231/system/webresources/user/enter";

    @Override
    public String getPath() {
        return REST_PATH;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

}
