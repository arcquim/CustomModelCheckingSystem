package com.arcquim.gwt.shared;

import com.googlecode.gwtstreamer.client.Streamable;
import java.util.List;

public class ClientDescriptor implements Streamable {

    private Long clientId;
    private String clientEmail;
    private List<ProjectDescriptor> projects;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public List<ProjectDescriptor> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDescriptor> projects) {
        this.projects = projects;
    }

}

