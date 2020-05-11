package com.eagles.ElectionDataQuality.PersistenceLayer;

import com.eagles.ElectionDataQuality.Entity.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.geojson.GeoJsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import java.io.InputStream;
import java.sql.Time;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.eagles.ElectionDataQuality.Helpers.MergePrecinctHelpers;

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
        stateName = stateName.replaceAll(" " , "");
        EntityManager em = getEntityManagerInstance();
        try {
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            Query query = em.createQuery("Select p.neighbors from Precinct p where p.canonicalName = \"" +
                    precinctName + "\"" + " and p.canonicalStateName = \"" + props.getProperty(stateName) + "\"");
            JSONParser parser = new JSONParser();
            JSONObject object =  (JSONObject) parser.parse((String)query.getSingleResult());
            JSONArray neighbors = (JSONArray) object.get("neighbors");
            return neighbors.toJSONString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String addNeighbors(String stateName, String precinct1, String precinct2){
        stateName = stateName.replaceAll(" " , "");
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
            String[] precinctNames = new String[2];
            precinctNames[0] = precinct1;
            precinctNames[1] = precinct2;
            generateCorrectionData("addNeighbors", precinctNames, null, null);
        }catch(Exception e){
            EntityTransaction tx = em.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
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
            em.flush();
            em.getTransaction().commit();
            String[] precinctNames = new String[2];
            precinctNames[0] = precinct1;
            precinctNames[1] = precinct2;
            generateCorrectionData("removeNeighbors", precinctNames, null, null);
        }catch(Exception e){
            EntityTransaction tx = em.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("\n\n" + e.getMessage() + "\n\n");
            return e.getMessage();

        }

        return "Success: Neighbors were removed";
    }

    public static String mergePrecincts(String precinct1, String precinct2){
        EntityManager em = getEntityManagerInstance();

        //removeNeighbors("Maryland", precinct1, precinct2);

        JSONParser parser = new JSONParser();
        GeoJsonReader reader = new GeoJsonReader();

        Query p1Query = em.createQuery("Select p from Precinct p where p.canonicalName = " + "\"" + precinct1 + "\"");
        Precinct p1 = (Precinct) p1Query.getSingleResult();
        String p1Geojson = p1.getGeojson();

        System.out.println(p1Geojson);

        Query p2Query = em.createQuery("Select p from Precinct p where p.canonicalName = " + "\"" + precinct2 + "\"");
        Precinct p2 = (Precinct) p2Query.getSingleResult();
        String p2Geojson = p2.getGeojson();

        System.out.println(p2Geojson);

        em.getTransaction().begin();

        try{
            JSONObject p1JSON =  (JSONObject) parser.parse(p1Geojson);
            JSONObject p1Coords = (JSONObject) p1JSON.get("geometry");

            JSONObject p2JSON =  (JSONObject) parser.parse(p2Geojson);
            JSONObject p2Coords = (JSONObject) p2JSON.get("geometry");

            System.out.println(p1Coords.toJSONString());

            Geometry p1Geom = reader.read(p1Coords.toJSONString());
            Geometry p2Geom = reader.read(p2Coords.toJSONString());

            p1Geom = mergeHelpers.validate(p1Geom);
            p2Geom = mergeHelpers.validate(p2Geom);

            Polygon p1Polygon = new GeometryFactory().createPolygon(p1Geom.getCoordinates());
            Polygon p2Polygon = new GeometryFactory().createPolygon(p2Geom.getCoordinates());

            Geometry union = p1Polygon.union(p2Polygon);
            Coordinate[] unionCoordinates = union.getCoordinates();

            String json = mergeHelpers.createCoordsJson(unionCoordinates);

            System.out.println("Precinct 1 GeoJSON BEFORE: " + p1JSON.toJSONString());

            JSONObject coordsJson = (JSONObject) parser.parse(json);

            System.out.println("New coordinates: " + coordsJson.toJSONString());

            p1JSON.remove("geometry");
            p1JSON.put("geometry", coordsJson);

            JSONObject p1Props = (JSONObject) p1JSON.get("properties");
            JSONObject p2Props = (JSONObject) p2JSON.get("properties");

            mergeHelpers.editProperties(p1Props, p2Props);

            System.out.println("New JSON: " + p1JSON.toJSONString());

            p1.setGeojson(p1JSON.toJSONString());

            System.out.println("Precinct 1 GeoJSON AFTER: " + p1.getGeojson());

            Query deleteQuery = em.createQuery("Delete from Precinct p where p.canonicalName = " + "\"" + precinct2 + "\"");
            deleteQuery.executeUpdate();
            String[] precinctNames = new String[2];
            precinctNames[0] = precinct1;
            precinctNames[1] = precinct2;
//            generateCorrectionData("enclosed", precinctNames, null, null);

        }catch(Exception e){
            EntityTransaction tx = em.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println(e);
            return "Error Merge";
        }

        em.flush();

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
                Coordinates coordinatesQuerySingleResult = (Coordinates)coordinatesQuery.getSingleResult();
                JSONObject coordinates = (JSONObject)parser.parse(coordinatesQuerySingleResult.getCoords());
                coordinates.put("type", coordinatesQuerySingleResult.getPolygonType());
                individualPrecinct.put("coordinates", coordinates);
                anomalousPrecincts.add(individualPrecinct);
            }
            return skeleton.toJSONString();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String getEnclosedPrecinctErrors(String stateName) {
        try {
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            EntityManager em = getEntityManagerInstance();
            Query query = em.createQuery("Select e from EnclosedErrors e where e.stateName = :stateName");
            query.setParameter("stateName", stateName);
            List<EnclosedErrors> errors = (List<EnclosedErrors>) query.getResultList();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(props.getProperty("enclosedErrors"));
            JSONArray enclosedErrors = (JSONArray)json.get("enclosedPrecincts");

            for(EnclosedErrors e : errors){
                JSONObject singleError = (JSONObject) parser.parse(props.getProperty("individualEnclosingPrecincts"));
                singleError.put("stateName", e.getStateName());
                singleError.put("enclosingPrecinct", e.getEnclosingPrecinct());
                singleError.put("enclosedPrecinct", e.getEnclosedPrecinct());
                JSONObject coordinatesSingleError = (JSONObject) singleError.get("coordinates");
                Query coordinatesQuery = em.createQuery("Select c from Coordinates c where c.canonicalName = :enclosed");
                coordinatesQuery.setParameter("enclosed", e.getEnclosedPrecinct());
                Coordinates coords = (Coordinates) coordinatesQuery.getSingleResult();
                JSONObject coordinates = (JSONObject) parser.parse(coords.getCoords());
                JSONArray coordinatesArray = (JSONArray) coordinates.get("coordinates");
                coordinatesSingleError.put("type", coords.getPolygonType());
                coordinatesSingleError.put("coordinates", coordinatesArray);
                enclosedErrors.add(singleError);
            }

            return json.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public static String addGhostPrecinct(String stateName, String ghostPrecinctString){
        EntityManager em = getEntityManagerInstance();

        ghostPrecinctString = ghostPrecinctString.replace("Ghost Precinct ", "");
        int ghostPrecinct = Integer.parseInt(ghostPrecinctString);

        System.out.println(ghostPrecinct);

        JSONParser parser = new JSONParser();
        GeoJsonReader reader = new GeoJsonReader();

        Query gQuery = em.createQuery("Select g from GhostPrecinct g where g.id = " + ghostPrecinct + "");
        GhostPrecinct g = (GhostPrecinct) gQuery.getSingleResult();

        String gCoordsString = g.getCoords();

        System.out.println(gCoordsString);

        try{
            JSONObject gJSON =  (JSONObject) parser.parse(gCoordsString);

            Geometry gGeom = reader.read(gJSON.toJSONString());

            gGeom = mergeHelpers.validate(gGeom);

            Polygon gPolygon = new GeometryFactory().createPolygon(gGeom.getCoordinates());

            String json = mergeHelpers.createCoordsJson(gPolygon.getCoordinates());

            JSONObject coordsJson = (JSONObject) parser.parse(json);

            JSONObject blankGeoJson =  (JSONObject) parser.parse(g.getBlankGeojson());
            blankGeoJson.remove("geometry");
            blankGeoJson.put("geometry", coordsJson);

            JSONObject gProps = (JSONObject) blankGeoJson.get("properties");
            gProps.remove("STATE");
            gProps.put("STATE", stateName);
            gProps.remove("CANON_NAME");
            gProps.put("CANON_NAME", "GhostPrecinct" + g.getId());


            em.getTransaction().begin();
            Coordinates coordinates = new Coordinates();
            coordinates.setCanonicalName("GhostPrecinct" + g.getId());
            coordinates.setCoords(gCoordsString);
            coordinates.setPolygonType("Polygon");
            em.persist(coordinates);

            Precinct precinct = new Precinct();
            precinct.setCanonicalName("GhostPrecinct" + g.getId());
            precinct.setFullName("" + g.getId());
            precinct.setNeighbors("{\"neighbors\": []}");
            precinct.setCanonicalStateName(props.getProperty(stateName));
            precinct.setGeojson(blankGeoJson.toJSONString());
            em.persist(precinct);

        }catch(Exception e){
            System.out.println(e);
            return "Error";
        }

        em.flush();

        return "SUCCESS MERGE";
    }

    public static String getOverlappingPrecinctErrors(String stateName){
        try {
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            EntityManager em = getEntityManagerInstance();
            Query query = em.createQuery("Select o from OverlappingErrors o where o.stateName = :stateName");
            query.setParameter("stateName", stateName);
            List<OverlappingErrors> errors = query.getResultList();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(props.getProperty("skeleton"));
            JSONArray features = (JSONArray) json.get("features");

            for(OverlappingErrors e : errors){
                JSONObject singleError = (JSONObject) parser.parse(props.getProperty("individualOverlappingErrors"));
                JSONObject properties = (JSONObject) singleError.get("properties");
                properties.put("CANON_NAME_1", e.getPrecinctName());
                properties.put("CANON_NAME_2", e.getOverlappingPrecinct());
                singleError.put("geometry", parser.parse(e.getGeometryJson()));
                features.add(singleError);

            }
            return json.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public static String getMapCoverageErrors(String stateName){
        try {
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            EntityManager em = getEntityManagerInstance();
            Query query = em.createQuery("Select e from MapCoverageErrors e where e.stateName = :stateName");
            query.setParameter("stateName", stateName);
            List<MapCoverageErrors> errors = (List<MapCoverageErrors>) query.getResultList();
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(props.getProperty("mapCoverageErrors"));
            JSONArray features = (JSONArray) object.get("mapCoverageErrors");

            for(MapCoverageErrors e : errors){
                JSONObject singleError = (JSONObject) parser.parse(props.getProperty("individualCoverageErrors"));
                singleError.put("stateName", e.getStateName());
                singleError.put("ghostPrecinct", e.getGhostPrecinct());
                JSONObject coordinates = (JSONObject) singleError.get("coordinates");
                JSONObject mapCoverageCoordinates = (JSONObject) parser.parse(e.getCoords());
                coordinates.put("type", mapCoverageCoordinates.get("type"));
                coordinates.put("coordinates", mapCoverageCoordinates.get("coordinates"));
                features.add(singleError);
            }
            return object.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String getCongressionalDistricts(){
        try {
            EntityManager em = getEntityManagerInstance();
            return (em.find(District.class, "all")).getGeojson();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

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

    public static String getPrecinctsData(String stateName){

        try {
            InputStream is = PersistenceLayer.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(is);
            EntityManager em = getEntityManagerInstance();
            String state = props.getProperty(stateName.replaceAll(" ", ""));
            Query query = em.createQuery("Select p.geojson from Precinct p where p.canonicalStateName = \"" + state + "\"");
            List<String> precincts = (List<String>) query.getResultList();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(props.getProperty("skeleton"));
            JSONArray features = (JSONArray) json.get("features");
            for(String geojson : precincts){
                features.add(parser.parse(geojson));
            }
            return json.toJSONString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String editPrecinctBoundaries(String precinctName, String coordinatesStr){
        EntityManager em = getEntityManagerInstance();
        try {
            Precinct precinct = em.find(Precinct.class, precinctName);
            JSONParser parser = new JSONParser();
            JSONObject geoJson = (JSONObject) parser.parse(precinct.getGeojson());
            em.getTransaction().begin();
            JSONObject geometry = (JSONObject)geoJson.get("geometry");
            System.out.println("PRECINCT GEOJSON BEFORE= " + geoJson.toJSONString());
            String before = geoJson.toJSONString();
            System.out.println();
            geometry.remove("coordinates");
            geometry.put("coordinates", parser.parse(coordinatesStr));
            geoJson.remove("geometry");
            geoJson.put("geometry", geometry);
            System.out.println("PRECINCT GEOJSON AFTER CHANGING JSONOBJECT= " + geoJson.toJSONString());
            String after = geoJson.toJSONString();
            System.out.println();
            precinct.setGeojson(geoJson.toJSONString());
            System.out.println("AFTER SETTING= " + precinct.getGeojson());
            System.out.println();
            em.flush();
            em.getTransaction().commit();
            String[] precinctNames = new String[1];
            precinctNames[0] = precinctName;
            generateCorrectionData("editPrecinctBoundary", precinctNames, before, after);

            return "SUCCESS";

        } catch (Exception e) {
            EntityTransaction tx = em.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println(e.getMessage());
            return e.getMessage();
        }

    }

    public static String getPrecinctCoordinates(String precinctName){
        try {
            EntityManager em = getEntityManagerInstance();
            Query query = em.createQuery("Select p.geojson from Precinct p where p.canonicalName = \"" + precinctName + "\"");
            JSONParser parser = new JSONParser();
            JSONObject geoJson = (JSONObject) parser.parse((String) query.getSingleResult());
            JSONObject geometry = (JSONObject) geoJson.get("geometry");
            JSONArray coordinates = (JSONArray) geometry.get("coordinates");
            return ((JSONObject) (parser.parse("{\"coordinates\" :" + coordinates.toJSONString() + "}"))).toJSONString();

        } catch (Exception e) {
            return e.getMessage();
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

    public static String generateCorrectionData(String datatype, String [] precinctNames, String oldGeojson, String newGeojson) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            System.out.println("The system date and time are " + format.format(cal.getTime()));
            String comment = "";
            String errorType = "";
            switch (datatype) {
                case "enclosed":
                    comment = precinctNames[0] + " was merged with " + precinctNames[1];
                    errorType = "ENCLOSED";
                    break;
                case "overlapping":
                    comment = precinctNames[0] + " had its boundary edited";
                    errorType = "OVERLAPPING";
                    break;
                case "addNeighbors":
                    comment = precinctNames[0] + " and " + precinctNames[1] + " were added as neigbhors";
                    errorType = "NEIGHBORS";
                    break;
                case "removeNeighbors":
                    comment = precinctNames[0] + " and " + precinctNames[1] + " were removed as neighbors";
                    errorType = "NEIGHBORS";
                    break;
                case "defineGhostPrecinct":
                    comment = precinctNames[0] + " was defined as a ghost precinct";
                    errorType = "MAP_COVERAGE";
                    break;
                case "editPrecinctBoundary":
                    comment = precinctNames[0] + " had its boundary edited";
                    errorType = "MAP_COVERAGE";
                    break;
                default:
                    break;
            }
            String result = format.format(cal.getTime());
            String[] dateAndTime = format.format(cal.getTime()).split(" ");
            String date = dateAndTime[0];
            String time = dateAndTime[1];

            updateCorrections(precinctNames, date, time, comment, oldGeojson, newGeojson, errorType);
            return "Success";
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public static String updateCorrections(String[] precinctNames, String date,
                                           String time, String comment, String oldGeojson, String newGeoJson,
                                           String errorType){

        EntityManager em = getEntityManagerInstance();
        try {
            em.getTransaction().begin();
            Correction correction = new Correction();
            correction.setTime(Time.valueOf(time));
            correction.setDate(Date.valueOf(date));
            String pNames = "";
            for(int i = 0; i < precinctNames.length; i++){
                if(i == 1){
                    pNames = pNames + ", " + precinctNames[i];
                }
                else{
                    pNames = precinctNames[i];
                }
            }
            correction.setCanonicalPrecinctNames(pNames);
            correction.setComment(comment);
            correction.setOldGeojson(oldGeojson);
            correction.setNewGeojson(newGeoJson);
            correction.setErrorType(errorType);

            System.out.println(correction.toString());
            em.persist(correction);
            em.flush();
            em.getTransaction().commit();

            return "SUCCESS";

        } catch (Exception e) {
            EntityTransaction tx = em.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return e.getMessage();
        }
    }


}