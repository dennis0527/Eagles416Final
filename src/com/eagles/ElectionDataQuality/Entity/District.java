package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "supaul", name = "DISTRICT")
public class District {
    private String canonicalName;
    private String fullName;
    private String canonicalStateName;
    private Demographics demographicsByCanonicalName;
    private Coordinates coordinatesByCanonicalName;
    private ElectionData electionDataByCanonicalName;
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
    @Column(name = "full_name", nullable = true, length = 45)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        District district = (District) o;
        return Objects.equals(canonicalName, district.canonicalName) &&
                Objects.equals(fullName, district.fullName) &&
                Objects.equals(canonicalStateName, district.canonicalStateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalName, fullName, canonicalStateName);
    }

    @OneToOne(mappedBy = "districtByCanonicalAreaName")
    public Demographics getDemographicsByCanonicalName() {
        return demographicsByCanonicalName;
    }

    public void setDemographicsByCanonicalName(Demographics demographicsByCanonicalName) {
        this.demographicsByCanonicalName = demographicsByCanonicalName;
    }

    @OneToOne
    @JoinColumn(name = "canonical_name", referencedColumnName = "canonical_name", nullable = false, insertable=false, updatable=false)
    public Coordinates getCoordinatesByCanonicalName() {
        return coordinatesByCanonicalName;
    }

    public void setCoordinatesByCanonicalName(Coordinates coordinatesByCanonicalName) {
        this.coordinatesByCanonicalName = coordinatesByCanonicalName;
    }

    @OneToOne(mappedBy = "districtByCanonicalAreaName")
    public ElectionData getElectionDataByCanonicalName() {
        return electionDataByCanonicalName;
    }

    public void setElectionDataByCanonicalName(ElectionData electionDataByCanonicalName) {
        this.electionDataByCanonicalName = electionDataByCanonicalName;
    }

    @OneToMany(mappedBy = "districtByCanonicalDistrictName")
    public Collection<Precinct> getPrecinctsByCanonicalName() {
        return precinctsByCanonicalName;
    }

    public void setPrecinctsByCanonicalName(Collection<Precinct> precinctsByCanonicalName) {
        this.precinctsByCanonicalName = precinctsByCanonicalName;
    }
}
