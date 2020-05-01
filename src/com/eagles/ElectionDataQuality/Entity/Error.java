package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "supaul", name = "ERROR")
public class Error {
    private int errorId;
    private String errorType;
    private String canonicalPrecinctName;
    private Correction correctionByErrorId;
    private Precinct precinctByCanonicalPrecinctName;

    @Id
    @Column(name = "error_id", nullable = false)
    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    @Basic
    @Column(name = "error_type", nullable = true)
    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    @Basic
    @Column(name = "canonical_precinct_name", nullable = true, length = 45)
    public String getCanonicalPrecinctName() {
        return canonicalPrecinctName;
    }

    public void setCanonicalPrecinctName(String canonicalPrecinctName) {
        this.canonicalPrecinctName = canonicalPrecinctName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error error = (Error) o;
        return errorId == error.errorId &&
                Objects.equals(errorType, error.errorType) &&
                Objects.equals(canonicalPrecinctName, error.canonicalPrecinctName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorId, errorType, canonicalPrecinctName);
    }

    @OneToOne(mappedBy = "errorByErrorId")
    public Correction getCorrectionByErrorId() {
        return correctionByErrorId;
    }

    public void setCorrectionByErrorId(Correction correctionByErrorId) {
        this.correctionByErrorId = correctionByErrorId;
    }

    @ManyToOne
    @JoinColumn(name = "canonical_precinct_name", insertable=false, updatable=false, referencedColumnName = "canonical_name")
    public Precinct getPrecinctByCanonicalPrecinctName() {
        return precinctByCanonicalPrecinctName;
    }

    public void setPrecinctByCanonicalPrecinctName(Precinct precinctByCanonicalPrecinctName) {
        this.precinctByCanonicalPrecinctName = precinctByCanonicalPrecinctName;
    }
}
