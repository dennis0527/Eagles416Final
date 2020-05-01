package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ELECTION_DATA", schema = "supaul")
public class ElectionData {
    private String canonicalAreaName;
    private String electionType;
    private Double democratVote;
    private Double republicanVote;
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
    @Column(name = "election_type", nullable = true)
    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
        this.electionType = electionType;
    }

    @Basic
    @Column(name = "democrat_vote", nullable = true, precision = 0)
    public Double getDemocratVote() {
        return democratVote;
    }

    public void setDemocratVote(Double democratVote) {
        this.democratVote = democratVote;
    }

    @Basic
    @Column(name = "republican_vote", nullable = true, precision = 0)
    public Double getRepublicanVote() {
        return republicanVote;
    }

    public void setRepublicanVote(Double republicanVote) {
        this.republicanVote = republicanVote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectionData that = (ElectionData) o;
        return Objects.equals(canonicalAreaName, that.canonicalAreaName) &&
                Objects.equals(electionType, that.electionType) &&
                Objects.equals(democratVote, that.democratVote) &&
                Objects.equals(republicanVote, that.republicanVote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalAreaName, electionType, democratVote, republicanVote);
    }

    @OneToOne
    @JoinColumn(name = "canonical_area_name", referencedColumnName = "canonical_name", nullable = false, insertable = false, updatable = false)
    public District getDistrictByCanonicalAreaName() {
        return districtByCanonicalAreaName;
    }

    public void setDistrictByCanonicalAreaName(District districtByCanonicalAreaName) {
        this.districtByCanonicalAreaName = districtByCanonicalAreaName;
    }

    @OneToOne
    @JoinColumn(name = "canonical_area_name", referencedColumnName = "canonical_name", nullable = false, insertable = false, updatable = false)
    public Precinct getPrecinctByCanonicalAreaName() {
        return precinctByCanonicalAreaName;
    }

    public void setPrecinctByCanonicalAreaName(Precinct precinctByCanonicalAreaName) {
        this.precinctByCanonicalAreaName = precinctByCanonicalAreaName;
    }
}
