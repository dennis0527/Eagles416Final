package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "supaul", name = "STATE")
public class State {
    private String canonicalName;
    private String abbreviation;
    private String fullName;
    private String geojson;
    private Demographics demographicsByCanonicalName;
    private Collection<NationalPark> nationalParksByCanonicalName;
    private Coordinates coordinatesByCanonicalName;
    private String enclosedErrors;
    private String neighbors;
    private String anomalousErrors;
    private Collection<Precinct> precinctsByCanonicalName;

    @Id
    @Column(name = "canonical_name", nullable = false, length = 45)
    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    @Basic
    @Column(name = "abbreviation", nullable = true, length = 45)
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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
    @Column(name = "geojson", nullable = true)
    public String getGeojson() {
        return geojson;
    }

    public void setGeojson(String geojson) {
        this.geojson = geojson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(canonicalName, state.canonicalName) &&
                Objects.equals(abbreviation, state.abbreviation) &&
                Objects.equals(fullName, state.fullName) &&
                Objects.equals(geojson, state.geojson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalName, abbreviation, fullName, geojson);
    }

    @OneToOne(mappedBy = "stateByCanonicalAreaName")
    public Demographics getDemographicsByCanonicalName() {
        return demographicsByCanonicalName;
    }

    public void setDemographicsByCanonicalName(Demographics demographicsByCanonicalName) {
        this.demographicsByCanonicalName = demographicsByCanonicalName;
    }

    @OneToMany(mappedBy = "stateByCanonicalStateName")
    public Collection<NationalPark> getNationalParksByCanonicalName() {
        return nationalParksByCanonicalName;
    }

    public void setNationalParksByCanonicalName(Collection<NationalPark> nationalParksByCanonicalName) {
        this.nationalParksByCanonicalName = nationalParksByCanonicalName;
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
    @Column(name = "enclosed_errors", nullable = true)
    public String getEnclosedErrors() {
        return enclosedErrors;
    }

    public void setEnclosedErrors(String enclosedErrors) {
        this.enclosedErrors = enclosedErrors;
    }

    @Basic
    @Column(name = "neighbors", nullable = true, length = -1)
    public String getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(String neighbors) {
        this.neighbors = neighbors;
    }

    @Basic
    @Column(name = "anomalous_errors", nullable = true)
    public String getAnomalousErrors() {
        return anomalousErrors;
    }

    public void setAnomalousErrors(String anomalousErrors) {
        this.anomalousErrors = anomalousErrors;
    }

    @OneToMany(mappedBy = "stateByCanonicalStateName")
    public Collection<Precinct> getPrecinctsByCanonicalName() {
        return precinctsByCanonicalName;
    }

    public void setPrecinctsByCanonicalName(Collection<Precinct> precinctsByCanonicalName) {
        this.precinctsByCanonicalName = precinctsByCanonicalName;
    }
}
