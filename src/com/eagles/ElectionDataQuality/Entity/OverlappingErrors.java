package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "OVERLAPPING_ERRORS", schema = "supaul", catalog = "")
public class OverlappingErrors {
    private int id;
    private String precinctName;
    private String overlappingPrecincts;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "overlapping_precincts", nullable = true)
    public String getOverlappingPrecincts() {
        return overlappingPrecincts;
    }

    public void setOverlappingPrecincts(String overlappingPrecincts) {
        this.overlappingPrecincts = overlappingPrecincts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverlappingErrors that = (OverlappingErrors) o;
        return id == that.id &&
                Objects.equals(precinctName, that.precinctName) &&
                Objects.equals(overlappingPrecincts, that.overlappingPrecincts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, precinctName, overlappingPrecincts);
    }
}