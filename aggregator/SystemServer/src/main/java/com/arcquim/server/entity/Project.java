package com.arcquim.server.entity;

import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.gwt.shared.Record;
import com.arcquim.server.transport.Recordable;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
@Entity
@Table(name = "project")
@NamedQueries({
    @NamedQuery(name = "Project.findById", query = "SELECT p FROM Project p WHERE p.id = :" + Project.ID_QUERY_PARAMETER),
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p")})
public class Project implements Serializable, Recordable {
    
    public static final String ID_QUERY_PARAMETER = "id";
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name="project_id_seq",
                       sequenceName="project_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="project_id_seq")
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
    @Column(name = "title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private Boolean active = true;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemClient systemClient;

    public SystemClient getSystemClient() {
        return systemClient;
    }

    public void setSystemClient(SystemClient systemClient) {
        this.systemClient = systemClient;
    }
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private List<ProjectHistory> attempts;

    public List<ProjectHistory> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<ProjectHistory> attempts) {
        this.attempts = attempts;
    }

    @Override
    public String toString() {
        return "Project: id " + id + ", title " + title + ", active " + active + ", systemClient " + systemClient;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final Project other = (Project) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.systemClient, other.systemClient)) {
            return false;
        }
        return true;
    }

    @Override
    public Record translateIntoRecord() {
        Record record = new Record();
        ProjectDescriptor projectDescriptor = new ProjectDescriptor();
        projectDescriptor.setId(id);
        projectDescriptor.setTitle(title);
        projectDescriptor.setActive(active);
        record.setProjectDescriptor(projectDescriptor);
        return record;
    }

    @Override
    public void translateFromRecord(Record record) {
        if (record != null) {
            ProjectDescriptor projectDescriptor = record.getProjectDescriptor();
            if (projectDescriptor != null) {
                String projectTitle = projectDescriptor.getTitle();
                Boolean isProjectActive = projectDescriptor.getActive();
                if (projectTitle != null) {
                    setTitle(projectTitle);
                }
                if (isProjectActive != null) {
                    setActive(isProjectActive);
                }
            }
        }
    }
    
}