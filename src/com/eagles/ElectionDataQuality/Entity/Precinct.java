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
    private String canonicalDistrictName;
    private Collection<Correction> correctionsByCanonicalName;
    private Demographics demographicsByCanonicalName;
    private ElectionData electionDataByCanonicalName;
    private Collection<Error> errorsByCanonicalName;
    private Coordinates coordinatesByCanonicalName;
    private String geojson;
    private State stateByCanonicalStateName;
    private District districtByCanonicalDistrictName;

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

    @Basic
    @Column(name = "canonical_district_name", nullable = true, length = 45)
    public String getCanonicalDistrictName() {
        return canonicalDistrictName;
    }

    public void setCanonicalDistrictName(String canonicalDistrictName) {
        this.canonicalDistrictName = canonicalDistrictName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Precinct precinct = (Precinct) o;
        return Objects.equals(canonicalName, precinct.canonicalName) &&
                Objects.equals(fullName, precinct.fullName) &&
                Objects.equals(neighbors, precinct.neighbors) &&
                Objects.equals(canonicalStateName, precinct.canonicalStateName) &&
                Objects.equals(canonicalDistrictName, precinct.canonicalDistrictName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalName, fullName, neighbors, canonicalStateName, canonicalDistrictName);
    }

    @OneToMany(mappedBy = "precinctByCanonicalPrecinctName")
    public Collection<Correction> getCorrectionsByCanonicalName() {
        return correctionsByCanonicalName;
    }

    public void setCorrectionsByCanonicalName(Collection<Correction> correctionsByCanonicalName) {
        this.correctionsByCanonicalName = correctionsByCanonicalName;
    }

    @OneToOne(mappedBy = "precinctByCanonicalAreaName")
    public Demographics getDemographicsByCanonicalName() {
        return demographicsByCanonicalName;
    }

    public void setDemographicsByCanonicalName(Demographics demographicsByCanonicalName) {
        this.demographicsByCanonicalName = demographicsByCanonicalName;
    }

    @OneToOne(mappedBy = "precinctByCanonicalAreaName")
    public ElectionData getElectionDataByCanonicalName() {
        return electionDataByCanonicalName;
    }

    public void setElectionDataByCanonicalName(ElectionData electionDataByCanonicalName) {
        this.electionDataByCanonicalName = electionDataByCanonicalName;
    }

    @OneToMany(mappedBy = "precinctByCanonicalPrecinctName")
    public Collection<Error> getErrorsByCanonicalName() {
        return errorsByCanonicalName;
    }

    public void setErrorsByCanonicalName(Collection<Error> errorsByCanonicalName) {
        this.errorsByCanonicalName = errorsByCanonicalName;
    }

    @OneToOne
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

    @ManyToOne
    @JoinColumn(name = "canonical_state_name", referencedColumnName = "canonical_name", insertable = false, updatable = false)
    public State getStateByCanonicalStateName() {
        return stateByCanonicalStateName;
    }

    public void setStateByCanonicalStateName(State stateByCanonicalStateName) {
        this.stateByCanonicalStateName = stateByCanonicalStateName;
    }

    @ManyToOne
    @JoinColumn(name = "canonical_district_name", referencedColumnName = "canonical_name", insertable = false, updatable = false)
    public District getDistrictByCanonicalDistrictName() {
        return districtByCanonicalDistrictName;
    }

    public void setDistrictByCanonicalDistrictName(District districtByCanonicalDistrictName) {
        this.districtByCanonicalDistrictName = districtByCanonicalDistrictName;
    }
}
