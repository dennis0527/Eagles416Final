package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(schema = "supaul", name = "CORRECTION")
public class Correction {
    private String comment;
    private Time time;
    private Date date;
    private String canonicalPrecinctNames;
    private int id;
    private String errorType;
    private String oldGeojson;
    private String newGeojson;

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
    @Column(name = "canonical_precinct_names", nullable = true, length = 45)
    public String getCanonicalPrecinctNames() {
        return canonicalPrecinctNames;
    }

    public void setCanonicalPrecinctNames(String canonicalPrecinctName) {
        this.canonicalPrecinctNames = canonicalPrecinctName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Correction that = (Correction) o;
        return id == that.id &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(time, that.time) &&
                Objects.equals(date, that.date) &&
                Objects.equals(errorType, that.errorType) &&
                Objects.equals(canonicalPrecinctNames, that.canonicalPrecinctNames);
    }

    @Override
    public String toString() {
        return "Correction{" +
                "comment='" + comment + '\'' +
                ", time=" + time +
                ", date=" + date +
                ", canonicalPrecinctNames='" + canonicalPrecinctNames + '\'' +
                ", id=" + id +
                ", errorType='" + errorType + '\'' +
                ", oldGeojson='" + oldGeojson + '\'' +
                ", newGeojson='" + newGeojson + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, time, date, canonicalPrecinctNames);
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

    @Basic
    @Column(name = "old_geojson", nullable = true)
    public String getOldGeojson() {
        return oldGeojson;
    }

    public void setOldGeojson(String oldGeojson) {
        this.oldGeojson = oldGeojson;
    }

    @Basic
    @Column(name = "new_geojson", nullable = true)
    public String getNewGeojson() {
        return newGeojson;
    }

    public void setNewGeojson(String newGeojson) {
        this.newGeojson = newGeojson;
    }
}
