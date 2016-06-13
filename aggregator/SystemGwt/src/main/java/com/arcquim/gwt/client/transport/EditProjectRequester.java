package com.arcquim.gwt.client.transport;

import com.google.gwt.core.client.GWT;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class EditProjectRequester extends Requester {
    
    private static final String REST_PATH = GWT.getHostPageBaseURL().substring(0, 
            GWT.getHostPageBaseURL().length() - 2) + 
            "/webresources/project/edit";

    @Override
    public String getPath() {
        return REST_PATH;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.PUT;
    }
    
}
