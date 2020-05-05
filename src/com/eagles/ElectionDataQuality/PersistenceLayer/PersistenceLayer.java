package com.eagles.ElectionDataQuality.PersistenceLayer;

import com.eagles.ElectionDataQuality.Entity.AnomalousErrors;
import com.eagles.ElectionDataQuality.Entity.Coordinates;
import com.eagles.ElectionDataQuality.Entity.NationalPark;
import com.eagles.ElectionDataQuality.Entity.Precinct;
import com.eagles.ElectionDataQuality.Entity.State;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.geojson.GeoJsonReader;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import java.io.InputStream;
import java.util.*;
import helpers.MergePrecinctHelpers;

public class PersistenceLayer {
    private static Properties props = new Properties();
    private static String propFileName = "config.properties";
    private static MergePrecinctHelpers mergeHelpers = new MergePrecinctHelpers();



    public static String getStatesJson() {
        try{
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            EntityManager em = getEntityManagerInstance();
            Query query = em.createQuery("Select s from State s order by s.canonicalName asc");
            List<State> states = (List<State>)query.getResultList();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(props.getProperty("skeleton"));
            JSONArray features = (JSONArray) json.get("features");
            for(State s : states){
                features.add(parser.parse(s.getGeojson()));
            }
            return json.toJSONString();
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static String getNeighbors(String stateName, String precinctName) {
        EntityManager em = getEntityManagerInstance();
        try {
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            Query query = em.createQuery("Select p.neighbors from Precinct p where p.canonicalName = \"" +
                    precinctName + "\"" + " and p.canonicalStateName = \"" + props.getProperty(stateName).
                    replaceAll(" ", "") + "\"");
            JSONParser parser = new JSONParser();
            JSONObject object =  (JSONObject) parser.parse((String)query.getSingleResult());
            JSONArray neighbors = (JSONArray) object.get("neighbors");
            return neighbors.toJSONString();

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public static String addNeighbors(String stateName, String precinct1, String precinct2){
        EntityManager em = getEntityManagerInstance();
        Query query1 = em.createQuery("Select p from Precinct p where p.canonicalName = " + "\"" + precinct1 + "\"");
        Precinct p1 = (Precinct) query1.getSingleResult();

        Query query2 = em.createQuery("Select p from Precinct p where p.canonicalName = " + "\"" + precinct2 + "\"");
        Precinct p2 = (Precinct) query2.getSingleResult();

        try{

            JSONParser parser = new JSONParser();
            JSONObject p1JSON =  (JSONObject) parser.parse(p1.getNeighbors());
            JSONArray p1Neighbors = (JSONArray) p1JSON.get("neighbors");
            if (p1Neighbors.toString().contains(precinct2)) {
                return "Error: Already neighbors";
            }
            p1Neighbors.add(p2.getCanonicalName());

            JSONObject p2JSON =  (JSONObject) parser.parse(p2.getNeighbors());
            JSONArray p2Neighbors = (JSONArray) p2JSON.get("neighbors");
            if (p2Neighbors.toString().contains(precinct1)) {
                return "Error: Already neighbors";
            }
            p2Neighbors.add(p1.getCanonicalName());
            em.getTransaction().begin();
            p1.setNeighbors(p1JSON.toJSONString());
            p2.setNeighbors(p2JSON.toJSONString());
            em.flush();
            em.getTransaction().commit();
        }catch(Exception e){
            System.out.print(e);
        }

        return "Success: neighbors were added";
    }

    public static String removeNeighbors(String stateName, String precinct1, String precinct2){
        EntityManager em = getEntityManagerInstance();

        Query query1 = em.createQuery("Select p from Precinct p where p.canonicalName = " + "\"" + precinct1 + "\"");
        Precinct p1 = (Precinct) query1.getSingleResult();

        Query query2 = em.createQuery("Select p from Precinct p where p.canonicalName = " + "\"" + precinct2 + "\"");
        Precinct p2 = (Precinct) query2.getSingleResult();

        try{

            JSONParser parser = new JSONParser();
            JSONObject p1JSON =  (JSONObject) parser.parse(p1.getNeighbors());
            JSONArray p1Neighbors = (JSONArray) p1JSON.get("neighbors");
            if (!p1Neighbors.toString().contains(precinct2)) {
                return "Error: Precincts were not neighbors";
            }
            p1Neighbors.remove(p2.getCanonicalName());

            JSONObject p2JSON =  (JSONObject) parser.parse(p2.getNeighbors());
            JSONArray p2Neighbors = (JSONArray) p2JSON.get("neighbors");
            if (!p2Neighbors.toString().contains(precinct1)) {
                return "Error: Precincts were not neighbors";
            }
            p2Neighbors.remove(p1.getCanonicalName());

            em.getTransaction().begin();

            p1.setNeighbors(p1JSON.toJSONString());
            p2.setNeighbors(p2JSON.toJSONString());

            em.getTransaction().commit();
        }catch(Exception e){
            System.out.println("\n\n" + e.getMessage() + "\n\n");
            return e.getMessage();

        }

        return "Success: Neighbors were added";
    }

    public static String mergePrecincts(String precinct1, String precinct2){
        EntityManager em = getEntityManagerInstance();
        JSONParser parser = new JSONParser();
        GeoJsonReader reader = new GeoJsonReader();

        Query p1Query = em.createQuery("Select p from Precinct p where p.canonicalName = " + "\"" + precinct1 + "\"");
        Precinct p1 = (Precinct) p1Query.getSingleResult();
        String p1Geojson = p1.getGeojson();

        Query p2Query = em.createQuery("Select p from Precinct p where p.canonicalName = " + "\"" + precinct2 + "\"");
        Precinct p2 = (Precinct) p2Query.getSingleResult();
        String p2Geojson = p2.getGeojson();

        //em.joinTransaction();

        try{
            JSONObject p1JSON =  (JSONObject) parser.parse(p1Geojson);
            JSONObject p1Coords = (JSONObject) p1JSON.get("geometry");

            JSONObject p2JSON =  (JSONObject) parser.parse(p2Geojson);
            JSONObject p2Coords = (JSONObject) p2JSON.get("geometry");

            Geometry p1Geom = reader.read(p1Coords.toJSONString());
            Geometry p2Geom = reader.read(p2Coords.toJSONString());

            p1Geom = mergeHelpers.validate(p1Geom);
            p2Geom = mergeHelpers.validate(p2Geom);

            Polygon p1Polygon = new GeometryFactory().createPolygon(p1Geom.getCoordinates());
            Polygon p2Polygon = new GeometryFactory().createPolygon(p2Geom.getCoordinates());

            Geometry union = p1Polygon.union(p2Polygon);
            Coordinate[] unionCoordinates = union.getCoordinates();

            String json = mergeHelpers.createCoordsJson(unionCoordinates);

            System.out.println(json);
        }catch(Exception e){
            System.out.println(e);
        }

        //em.flush();

        return "SUCCESS MERGE";
    }

    public static String getAnomalousErrors(String stateName){
        try {
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            EntityManager em = getEntityManagerInstance();
            Query query = em.createQuery("Select e from AnomalousErrors e WHERE e.stateName = \"" + stateName + "\"");
            List<AnomalousErrors> anomalousErrors = (List<AnomalousErrors>) query.getResultList();
            JSONParser parser = new JSONParser();
            JSONObject skeleton =  (JSONObject) parser.parse(props.getProperty("anomalousErrors"));
            JSONArray anomalousPrecincts = (JSONArray)skeleton.get("anomalousPrecincts");

            for(AnomalousErrors e : anomalousErrors){
                JSONObject individualPrecinct = (JSONObject) parser.parse(props.getProperty("individualAnomalousPrecincts"));
                individualPrecinct.put("errorId", e.getId());
                individualPrecinct.put("errorIdentifier", e.getErrorIdentifier());
                individualPrecinct.put("stateName", e.getStateName());
                individualPrecinct.put("precinctName", e.getPrecinctName());
                Query coordinatesQuery = em.createQuery("Select c from Coordinates c WHERE c.canonicalName = \""
                        + e.getPrecinctName() + "\"");
                Coordinates coords = (Coordinates)coordinatesQuery.getSingleResult();
                JSONObject coordinates = (JSONObject)parser.parse(coords.getCoords());
                coordinates.put("type", coords.getPolygonType());
                individualPrecinct.put("coordinates", coordinates);
                anomalousPrecincts.add(individualPrecinct);
            }

            return skeleton.toJSONString();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String getEnclosedPrecinctErrors(String stateName) {
        EntityManager em = getEntityManagerInstance();
        if (stateName.equalsIgnoreCase("Maryland")) {
            State s = em.find(State.class, "state_MD");
            return s.getEnclosedErrors();
        }
        return "";
    }

    public static String getNationalParks() {
        try {
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            EntityManager em = getEntityManagerInstance();
            Query query = em.createQuery("Select p from NationalPark p order by p.canonicalName asc");
            List<NationalPark> nationalParks = (List<NationalPark>)query.getResultList();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(props.getProperty("skeleton"));
            JSONArray features = (JSONArray) json.get("features");
            for(NationalPark park : nationalParks){
                features.add(parser.parse(park.getGeojson()));
            }
            return json.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPrecinctData(String stateName){

        try {
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            EntityManager entityManager = getEntityManagerInstance();

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static EntityManager getEntityManagerInstance() {
        try {
            ServletContext context = PersistenceContextListener.getApplicationContext();
            EntityManager em = (EntityManager) context.getAttribute("em");
            return em;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}