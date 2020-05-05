package com.eagles.ElectionDataQuality.Handler;

import com.eagles.ElectionDataQuality.PersistenceLayer.PersistenceLayer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


// The Java class will be hosted at the URI path "/app"
@Path("/app")
public class AppHandler {

    public AppHandler() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getState() {
        return PersistenceLayer.getStatesJson();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/Precinct/{stateName}")
    public String getPrecinctData(@PathParam("stateName") String name) {
        return DatabaseSimulator.getPrecinctJson(name.replaceAll("%20", " "));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/Districts")
    public String getDistricts(){
        return DatabaseSimulator.getCongressionalDistricts();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{stateName}/{precinctName}/neighbors")
    public String getNeighbors(@PathParam("stateName") String state, @PathParam("precinctName") String precinct){
        return PersistenceLayer.getNeighbors(state, precinct.replaceAll("%20", " "));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("AnomalousError/{stateName}")
    public String getAnomalousErrors(@PathParam("stateName") String state) {
        return PersistenceLayer.getAnomalousErrors(state);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("EnclosedPrecinctError/{stateName}")
    public String getEnclosedPrecinctErrors(@PathParam("stateName") String state) {
        return PersistenceLayer.getEnclosedPrecinctErrors(state);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("OverlappingPrecinctError/{stateName}")
    public String getOverlapPrecinctErrors(@PathParam("stateName") String state) {
        return DatabaseSimulator.getOverlappingPrecinctErrors(state);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("AddNeighbors/{stateName}/{precinctName1}/{precinctName2}")
    public String addPrecinctNeighbors(@PathParam("precinctName1") String precinctName1, @PathParam("precinctName2") String precinctName2,
                                       @PathParam("stateName") String state) {
        return PersistenceLayer.addNeighbors(state, precinctName1, precinctName2);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("RemoveNeighbors/{stateName}/{precinctName1}/{precinctName2}")
    public String removePrecinctNeighbors(@PathParam("precinctName1") String precinctName1, @PathParam("precinctName2") String precinctName2,
                                       @PathParam("stateName") String state) {
        return PersistenceLayer.removeNeighbors(state.replaceAll("%20", " "), precinctName1.replaceAll("%20", " "), precinctName2.replaceAll("%20", " "));
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("MergePrecincts/{precinctName1}/{precinctName2}")
    public String mergePrecincts(@PathParam("precinctName1") String precinctName1, @PathParam("precinctName2") String precinctName2) {
        return PersistenceLayer.mergePrecincts(precinctName1, precinctName2);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("NationalParks")
    public String getNationalParks(){
        return PersistenceLayer.getNationalParks();
    }

}