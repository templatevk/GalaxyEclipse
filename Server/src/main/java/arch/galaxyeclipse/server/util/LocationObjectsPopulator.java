package arch.galaxyeclipse.server.util;


import com.sun.javafx.geom.Point2D;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;


/**
 * Created with IntelliJ IDEA.
 * User: Spaun
 * Date: 06.05.13
 * Time: 23:21
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
public class LocationObjectsPopulator {
    private enum DistanceType{
        FAR,
        MIDDLE,
        CLOSE
    }

    private Properties prop;
    private String script;
    private Map<Integer,DistanceType> distances;
    private String[] far_coords;
    private String[] close_coords;
    private String[] middle_coords;
    private String[] objectNativeId;
    private int location_object_behavior_type_id;
    private int location_object_type_id;
    private int location_id;
    private int max_objects_count;

    public LocationObjectsPopulator(){
        loadPropertiesFile();
        far_coords =
                prop.getProperty("FAR").split(",");
        close_coords =
                prop.getProperty("CLOSE").split(",");
        middle_coords =
                prop.getProperty("MIDDLE").split(",");
        location_object_behavior_type_id =
                Integer.parseInt(prop.getProperty("location_object_behavior_type_id"));
        location_object_type_id =
                Integer.parseInt(prop.getProperty("location_object_type_id"));
        objectNativeId =
                prop.getProperty("object_native_id").split(",");
        location_id =
                Integer.parseInt(prop.getProperty("location_id"));
        max_objects_count =
                Integer.parseInt(prop.getProperty("max_objects_count"));
        distances = new HashMap<>();
        for(int i = 0; i < objectNativeId.length; i++) {
            String distanceValue = prop.getProperty("object_native_id." + objectNativeId[i]);
            distances.put(Integer.parseInt(objectNativeId[i]), DistanceType.valueOf(distanceValue));
        }
    }
    private void loadPropertiesFile() {
        prop = new Properties();
        try {
            prop.load(new FileInputStream("obj_script.properties"));
        }
        catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        }
        catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }


    private Point2D setDistanceXY(int objectDistance) {
        Point2D point = new Point2D();
        float coord_x, coord_y;
        switch (distances.get(objectDistance)){
            case FAR:
                coord_x = Float.parseFloat(far_coords[0]);
                coord_y = Float.parseFloat(far_coords[1]);
                point.x = new Random().nextFloat() * coord_x;
                point.y = new Random().nextFloat() * coord_y;
                break;
            case MIDDLE:
                coord_x = Float.parseFloat(middle_coords[0]);
                coord_y = Float.parseFloat(middle_coords[1]);
                point.x = new Random().nextFloat() * coord_x;
                point.y = new Random().nextFloat() * coord_y;
                break;
            case CLOSE:
                coord_x = Float.parseFloat(close_coords[0]);
                coord_y = Float.parseFloat(close_coords[1]);
                point.x = new Random().nextFloat() * coord_x;
                point.y = new Random().nextFloat() * coord_y;
                break;
        }
        return point;
    }


    private void generateScript() {
        StringBuilder middle;
        String end = "";
        final float MAX_DEGREES = 360f;
        float rotation_angle;
        int rand_native_id;
        Random rand;

        String start = "insert into location_object" +
                        "(\nlocation_object_behavior_type_id,\n" +
                        "location_object_type_id,\n" +
                        "object_native_id,\n" +
                        "rotation_angle,\n" +
                        "position_x,\n" +
                        "position_y,\n" +
                        "location_id\n)\n" +
                        "values\n(";

        for(int i = 0; i < max_objects_count; i++) {
            rand = new Random();
            rotation_angle = rand.nextFloat() * MAX_DEGREES;
            rand_native_id = rand.nextInt(objectNativeId.length);
            middle = new StringBuilder();
            middle.append(location_object_behavior_type_id + "," +
                          location_object_type_id + ",");
            middle.append(objectNativeId[rand_native_id] + ",");
            middle.append(rotation_angle + ",");
            middle.append(setDistanceXY(Integer.parseInt(objectNativeId[rand_native_id])).x + ",");
            middle.append(setDistanceXY(Integer.parseInt(objectNativeId[rand_native_id])).y + ",");
            middle.append(location_id + "),\n(");

            end += middle;
        }
        end = end.substring(0, end.length() - 3);
        script = start + end;
        System.out.println(script);
    }

    public final String getScript() {
        return script;
    }

    public static void main(String args[]) {
        LocationObjectsPopulator obj = new LocationObjectsPopulator();
        obj.generateScript();

        //DBScriptInsert db = new DBScriptInsert();
        //db.executeScript(obj.getScript());
    }
}
