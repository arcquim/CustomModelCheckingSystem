package com.arcquim.server.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "result")
@NamedQueries({
    @NamedQuery(name = "Result.findById", query = "SELECT vr FROM Result vr WHERE vr.id = :" + Result.ID_QUERY_PARAMETER),
    @NamedQuery(name = "Result.findByName", query = "SELECT vr FROM Result vr WHERE vr.resultName = :" + Result.NAME_QUERY_PARAMETER),
    @NamedQuery(name = "Result.findAll", query = "SELECT vr FROM Result vr")})
public class Result implements Serializable {
    
    public static final String ID_QUERY_PARAMETER = "id";
    public static final String NAME_QUERY_PARAMETER = "name";
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name="result_id_seq",
                       sequenceName="result_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="result_id_seq")
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
    @Column(name = "result_name", unique = true)
    private String resultName;

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    @Override
    public String toString() {
        return "VerificationReullt: id " + id + ", resultName " + resultName;
    }

    @Override
    public int hashCode() {
        int hash = 67;
        hash += (id != null ? id.hashCode() : 0);
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
        final Result other = (Result) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
