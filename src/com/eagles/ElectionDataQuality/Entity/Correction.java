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
    private Error errorByErrorId;
    private Precinct precinctByCanonicalPrecinctName;

    @Id
    @Column(name = "errorID", nullable = false)
    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

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

    @OneToOne
    @JoinColumn(name = "errorID", referencedColumnName = "error_id", nullable = false, insertable=false, updatable=false)
    public Error getErrorByErrorId() {
        return errorByErrorId;
    }

    public void setErrorByErrorId(Error errorByErrorId) {
        this.errorByErrorId = errorByErrorId;
    }

    @ManyToOne
    @JoinColumn(name = "canonical_precinct_name", referencedColumnName = "canonical_name", insertable=false, updatable=false)
    public Precinct getPrecinctByCanonicalPrecinctName() {
        return precinctByCanonicalPrecinctName;
    }

    public void setPrecinctByCanonicalPrecinctName(Precinct precinctByCanonicalPrecinctName) {
        this.precinctByCanonicalPrecinctName = precinctByCanonicalPrecinctName;
    }
}
