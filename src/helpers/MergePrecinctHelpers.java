package helpers;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

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
                geom.normalize(); // validate does not pick up rings in the wrong order - this will fix that
                return geom; // If the polygon is valid just return it
            }
            Polygonizer polygonizer = new Polygonizer();
            addPolygon((Polygon)geom, polygonizer);
            return toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
        }else if(geom instanceof MultiPolygon){
            if(geom.isValid()){
                geom.normalize(); // validate does not pick up rings in the wrong order - this will fix that
                return geom; // If the multipolygon is valid just return it
            }
            Polygonizer polygonizer = new Polygonizer();
            for(int n = geom.getNumGeometries(); n-- > 0;){
                addPolygon((Polygon)geom.getGeometryN(n), polygonizer);
            }
            return toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
        }else{
            return geom; // In my case, I only care about polygon / multipolygon geometries
        }
    }

    static void addPolygon(Polygon polygon, Polygonizer polygonizer){
        addLineString(polygon.getExteriorRing(), polygonizer);
        for(int n = polygon.getNumInteriorRing(); n-- > 0;){
            addLineString(polygon.getInteriorRingN(n), polygonizer);
        }
    }

    static void addLineString(LineString lineString, Polygonizer polygonizer){

        if(lineString instanceof LinearRing){ // LinearRings are treated differently to line strings : we need a LineString NOT a LinearRing
            lineString = lineString.getFactory().createLineString(lineString.getCoordinateSequence());
        }

        // unioning the linestring with the point makes any self intersections explicit.
        Point point = lineString.getFactory().createPoint(lineString.getCoordinateN(0));
        Geometry toAdd = lineString.union(point);

        //Add result to polygonizer
        polygonizer.add(toAdd);
    }

    static Geometry toPolygonGeometry(Collection<Polygon> polygons, GeometryFactory factory){
        switch(polygons.size()){
            case 0:
                return null; // No valid polygons!
            case 1:
                return polygons.iterator().next(); // single polygon - no need to wrap
            default:
                //polygons may still overlap! Need to sym difference them
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

        for(int i = 0; i < coords.length-2; i++){
            json = json + "[" + coords[i].x + ", " + coords[i].y + "],";
        }

        json = json + "[" + coords[coords.length-1].x + ", " + coords[coords.length-1].y + "]";

        json = json + "]], \"type\":\"Polygon\"}";

        return json;
    }
}
