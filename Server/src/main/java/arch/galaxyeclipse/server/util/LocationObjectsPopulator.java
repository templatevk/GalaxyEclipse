package arch.galaxyeclipse.server.util;


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
    private Float locationWidth;
    private Float locationHeight;
    private String dstDir;
    private String fileName;
    private String databaseName;
    private boolean deleteScriptFile;
    private boolean execute;
    private Random rand;
    private Integer[] objectNativeId;
    private int locationObjectBehaviorTypeId;
    private int locationId;
    private Map<Integer, Integer> distances;
    private Map<Integer, Integer> objectsTypesId;

    public LocationObjectsPopulator() {
        loadPropertiesFile();
        initializeVariables();
    }

    public void populate() {
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
            DbScriptExecutor executor = new DbScriptExecutor();
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
        locationObjectBehaviorTypeId = Integer.parseInt(prop.getProperty(
                "location_object_behavior_type_id"));
        locationId = Integer.parseInt(prop.getProperty("location.id"));
        locationWidth = Float.valueOf(prop.getProperty("location.width"));
        locationHeight = Float.valueOf(prop.getProperty("location.height"));
        String[] objNativeId = prop.getProperty("object_native_id").split(",");
        objectNativeId = new Integer[objNativeId.length];
        rand = new Random();

        distances = new HashMap<>();
        objectsTypesId = new HashMap<>();
        for(int i = 0; i < objNativeId.length; i++) {
            objectNativeId[i] = Integer.valueOf(objNativeId[i]);
        }

        for (int i = 0; i < objectNativeId.length; i++) {
            Integer objCount = Integer.valueOf(prop.getProperty(
                    "object_native_id." + objectNativeId[i]));
            Integer objTypeId = Integer.valueOf(prop.getProperty(
                    "object_native_id." + objectNativeId[i] + ".type"));
            distances.put(objectNativeId[i], objCount);
            objectsTypesId.put(objectNativeId[i], objTypeId);
        }
    }

    private GePosition[] setDistanceXY(int objectNativeId) {
        int count = distances.get(objectNativeId);
        GePosition[] position = new GePosition[count];

        for(int i = 0; i < position.length; i++) {
            position[i] = new GePosition();
            position[i].setX(rand.nextFloat() * locationWidth);
            position[i].setY(rand.nextFloat() * locationHeight);
        }

        return position;
    }

    private void generateScript() {
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

        for (int i = 0; i < objectNativeId.length; i++) {
            GePosition[] positions = setDistanceXY(objectNativeId[i]);

            for(GePosition pos : positions) {
                float rotation_angle = rand.nextFloat() * MAX_DEGREES;
                scriptBuilder.append(locationObjectBehaviorTypeId).append(", ");
                scriptBuilder.append(objectsTypesId.get(objectNativeId[i])).append(", ");
                scriptBuilder.append(objectNativeId[i]).append(", ");
                scriptBuilder.append(rotation_angle).append(", ");
                scriptBuilder.append(pos.getX()).append(", ");
                scriptBuilder.append(pos.getY()).append(", ");
                if (i != objectNativeId.length - 1) {
                    scriptBuilder.append(locationId).append("),\n(");
                } else {
                    scriptBuilder.append(locationId).append(");");
                }
            }
        }
        script = scriptBuilder.toString();
    }

    public static void main(String args[]) {
        new LocationObjectsPopulator().populate();
    }
}
