package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "supaul", name = "DEMOGRAPHICS")
public class Demographics {
    private String canonicalAreaName;
    private Long totalPopulation;
    private Double whitePercent;
    private Double blackPercent;
    private Double hispanicPercent;
    private Double asianPercent;
    private Double nativeAmericanPercent;
    private State stateByCanonicalAreaName;
    private District districtByCanonicalAreaName;
    private Precinct precinctByCanonicalAreaName;

    @Id
    @Column(name = "canonical_area_name", nullable = false, length = 45)
    public String getCanonicalAreaName() {
        return canonicalAreaName;
    }

    public void setCanonicalAreaName(String canonicalAreaName) {
        this.canonicalAreaName = canonicalAreaName;
    }

    @Basic
    @Column(name = "total_population", nullable = true)
    public Long getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(Long totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    @Basic
    @Column(name = "white_percent", nullable = true, precision = 0)
    public Double getWhitePercent() {
        return whitePercent;
    }

    public void setWhitePercent(Double whitePercent) {
        this.whitePercent = whitePercent;
    }

    @Basic
    @Column(name = "black_percent", nullable = true, precision = 0)
    public Double getBlackPercent() {
        return blackPercent;
    }

    public void setBlackPercent(Double blackPercent) {
        this.blackPercent = blackPercent;
    }

    @Basic
    @Column(name = "hispanic_percent", nullable = true, precision = 0)
    public Double getHispanicPercent() {
        return hispanicPercent;
    }

    public void setHispanicPercent(Double hispanicPercent) {
        this.hispanicPercent = hispanicPercent;
    }

    @Basic
    @Column(name = "asian_percent", nullable = true, precision = 0)
    public Double getAsianPercent() {
        return asianPercent;
    }

    public void setAsianPercent(Double asianPercent) {
        this.asianPercent = asianPercent;
    }

    @Basic
    @Column(name = "native_american_percent", nullable = true, precision = 0)
    public Double getNativeAmericanPercent() {
        return nativeAmericanPercent;
    }

    public void setNativeAmericanPercent(Double nativeAmericanPercent) {
        this.nativeAmericanPercent = nativeAmericanPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Demographics that = (Demographics) o;
        return Objects.equals(canonicalAreaName, that.canonicalAreaName) &&
                Objects.equals(totalPopulation, that.totalPopulation) &&
                Objects.equals(whitePercent, that.whitePercent) &&
                Objects.equals(blackPercent, that.blackPercent) &&
                Objects.equals(hispanicPercent, that.hispanicPercent) &&
                Objects.equals(asianPercent, that.asianPercent) &&
                Objects.equals(nativeAmericanPercent, that.nativeAmericanPercent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalAreaName, totalPopulation, whitePercent, blackPercent, hispanicPercent, asianPercent, nativeAmericanPercent);
    }

    @OneToOne
    @JoinColumn(name = "canonical_area_name", referencedColumnName = "canonical_name", nullable = false, insertable=false, updatable=false)
    public State getStateByCanonicalAreaName() {
        return stateByCanonicalAreaName;
    }

    public void setStateByCanonicalAreaName(State stateByCanonicalAreaName) {
        this.stateByCanonicalAreaName = stateByCanonicalAreaName;
    }

    @OneToOne
    @JoinColumn(name = "canonical_area_name", referencedColumnName = "canonical_name", nullable = false, insertable=false, updatable=false)
    public District getDistrictByCanonicalAreaName() {
        return districtByCanonicalAreaName;
    }

    public void setDistrictByCanonicalAreaName(District districtByCanonicalAreaName) {
        this.districtByCanonicalAreaName = districtByCanonicalAreaName;
    }

    @OneToOne
    @JoinColumn(name = "canonical_area_name", referencedColumnName = "canonical_name", nullable = false, insertable=false, updatable=false)
    public Precinct getPrecinctByCanonicalAreaName() {
        return precinctByCanonicalAreaName;
    }

    public void setPrecinctByCanonicalAreaName(Precinct precinctByCanonicalAreaName) {
        this.precinctByCanonicalAreaName = precinctByCanonicalAreaName;
    }
}
