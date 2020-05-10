package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "OVERLAPPING_ERRORS", schema = "supaul")
public class OverlappingErrors {
    private int id;
    private String precinctName;
    private String overlappingPrecinct;
    private String stateName;
    private String geometryJson;

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
    @Column(name = "overlapping_precinct", nullable = true)
    public String getOverlappingPrecinct() {
        return overlappingPrecinct;
    }

    public void setOverlappingPrecinct(String overlappingPrecincts) {
        this.overlappingPrecinct = overlappingPrecincts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverlappingErrors that = (OverlappingErrors) o;
        return id == that.id &&
                Objects.equals(precinctName, that.precinctName) &&
                Objects.equals(overlappingPrecinct, that.overlappingPrecinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, precinctName, overlappingPrecinct);
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
    @Column(name = "geometry_json", nullable = true)
    public String getGeometryJson() {
        return geometryJson;
    }

    public void setGeometryJson(String geometryJson) {
        this.geometryJson = geometryJson;
    }
}
