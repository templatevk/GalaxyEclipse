package arch.galaxyeclipse.server.util;


import arch.galaxyeclipse.server.data.DbScriptExecutor;
import arch.galaxyeclipse.shared.common.GePosition;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
    private static final String PROP_FILE_NAME = "obj_script.properties";
    private static final float MAX_DEGREES = 360f;

    private Properties prop;
    private String script;
    private File scriptFile;
    private String dstDir;
    private String fileName;
    private String databaseName;
    private boolean deleteScriptFile;
    private boolean execute;
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

    public void populate() {
        DbScriptExecutor executor = new DbScriptExecutor();
        generateScript();
        if (!deleteScriptFile) {
            try {
                scriptFile = new File(dstDir, fileName);
                if (!scriptFile.exists()) {
                    log.info("Creating new file " + scriptFile.getPath());
                } else {
                    scriptFile.delete();
                    log.info("Replacing file " + scriptFile.getPath());
                }
                Files.createParentDirs(scriptFile);
                scriptFile.createNewFile();

                try (FileWriter scriptFileWriter = new FileWriter(scriptFile)) {
                    scriptFileWriter.write(script);
                }
            } catch (IOException e) {
                log.error("Error creating script file", e);
            }
        }
        if (execute) {
            executor.executeScript(script);
        }
    }

    private void loadPropertiesFile() {
        try {
            InputStream propStream = getClass().getResourceAsStream(PROP_FILE_NAME);
            prop = new Properties();
            prop.load(propStream);
        } catch (Exception e) {
            log.error("Error loading " + PROP_FILE_NAME);
            throw new InstantiationError();
        }
    }

    private void initializeVariables() {
        fileName = prop.getProperty("script.filename");
        dstDir = prop.getProperty("script.dst_dir");
        databaseName = prop.getProperty("script.db");
        deleteScriptFile = Boolean.valueOf(prop.getProperty("script.delete_file"));
        execute = Boolean.valueOf(prop.getProperty("script.execute"));

        farCoords = prop.getProperty("FAR").split(",");
        fCoordX = Float.valueOf(farCoords[0]);
        fCoordY = Float.valueOf(farCoords[1]);

        closeCoords = prop.getProperty("CLOSE").split(",");
        cCoordX = Float.valueOf(closeCoords[0]);
        cCoordY = Float.valueOf(closeCoords[1]);

        middleCoords =  prop.getProperty("MIDDLE").split(",");
        mCoordX = Float.valueOf(middleCoords[0]);
        mCoordY = Float.valueOf(middleCoords[1]);

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
        Random rand = new Random();
        StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append(
                "use " + databaseName + ";\n\n" +
                "insert into location_object" +
                "(\nlocation_object_behavior_type_id,\n" +
                "location_object_type_id,\n" +
                "object_native_id,\n" +
                "rotation_angle,\n" +
                "position_x,\n" +
                "position_y,\n" +
                "location_id)\n" +
                "values\n(");

        for (int i = 0; i < maxObjectsCount; i++) {
            float rotation_angle = rand.nextFloat() * MAX_DEGREES;
            int rand_native_id = rand.nextInt(objectNativeId.length);
            GePosition point = setDistanceXY(objectNativeId[rand_native_id]);

            scriptBuilder.append(locationObjectBehaviorTypeId).append(", ");
            scriptBuilder.append(locationObjectTypeId).append(", ");
            scriptBuilder.append(objectNativeId[rand_native_id]).append(", ");
            scriptBuilder.append(rotation_angle).append(", ");
            scriptBuilder.append(point.getX()).append(", ");
            scriptBuilder.append(point.getY()).append(", ");

            if (i != maxObjectsCount - 1) {
                scriptBuilder.append(locationId).append("),\n(");
            } else {
                scriptBuilder.append(locationId).append(");");
            }
        }
        script = scriptBuilder.toString();
    }

    public static void main(String args[]) {
        new LocationObjectsPopulator().populate();
    }

    private enum DistanceType {
        FAR,
        MIDDLE,
        CLOSE
    }
}
