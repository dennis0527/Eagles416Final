package com.eagles.ElectionDataQuality.Entity;

import javax.persistence.*;

@Entity
@Table(schema = "supaul", name = "GHOST_PRECINCTS")
public class GhostPrecinct {
    private int id;
    private String stateName;
    private String coords;
    private String blank_geojson;

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
    @Column(name = "coords", nullable = true)
    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    @Basic
    @Column(name = "blank_geojson", nullable = true)
    public String getBlankGeojson() {
        return blank_geojson;
    }

    public void setBlankGeojson(String blank_geojson) {
        this.blank_geojson = blank_geojson;
    }
}
