package helpers;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.Iterator;

public class MergePrecinctHelpers {
    //TODO: remember might need to do something different for Polygon vs. MultiPolygon

    //assumes polygon
    public static Geometry validate(Geometry geom){
        if(geom.isValid()){
            geom.normalize(); // validate does not pick up rings in the wrong order - this will fix that
        }
        Polygonizer polygonizer = new Polygonizer();
        MergePrecinctHelpers.addPolygon((Polygon)geom, polygonizer);
        geom = toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
        return geom;
    }

    //different for polygon or multi-polygon
    public static Geometry validate2(Geometry geom){
        if(geom instanceof Polygon){
            if(geom.isValid()){
                geom.normalize();
                return geom;
            }
            Polygonizer polygonizer = new Polygonizer();
            addPolygon((Polygon)geom, polygonizer);
            return toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
        }else if(geom instanceof MultiPolygon){
            if(geom.isValid()){
                geom.normalize();
                return geom;
            }
            Polygonizer polygonizer = new Polygonizer();
            for(int n = geom.getNumGeometries(); n-- > 0;){
                addPolygon((Polygon)geom.getGeometryN(n), polygonizer);
            }
            return toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
        }else{
            return geom;
        }
    }

    static void addPolygon(Polygon polygon, Polygonizer polygonizer){
        addLineString(polygon.getExteriorRing(), polygonizer);
        for(int n = polygon.getNumInteriorRing(); n-- > 0;){
            addLineString(polygon.getInteriorRingN(n), polygonizer);
        }
    }

    static void addLineString(LineString lineString, Polygonizer polygonizer){
        if(lineString instanceof LinearRing){
            lineString = lineString.getFactory().createLineString(lineString.getCoordinateSequence());
        }

        Point point = lineString.getFactory().createPoint(lineString.getCoordinateN(0));
        Geometry toAdd = lineString.union(point);

        polygonizer.add(toAdd);
    }

    static Geometry toPolygonGeometry(Collection<Polygon> polygons, GeometryFactory factory){
        switch(polygons.size()){
            case 0:
                return null;
            case 1:
                return polygons.iterator().next();
            default:
                Iterator<Polygon> iter = polygons.iterator();
                Geometry ret = iter.next();
                while(iter.hasNext()){
                    ret = ret.symDifference(iter.next());
                }
                return ret;
        }
    }

    public static String createCoordsJson(Coordinate[] coords){
        String json = "{\"coordinates\": [[";
        //String json = "[[";

        for(int i = 0; i < coords.length-2; i++){
            json = json + "[" + coords[i].x + ", " + coords[i].y + "],";
        }

        json = json + "[" + coords[coords.length-1].x + ", " + coords[coords.length-1].y + "]";

        json = json + "]], \"type\":\"Polygon\"}";

        //json = json + "]]";

        return json;
    }

    public void editProperties(JSONObject p1Props, JSONObject p2Props) {
        long val = 0;

        val = (long) p1Props.get("POP") + (long) p2Props.get("POP");
        p1Props.remove("POP");
        p1Props.put("POP", val);

        val = (long) p1Props.get("HISP") + (long) p2Props.get("HISP");
        p1Props.remove("HISP");
        p1Props.put("HISP", val);

        val = (long) p1Props.get("ASIAN") + (long) p2Props.get("ASIAN");
        p1Props.remove("ASIAN");
        p1Props.put("ASIAN", val);

        val = (long) p1Props.get("BLACK") + (long) p2Props.get("BLACK");
        p1Props.remove("BLACK");
        p1Props.put("BLACK", val);

        val = (long) p1Props.get("WHITE") + (long) p2Props.get("WHITE");
        p1Props.remove("WHITE");
        p1Props.put("WHITE", val);

        val = (long) p1Props.get("NAT_AMER") + (long) p2Props.get("NAT_AMER");
        p1Props.remove("NAT_AMER");
        p1Props.put("NAT_AMER", val);

        val = (long) p1Props.get("USH16D") + (long) p2Props.get("USH16D");
        p1Props.remove("USH16D");
        p1Props.put("USH16D", val);

        val = (long) p1Props.get("USH16R") + (long) p2Props.get("USH16R");
        p1Props.remove("USH16R");
        p1Props.put("USH16R", val);

        val = (long) p1Props.get("USH18D") + (long) p2Props.get("USH18D");
        p1Props.remove("USH18D");
        p1Props.put("USH18D", val);

        val = (long) p1Props.get("USH18R") + (long) p2Props.get("USH18R");
        p1Props.remove("USH18R");
        p1Props.put("USH18R", val);

        val = (long) p1Props.get("PRES16D") + (long) p2Props.get("PRES16D");
        p1Props.remove("PRES16D");
        p1Props.put("PRES16D", val);

        val = (long) p1Props.get("PRES16R") + (long) p2Props.get("PRES16R");
        p1Props.remove("PRES16R");
        p1Props.put("PRES16R", val);
    }
}
