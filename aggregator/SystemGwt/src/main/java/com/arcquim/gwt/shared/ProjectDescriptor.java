package com.arcquim.gwt.shared;

import com.googlecode.gwtstreamer.client.Streamable;
import java.util.List;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ProjectDescriptor implements Streamable {
    
    private ClientDescriptor client;
    private Long id;
    private String title;
    private Boolean active;
    private List<ProjectHistoryDescriptor> history;
    
    private List<String> atomicPredicates;
    private String program;
    private String property;

    public ClientDescriptor getClient() {
        return client;
    }

    public void setClient(ClientDescriptor client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ProjectHistoryDescriptor> getHistory() {
        return history;
    }

    public void setHistory(List<ProjectHistoryDescriptor> history) {
        this.history = history;
    }

    public List<String> getAtomicPredicates() {
        return atomicPredicates;
    }

    public void setAtomicPredicates(List<String> atomicPredicates) {
        this.atomicPredicates = atomicPredicates;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
    
}
