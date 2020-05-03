package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ENCLOSED_ERRORS", schema = "supaul", catalog = "")
public class EnclosedErrors {
    private int id;
    private String stateName;
    private String enclosedPrecinct;
    private String enclosingPrecinct;

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
    @Column(name = "enclosed_precinct", nullable = true, length = 255)
    public String getEnclosedPrecinct() {
        return enclosedPrecinct;
    }

    public void setEnclosedPrecinct(String enclosedPrecinct) {
        this.enclosedPrecinct = enclosedPrecinct;
    }

    @Basic
    @Column(name = "enclosing_precinct", nullable = true, length = 255)
    public String getEnclosingPrecinct() {
        return enclosingPrecinct;
    }

    public void setEnclosingPrecinct(String enclosingPrecinct) {
        this.enclosingPrecinct = enclosingPrecinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnclosedErrors that = (EnclosedErrors) o;
        return id == that.id &&
                Objects.equals(stateName, that.stateName) &&
                Objects.equals(enclosedPrecinct, that.enclosedPrecinct) &&
                Objects.equals(enclosingPrecinct, that.enclosingPrecinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stateName, enclosedPrecinct, enclosingPrecinct);
    }
}
