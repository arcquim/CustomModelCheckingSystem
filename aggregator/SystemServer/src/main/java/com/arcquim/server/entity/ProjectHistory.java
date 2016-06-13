package com.arcquim.server.entity;

import com.arcquim.gwt.shared.ProjectHistoryDescriptor;
import com.arcquim.gwt.shared.Record;
import com.arcquim.server.transport.Recordable;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
@Entity
@Table(name = "projecthistory")
@NamedQueries({
    @NamedQuery(name = "ProjectHistory.findAll", query = "SELECT ph FROM ProjectHistory ph")})
public class ProjectHistory implements Serializable, Recordable {
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name="projecthistory_id_seq",
                       sequenceName="projecthistory_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="projecthistory_id_seq")
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "listing")
    private String listing;

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "property")
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
    
    @Column(name = "counter_examples")
    private String counterExamples;

    public String getCounterExamples() {
        return counterExamples;
    }

    public void setCounterExamples(String counterExamples) {
        this.counterExamples = counterExamples;
    }
    
    @JoinColumn(name = "result_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
    
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProjectHistory other = (ProjectHistory) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProjectHistory: id " + id + ", listing " + listing + 
                ", property " + property + ", result " + result + 
                ", project " + project;
    }

    @Override
    public Record translateIntoRecord() {
        Record record = new Record();
        ProjectHistoryDescriptor descriptor = new ProjectHistoryDescriptor();
        descriptor.setId(id);
        descriptor.setListing(listing);
        descriptor.setProperty(property);
        descriptor.setResult(result.getResultName());
        descriptor.setCounterExamples(counterExamples);
        descriptor.setProject(project.translateIntoRecord().getProjectDescriptor());
        record.setProjectHistoryDescriptor(descriptor);
        return record;
    }

    @Override
    public void translateFromRecord(Record record) {
        if (record != null) {
            ProjectHistoryDescriptor descriptor = record.getProjectHistoryDescriptor();
            if (descriptor != null) {
                String projectListing = descriptor.getListing();
                String projectProperty = descriptor.getProperty();
                if (projectListing != null) {
                    setListing(projectListing);
                }
                if (projectProperty != null) {
                    setProperty(projectProperty);
                }
            }
        }
    }
    
}
