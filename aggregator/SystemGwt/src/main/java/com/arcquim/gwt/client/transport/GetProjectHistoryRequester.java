package com.arcquim.gwt.client.transport;

import com.google.gwt.core.client.GWT;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class GetProjectHistoryRequester extends Requester {
    
    private static final String REST_PATH = GWT.getHostPageBaseURL().substring(0, 
            GWT.getHostPageBaseURL().length() - 2) + 
            "/webresources/project/get/history/";
    
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
