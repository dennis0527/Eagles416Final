package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "NATIONAL_PARK", schema = "supaul", catalog = "")
public class NationalPark {
    private String canonicalName;
    private String canonicalStateName;
    private String neighbors;
    private Coordinates coordinatesByCanonicalName;
    private State stateByCanonicalStateName;
    private String geojson;

    @Id
    @Column(name = "canonical_name", nullable = false, length = 45)
    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    @Basic
    @Column(name = "canonical_state_name", nullable = false, length = 45)
    public String getCanonicalStateName() {
        return canonicalStateName;
    }

    public void setCanonicalStateName(String canonicalStateName) {
        this.canonicalStateName = canonicalStateName;
    }

    @Basic
    @Column(name = "neighbors", nullable = true)
    public String getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(String neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NationalPark that = (NationalPark) o;
        return Objects.equals(canonicalName, that.canonicalName) &&
                Objects.equals(canonicalStateName, that.canonicalStateName) &&
                Objects.equals(neighbors, that.neighbors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalName, canonicalStateName, neighbors);
    }

    @OneToOne
    @JoinColumn(name = "canonical_name", referencedColumnName = "canonical_name", nullable = false, insertable=false, updatable=false)
    public Coordinates getCoordinatesByCanonicalName() {
        return coordinatesByCanonicalName;
    }

    public void setCoordinatesByCanonicalName(Coordinates coordinatesByCanonicalName) {
        this.coordinatesByCanonicalName = coordinatesByCanonicalName;
    }

    @ManyToOne
    @JoinColumn(name = "canonical_state_name", referencedColumnName = "canonical_name", nullable = false, insertable=false, updatable=false)
    public State getStateByCanonicalStateName() {
        return stateByCanonicalStateName;
    }

    public void setStateByCanonicalStateName(State stateByCanonicalStateName) {
        this.stateByCanonicalStateName = stateByCanonicalStateName;
    }

    @Basic
    @Column(name = "geojson", nullable = true)
    public String getGeojson() {
        return geojson;
    }

    public void setGeojson(String geojson) {
        this.geojson = geojson;
    }
}
