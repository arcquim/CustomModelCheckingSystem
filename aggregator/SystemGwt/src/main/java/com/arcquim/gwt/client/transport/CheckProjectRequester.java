package com.arcquim.gwt.client.transport;

import com.google.gwt.core.client.GWT;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CheckProjectRequester extends Requester {
    
    private static final String REST_PATH = GWT.getHostPageBaseURL().substring(0, 
            GWT.getHostPageBaseURL().length() - 2) + 
            "/webresources/project/check";

    @Override
    public String getPath() {
        return REST_PATH;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }
    
}
