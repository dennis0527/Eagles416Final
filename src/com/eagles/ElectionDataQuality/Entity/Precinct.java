package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "supaul", name = "PRECINCT")
public class Precinct {
    private String canonicalName;
    private String fullName;
    private String neighbors;
    private String canonicalStateName;
    private Collection<Correction> correctionsByCanonicalName;
    private Demographics demographicsByCanonicalName;
    private ElectionData electionDataByCanonicalName;
    private Coordinates coordinatesByCanonicalName;
    private String geojson;
    private State stateByCanonicalStateName;

    @Id
    @Column(name = "canonical_name", nullable = false, length = 45)
    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    @Basic
    @Column(name = "full_name", nullable = true, length = 45)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Basic
    @Column(name = "neighbors", nullable = true)
    public String getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(String neighbors) {
        this.neighbors = neighbors;
    }

    @Basic
    @Column(name = "canonical_state_name", nullable = true, length = 45)
    public String getCanonicalStateName() {
        return canonicalStateName;
    }

    public void setCanonicalStateName(String canonicalStateName) {
        this.canonicalStateName = canonicalStateName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Precinct precinct = (Precinct) o;
        return Objects.equals(canonicalName, precinct.canonicalName) &&
                Objects.equals(fullName, precinct.fullName) &&
                Objects.equals(neighbors, precinct.neighbors) &&
                Objects.equals(canonicalStateName, precinct.canonicalStateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalName, fullName, neighbors, canonicalStateName);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "precinctByCanonicalPrecinctName")
    public Collection<Correction> getCorrectionsByCanonicalName() {
        return correctionsByCanonicalName;
    }

    public void setCorrectionsByCanonicalName(Collection<Correction> correctionsByCanonicalName) {
        this.correctionsByCanonicalName = correctionsByCanonicalName;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "precinctByCanonicalAreaName")
    public Demographics getDemographicsByCanonicalName() {
        return demographicsByCanonicalName;
    }

    public void setDemographicsByCanonicalName(Demographics demographicsByCanonicalName) {
        this.demographicsByCanonicalName = demographicsByCanonicalName;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "precinctByCanonicalAreaName")
    public ElectionData getElectionDataByCanonicalName() {
        return electionDataByCanonicalName;
    }

    public void setElectionDataByCanonicalName(ElectionData electionDataByCanonicalName) {
        this.electionDataByCanonicalName = electionDataByCanonicalName;
    }



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canonical_name", referencedColumnName = "canonical_name", nullable = false, insertable=false, updatable=false)
    public Coordinates getCoordinatesByCanonicalName() {
        return coordinatesByCanonicalName;
    }

    public void setCoordinatesByCanonicalName(Coordinates coordinatesByCanonicalName) {
        this.coordinatesByCanonicalName = coordinatesByCanonicalName;
    }

    @Basic
    @Column(name = "geojson", nullable = true)
    public String getGeojson() {
        return geojson;
    }

    public void setGeojson(String geojson) {
        this.geojson = geojson;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canonical_state_name", referencedColumnName = "canonical_name", insertable = false, updatable = false)
    public State getStateByCanonicalStateName() {
        return stateByCanonicalStateName;
    }

    public void setStateByCanonicalStateName(State stateByCanonicalStateName) {
        this.stateByCanonicalStateName = stateByCanonicalStateName;
    }

}
