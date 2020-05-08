package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(schema = "supaul", name = "CORRECTION")
public class Correction {
    private int errorId;
    private String comment;
    private Time time;
    private Date date;
    private String canonicalPrecinctName;
    private Precinct precinctByCanonicalPrecinctName;
    private int id;
    private String errorType;

    @Basic
    @Column(name = "comment", nullable = true, length = 255)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "time", nullable = true)
    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Basic
    @Column(name = "date", nullable = true)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        Correction that = (Correction) o;
        return errorId == that.errorId &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(time, that.time) &&
                Objects.equals(date, that.date) &&
                Objects.equals(canonicalPrecinctName, that.canonicalPrecinctName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorId, comment, time, date, canonicalPrecinctName);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canonical_precinct_name", referencedColumnName = "canonical_name", insertable=false, updatable=false)
    public Precinct getPrecinctByCanonicalPrecinctName() {
        return precinctByCanonicalPrecinctName;
    }

    public void setPrecinctByCanonicalPrecinctName(Precinct precinctByCanonicalPrecinctName) {
        this.precinctByCanonicalPrecinctName = precinctByCanonicalPrecinctName;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "error_type", nullable = true)
    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
}
