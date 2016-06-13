package com.arcquim.gwt.shared;

import com.googlecode.gwtstreamer.client.Streamable;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class Record implements Streamable {
    
    public static final String RECORD_KEY = "payload";
    
    private ProjectDescriptor projectDescriptor;
    private ClientDescriptor clientDescriptor;
    private ProjectHistoryDescriptor projectHistoryDescriptor;

    public ProjectDescriptor getProjectDescriptor() {
        return projectDescriptor;
    }

    public void setProjectDescriptor(ProjectDescriptor projectDescriptor) {
        this.projectDescriptor = projectDescriptor;
    }

    public ClientDescriptor getClientDescriptor() {
        return clientDescriptor;
    }

    public void setClientDescriptor(ClientDescriptor clientDescriptor) {
        this.clientDescriptor = clientDescriptor;
    }

    public ProjectHistoryDescriptor getProjectHistoryDescriptor() {
        return projectHistoryDescriptor;
    }

    public void setProjectHistoryDescriptor(ProjectHistoryDescriptor projectHistoryDescriptor) {
        this.projectHistoryDescriptor = projectHistoryDescriptor;
    }
    
}
    
