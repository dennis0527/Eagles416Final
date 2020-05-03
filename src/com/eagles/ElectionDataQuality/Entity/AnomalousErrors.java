package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ANOMALOUS_ERRORS", schema = "supaul", catalog = "")
public class AnomalousErrors {
    private int id;
    private String stateName;
    private String precinctName;
    private String errorIdentifier;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "state_name", nullable = true, length = 45)
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Basic
    @Column(name = "precinct_name", nullable = true, length = 45)
    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName;
    }

    @Basic
    @Column(name = "error_identifier", nullable = true, length = 255)
    public String getErrorIdentifier() {
        return errorIdentifier;
    }

    public void setErrorIdentifier(String errorIdentifier) {
        this.errorIdentifier = errorIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnomalousErrors that = (AnomalousErrors) o;
        return id == that.id &&
                Objects.equals(stateName, that.stateName) &&
                Objects.equals(precinctName, that.precinctName) &&
                Objects.equals(errorIdentifier, that.errorIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stateName, precinctName, errorIdentifier);
    }
}
