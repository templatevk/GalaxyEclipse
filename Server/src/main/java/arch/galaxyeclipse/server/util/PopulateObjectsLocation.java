package arch.galaxyeclipse.server.util;


import com.sun.javafx.geom.Point2D;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;


/**
 * Created with IntelliJ IDEA.
 * User: Spaun
 * Date: 06.05.13
 * Time: 23:21
 * To change this template use File | Settings | File Templates.
 */
public class PopulateObjectsLocation {
    private enum Distance{
        FAR,
        MIDDLE,
        CLOSE
    }

    private Properties prop;
    private String script;
    private final String[] FAR_COORDS;
    private final String[] CLOSE_COORDS;
    private final String[] MIDDLE_COORDS;
    private final String[] OBJECT_NATIVE_ID;
    private final String LOCATION_OBJECT_BEHAVIOR_TYPE_ID;
    private final String LOCATION_OBJECT_TYPE_ID;
    private final String LOCATION_ID;
    private final int MAX_OBJECTS_COUNT;

    public PopulateObjectsLocation(){
        prop = new Properties();

        try {
            prop.load(new FileInputStream("objScriptProp.properties"));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        FAR_COORDS =
                prop.getProperty("FAR").split(",");
        CLOSE_COORDS =
                prop.getProperty("CLOSE").split(",");
        MIDDLE_COORDS =
                prop.getProperty("MIDDLE").split(",");
        LOCATION_OBJECT_BEHAVIOR_TYPE_ID =
                prop.getProperty("location_object_behavior_type_id");
        LOCATION_OBJECT_TYPE_ID =
                prop.getProperty("location_object_type_id");
        OBJECT_NATIVE_ID =
                prop.getProperty("object_native_id").split(",");
        LOCATION_ID =
                prop.getProperty("location_id");
        MAX_OBJECTS_COUNT =
                Integer.parseInt(prop.getProperty("max_objects_count"));
    }

    private Point2D setDistanceXY(String OBJECT_DISTANCE) {
        Point2D point = new Point2D();
        Distance distance =
                Distance.valueOf(prop.getProperty("object_native_id." + OBJECT_DISTANCE));
        switch (distance){
            case FAR:
                point.x = new Random().nextInt(Integer.parseInt(FAR_COORDS[0]));
                point.y = new Random().nextInt(Integer.parseInt(FAR_COORDS[1]));
                break;
            case MIDDLE:
                point.x = new Random().nextInt(Integer.parseInt(MIDDLE_COORDS[0]));
                point.y = new Random().nextInt(Integer.parseInt(MIDDLE_COORDS[1]));
                break;
            case CLOSE:
                point.x = new Random().nextInt(Integer.parseInt(CLOSE_COORDS[0]));
                point.y = new Random().nextInt(Integer.parseInt(CLOSE_COORDS[1]));
                break;
        }
        return point;
    }


    private void generateScript(){
        String start = "insert into location_object" +
                        "(\nlocation_object_behavior_type_id,\n" +
                        "location_object_type_id,\n" +
                        "object_native_id,\n" +
                        "rotation_angle,\n" +
                        "position_x,\n" +
                        "position_y,\n" +
                        "location_id\n)\n" +
                        "values\n(";
        String middle;
        String end = "";
        int rotation_angle;
        int rand_native_id;
        Random rand;
        for(int i = 1; i <= MAX_OBJECTS_COUNT; i++){
            rand = new Random();
            rotation_angle = rand.nextInt(360);
            rand_native_id = rand.nextInt(OBJECT_NATIVE_ID.length);

            middle = LOCATION_OBJECT_BEHAVIOR_TYPE_ID + "," +
                     LOCATION_OBJECT_TYPE_ID + "," ;
            middle+= OBJECT_NATIVE_ID[rand_native_id] + ",";
            middle+= rotation_angle + ",";

            middle+= setDistanceXY(OBJECT_NATIVE_ID[rand_native_id]).x + ",";
            middle+= setDistanceXY(OBJECT_NATIVE_ID[rand_native_id]).y + ",";
            middle+= LOCATION_ID + "),\n(";

            end += middle;
        }
        end = end.substring(0, end.length() - 3);
        script = start + end;
    }

    public final String getScript(){
        return script;
    }

    public static void main(String args[]) {
        PopulateObjectsLocation obj = new PopulateObjectsLocation();
        obj.generateScript();

        DBScriptInsert db = new DBScriptInsert();
        db.executeScript(obj.getScript());
    }
}
