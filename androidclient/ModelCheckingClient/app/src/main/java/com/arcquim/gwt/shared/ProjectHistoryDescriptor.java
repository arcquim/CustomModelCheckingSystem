package com.arcquim.gwt.shared;

import com.arcquim.gwt.shared.ProjectDescriptor;
import com.googlecode.gwtstreamer.client.Streamable;

public class ProjectHistoryDescriptor implements Streamable {

    private Long id;
    private String result;
    private String listing;
    private String property;
    private String counterExamples;
    private ProjectDescriptor project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getCounterExamples() {
        return counterExamples;
    }

    public void setCounterExamples(String counterExamples) {
        this.counterExamples = counterExamples;
    }

    public ProjectDescriptor getProject() {
        return project;
    }

    public void setProject(ProjectDescriptor project) {
        this.project = project;
    }

}
