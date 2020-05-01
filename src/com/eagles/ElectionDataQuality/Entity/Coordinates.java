package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "COORDINATES", schema = "SUPAUL")
public class Coordinates {
    private String canonicalName;
    private String coords;
    private District districtByCanonicalName;
    private NationalPark nationalParkByCanonicalName;
    private Precinct precinctByCanonicalName;
    private State stateByCanonicalName;

    @Id
    @Column(name = "canonical_name", nullable = false, length = 45)
    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    @Basic
    @Column(name = "coords", nullable = true)
    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(canonicalName, that.canonicalName) &&
                Objects.equals(coords, that.coords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalName, coords);
    }

    @OneToOne(mappedBy = "coordinatesByCanonicalName")
    public District getDistrictByCanonicalName() {
        return districtByCanonicalName;
    }

    public void setDistrictByCanonicalName(District districtByCanonicalName) {
        this.districtByCanonicalName = districtByCanonicalName;
    }

    @OneToOne(mappedBy = "coordinatesByCanonicalName")
    public NationalPark getNationalParkByCanonicalName() {
        return nationalParkByCanonicalName;
    }

    public void setNationalParkByCanonicalName(NationalPark nationalParkByCanonicalName) {
        this.nationalParkByCanonicalName = nationalParkByCanonicalName;
    }

    @OneToOne(mappedBy = "coordinatesByCanonicalName")
    public Precinct getPrecinctByCanonicalName() {
        return precinctByCanonicalName;
    }

    public void setPrecinctByCanonicalName(Precinct precinctByCanonicalName) {
        this.precinctByCanonicalName = precinctByCanonicalName;
    }

    @OneToOne(mappedBy = "coordinatesByCanonicalName")
    public State getStateByCanonicalName() {
        return stateByCanonicalName;
    }

    public void setStateByCanonicalName(State stateByCanonicalName) {
        this.stateByCanonicalName = stateByCanonicalName;
    }
}
