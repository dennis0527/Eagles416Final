package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MAP_COVERAGE_ERRORS", schema = "supaul")
public class MapCoverageErrors {
    private int id;
    private String stateName;
    private String ghostPrecinct;
    private String coords;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "state_name", nullable = true, length = 45)
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Basic
    @Column(name = "ghost_precinct", nullable = true, length = 45)
    public String getGhostPrecinct() {
        return ghostPrecinct;
    }

    public void setGhostPrecinct(String ghostPrecinct) {
        this.ghostPrecinct = ghostPrecinct;
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
        MapCoverageErrors that = (MapCoverageErrors) o;
        return id == that.id &&
                Objects.equals(stateName, that.stateName) &&
                Objects.equals(ghostPrecinct, that.ghostPrecinct) &&
                Objects.equals(coords, that.coords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stateName, ghostPrecinct, coords);
    }
}
