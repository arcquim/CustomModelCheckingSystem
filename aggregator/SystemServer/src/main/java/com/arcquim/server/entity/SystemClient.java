package com.arcquim.server.entity;

import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.gwt.shared.Record;
import com.arcquim.server.transport.Recordable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
@Entity
@Table(name = "systemclients")
@NamedQueries({
    @NamedQuery(name = "SystemClient.findAll", query = "SELECT sc FROM SystemClient sc"),
    @NamedQuery(name = "SystemClient.findById", query = "SELECT sc FROM SystemClient sc WHERE sc.id = :" + SystemClient.ID_QUERY_PARAMETER),
    @NamedQuery(name = "SystemClient.findByIdAndEmail", query = "SELECT sc FROM SystemClient sc WHERE sc.id = :" + SystemClient.ID_QUERY_PARAMETER + " AND sc.email = :" + SystemClient.EMAIL_QUERY_PARAMETER),
    @NamedQuery(name = "SystemClient.findByEmail", query = "SELECT sc FROM SystemClient sc WHERE sc.email=:" + SystemClient.EMAIL_QUERY_PARAMETER)})
public class SystemClient implements Serializable, Recordable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String EMAIL_QUERY_PARAMETER = "email";
    public static final String ID_QUERY_PARAMETER = "id";
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name="systemclients_id_seq",
                       sequenceName="systemclients_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="systemclients_id_seq")
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
    @Column(name = "email", unique = true)
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email address")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "busy")
    private Boolean busy = false;

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemClient")
    private List<Project> projects;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public int hashCode() {
        int hash = 71;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemClient)) {
            return false;
        }
        SystemClient other = (SystemClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SystemClient: id " + id + ", email " + email + ", busy " + busy;
    }

    @Override
    public Record translateIntoRecord() {
        Record record = new Record();
        ClientDescriptor clientDescriptor = new ClientDescriptor();
        clientDescriptor.setClientId(id);
        clientDescriptor.setClientEmail(email);
        if (projects != null) {
            List<ProjectDescriptor> projectDescriptors = new ArrayList<>();
            for (Project project : projects) {
                projectDescriptors.add(project.translateIntoRecord().getProjectDescriptor());
            }
            clientDescriptor.setProjects(projectDescriptors);
        }
        record.setClientDescriptor(clientDescriptor);
        return record;
    }

    @Override
    public void translateFromRecord(Record record) {
        if (record != null) {
            ClientDescriptor clientDescriptor = record.getClientDescriptor();
            if (clientDescriptor != null) {
                String clientEmail = clientDescriptor.getClientEmail();
                if (clientEmail != null) {
                    setEmail(clientEmail);
                }
            }
        }
    }
    
}
