package arch.galaxyeclipse.server.util;


import arch.galaxyeclipse.shared.common.GePosition;
import com.sun.javafx.geom.Point2D;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: Spaun
 * Date: 06.05.13
 * Time: 23:21
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
public class LocationObjectsPopulator {
    private Properties prop;
    private final String propFileName = "obj_script.properties";
    private String script;
    private String[] farCoords;
    private String[] closeCoords;
    private String[] middleCoords;
    private Integer[] objectNativeId;
    private int locationObjectBehaviorTypeId;
    private int locationObjectTypeId;
    private int locationId;
    private int maxObjectsCount;
    private Map<Integer, DistanceType> distances;
    private float fCoordX, fCoordY, cCoordX, cCoordY, mCoordX, mCoordY;

    public LocationObjectsPopulator() {
        loadPropertiesFile();
        initializeVariables();
    }

    private void loadPropertiesFile() {
        prop = new Properties();
        try {
            prop.load(new FileInputStream(propFileName));
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    private void initializeVariables() {
        farCoords = prop.getProperty("FAR").split(",");
        fCoordX = Float.valueOf(farCoords[0]);
        fCoordY = Float.valueOf(farCoords[1]);

        closeCoords = prop.getProperty("CLOSE").split(",");
        cCoordX = Float.valueOf(closeCoords[0]);
        cCoordY = Float.valueOf(closeCoords[1]);

        middleCoords =  prop.getProperty("MIDDLE").split(",");
        mCoordX = Float.valueOf(middleCoords[0]);
        mCoordY = Float.valueOf(closeCoords[1]);

        locationObjectBehaviorTypeId = Integer.parseInt(prop.getProperty("location_object_behavior_type_id"));
        locationObjectTypeId = Integer.parseInt(prop.getProperty("location_object_type_id"));
        maxObjectsCount = Integer.parseInt(prop.getProperty("max_objects_count"));
        locationId = Integer.parseInt(prop.getProperty("location_id"));
        String[] objNativeId = prop.getProperty("object_native_id").split(",");
        objectNativeId = new Integer[objNativeId.length];

        distances = new HashMap<>();
        for(int i = 0; i < objNativeId.length; i++) {
            objectNativeId[i] = Integer.valueOf(objNativeId[i]);
        }

        for (int i = 0; i < objectNativeId.length; i++) {
            String distanceValue = prop.getProperty("object_native_id." + objectNativeId[i]);
            distances.put(objectNativeId[i], DistanceType.valueOf(distanceValue));
        }
    }

    private GePosition setDistanceXY(int objectDistance) {
        GePosition position = new GePosition();

        switch (distances.get(objectDistance)) {
            case FAR:
                position.setX(new Random().nextFloat() * fCoordX);
                position.setY(new Random().nextFloat() * fCoordY);
                break;
            case MIDDLE:
                position.setX(new Random().nextFloat() * mCoordX);
                position.setY(new Random().nextFloat() * mCoordY);
                break;
            case CLOSE:
                position.setX(new Random().nextFloat() * cCoordX);
                position.setY(new Random().nextFloat() * cCoordY);
                break;
        }
        return position;
    }

    private void generateScript() {
        final float MAX_DEGREES = 360f;
        float rotation_angle;
        int rand_native_id;
        GePosition point;
        StringBuilder middle;
        String end = "";
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

        for (int i = 0; i < maxObjectsCount; i++) {
            rand = new Random();
            rotation_angle = rand.nextFloat() * MAX_DEGREES;
            rand_native_id = rand.nextInt(objectNativeId.length);
            middle = new StringBuilder();
            middle.append(locationObjectBehaviorTypeId + "," +
                          locationObjectTypeId + ",");
            middle.append(objectNativeId[rand_native_id] + ",");
            middle.append(rotation_angle + ",");
            point = setDistanceXY(objectNativeId[rand_native_id]);
            middle.append(point.getX() + ",");
            middle.append(point.getY() + ",");
            middle.append(locationId + "),\n(");

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

        //DbScriptExecutor db = new DbScriptExecutor();
        //db.executeScript(obj.getScript());
    }

    private enum DistanceType {
        FAR,
        MIDDLE,
        CLOSE
    }
}
