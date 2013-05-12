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
    private Integer objectDescriptionsCount;
    private int locationObjectBehaviorTypeId;
    private int locationId;
    private Map<Integer, Integer> objectsCount;
    private Map<Integer, Integer> objectsTypeId;
    private Map<Integer, Integer> objectsNativeId;

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
        rand = new Random();
        objectsCount = new HashMap<>();
        objectsTypeId = new HashMap<>();
        objectsNativeId = new HashMap<>();

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
        objectDescriptionsCount = Integer.valueOf(prop.getProperty(
                "object_descriptions_count"));

        for (int i = 0; i < objectDescriptionsCount; i++) {
            Integer objCount = Integer.valueOf(prop.getProperty(
                    "object." + i + ".count"));
            Integer objTypeId = Integer.valueOf(prop.getProperty(
                    "object." + i + ".type_id"));
            Integer objNativeId = Integer.valueOf(prop.getProperty(
                    "object." + i + ".type_id"));
            objectsCount.put(i, objCount);
            objectsTypeId.put(i, objTypeId);
            objectsNativeId.put(i, objNativeId);
        }
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

        for (int i = 0; i < objectDescriptionsCount; i++) {
            int count = objectsCount.get(i);
            GePosition[] positions = new GePosition[count];

            for(int j = 0; j < positions.length; j++) {
                positions[j] = new GePosition();
                positions[j].setX(rand.nextFloat() * locationWidth);
                positions[j].setY(rand.nextFloat() * locationHeight);

                float rotation_angle = rand.nextFloat() * MAX_DEGREES;
                scriptBuilder.append(locationObjectBehaviorTypeId).append(", ");
                scriptBuilder.append(objectsTypeId.get(i)).append(", ");
                scriptBuilder.append(objectsNativeId.get(i)).append(", ");
                scriptBuilder.append(rotation_angle).append(", ");
                scriptBuilder.append(positions[j].getX()).append(", ");
                scriptBuilder.append(positions[j].getY()).append(", ");

                if (i == objectDescriptionsCount - 1 && j == positions.length - 1) {
                    scriptBuilder.append(locationId).append(");");
                } else {
                    scriptBuilder.append(locationId).append("),\n(");
                }
            }
        }
        script = scriptBuilder.toString();
    }

    public static void main(String args[]) {
        new LocationObjectsPopulator().populate();
    }
}
