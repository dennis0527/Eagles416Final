package com.eagles.ElectionDataQuality.Handler;

import com.eagles.ElectionDataQuality.PersistenceLayer.PersistenceLayer;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.BufferedReader;
import java.io.*;
import java.util.Iterator;
import java.util.Set;

public class DatabaseSimulator {

    public static String getPrecinctJson(String stateName) {
        InputStream fileName = null;
        if (stateName.equals("Maryland")) {
            fileName = DatabaseSimulator.class.getClassLoader().getResourceAsStream("MD_precinct.geojson");
        }
        else if (stateName.equals("Florida")) {
            fileName = DatabaseSimulator.class.getClassLoader().getResourceAsStream("FL_precinct.geojson");
        }
        else if (stateName.equals("New York")) {
            fileName = DatabaseSimulator.class.getClassLoader().getResourceAsStream("NY_precinct.json");
        }


        try {
            JSONParser parser = new JSONParser();
            Reader reader = new InputStreamReader(fileName);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            String result = "";
            result = jsonObject.toString();
            return result;
        } catch (Exception e) {
            System.out.println("\n\n\n\n\n" + e.toString());
            return e.toString();
        }

    }

    public static String getCongressionalDistricts() {
        JSONParser parser = new JSONParser();

        try {
            Reader reader = new InputStreamReader(DatabaseSimulator.class.getClassLoader().getResourceAsStream("congressional_json.json"));
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            return jsonObject.toString();


        } catch (Exception e) {
            System.out.println("\n\n\n\n\n" + e.toString());
            return e.toString();
        }

    }

    public static String getOverlappingPrecinctErrors(String stateName) {
        JSONParser parser = new JSONParser();
        InputStream fileName = null;
        if (stateName.equals("Maryland")) {

        }
        else if (stateName.equals("Florida")) {
            fileName = DatabaseSimulator.class.getClassLoader().getResourceAsStream("fl_overlapping.json");
        }
        else if (stateName.equals("New York")) {
            fileName = DatabaseSimulator.class.getClassLoader().getResourceAsStream("NY_overlapping.json");
        }
        try {
            Reader reader = new InputStreamReader(fileName);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            String result = "";
            result = jsonObject.toString();
            return result;
        } catch (Exception e) {
            System.out.println("\n\n\n\n\n" + e.toString());
            return e.toString();
        }
    }


    public static String getMapCoverageErrors(String stateName) {
        JSONParser parser = new JSONParser();
        InputStream fileName = null;
        if (stateName.equals("Maryland")) {

        }
        else if (stateName.equals("Florida")) {
            fileName = DatabaseSimulator.class.getClassLoader().getResourceAsStream("Florida_map_coverage_errors.json");
        }
        else if (stateName.equals("New York")) {
            fileName = DatabaseSimulator.class.getClassLoader().getResourceAsStream("New York_map_coverage_errors.json");
        }
        try {
            Reader reader = new InputStreamReader(fileName);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            String result = "";
            result = jsonObject.toString();
            return result;
        } catch (Exception e) {
            System.out.println("\n\n\n\n\n" + e.toString());
            return e.toString();
        }
    }
}
