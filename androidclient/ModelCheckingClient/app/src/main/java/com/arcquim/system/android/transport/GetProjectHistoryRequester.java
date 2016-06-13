package com.arcquim.system.android.transport;

public class GetProjectHistoryRequester extends Requester {

    private static final String REST_PATH =
            "http://10.0.3.2:2231/system/webresources/project/get/history/";

    private String queryParameters = "";

    @Override
    public String getPath() {
        return REST_PATH + queryParameters;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.GET;
    }

    @Override
    public void setQueryParametersString(String queryParameters) {
        super.setQueryParametersString(queryParameters);
        this.queryParameters = queryParameters;
    }

}
