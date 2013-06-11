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

@Slf4j
public class GeLocationObjectsPopulator {

    private static final String PROP_FILE_NAME = "obj_script.properties";
    private static final String FILL = "fill";
    private static final String RANDOM = "random";

    private static final int MAX_DEGREES = 360;
    private static final int OBJECT_TYPE_STAR = 3;

    private Properties prop;
    private String script;
    private File scriptFile;
    private int locationWidth;
    private int locationHeight;
    private String dstDir;
    private String fileName;
    private String databaseName;
    private boolean deleteScriptFile;
    private boolean execute;
    private Random rand;
    private Integer objectDescriptionsCount;
    private int locationObjectBehaviorTypeId;
    private int locationId;
    private Map<Integer, String> objectsMethod;
    private Map<Integer, Integer> objectsWidth;
    private Map<Integer, Integer> objectsHeight;
    private Map<Integer, Integer> objectsCount;
    private Map<Integer, Integer> objectsTypeId;
    private Map<Integer, Integer> objectsNativeId;

    public GeLocationObjectsPopulator() {
        loadPropertiesFile();
        initializeVariables();
    }

    public void populate() {
        generateScript();
        if (!deleteScriptFile) {
            try {
                scriptFile = new File(dstDir, fileName);
                if (!scriptFile.exists()) {
                    GeLocationObjectsPopulator.log.info("Creating new file " + scriptFile.getPath());
                } else {
                    scriptFile.delete();
                    GeLocationObjectsPopulator.log.info("Replacing file " + scriptFile.getPath());
                }
                Files.createParentDirs(scriptFile);
                scriptFile.createNewFile();

                try (FileWriter scriptFileWriter = new FileWriter(scriptFile)) {
                    scriptFileWriter.write(script);
                }
            } catch (IOException e) {
                GeLocationObjectsPopulator.log.error("Error creating script file", e);
            }
        }
        if (execute) {
            GeDbScriptExecutor executor = new GeDbScriptExecutor();
            executor.executeScript(script);
        }
    }

    private void loadPropertiesFile() {
        try {
            InputStream propStream = getClass().getResourceAsStream(PROP_FILE_NAME);
            prop = new Properties();
            prop.load(propStream);
        } catch (Exception e) {
            GeLocationObjectsPopulator.log.error("Error loading " + PROP_FILE_NAME);
            throw new InstantiationError();
        }
    }

    private void initializeVariables() {
        rand = new Random();
        objectsMethod = new HashMap<>();
        objectsWidth = new HashMap<>();
        objectsHeight = new HashMap<>();
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
        locationWidth = Integer.valueOf(prop.getProperty("location.width"));
        locationHeight = Integer.valueOf(prop.getProperty("location.height"));
        objectDescriptionsCount = Integer.valueOf(prop.getProperty(
                "object_descriptions_count"));

        for (int i = 0; i < objectDescriptionsCount; i++) {
            String objMethod = prop.getProperty(
                    "object." + i + ".method");
            objectsMethod.put(i, objMethod);
            if (objMethod.equals(RANDOM)) {
                Integer objCount = Integer.valueOf(prop.getProperty(
                        "object." + i + ".count"));
                objectsCount.put(i, objCount);
            }
            if (objMethod.equals(FILL)) {
                Integer objWidth = Integer.valueOf(prop.getProperty(
                        "object." + i + ".width"));
                objectsWidth.put(i, objWidth);
                Integer objHeight = Integer.valueOf(prop.getProperty(
                        "object." + i + ".height"));
                objectsHeight.put(i, objHeight);
            }
            Integer objTypeId = Integer.valueOf(prop.getProperty(
                    "object." + i + ".type_id"));
            objectsTypeId.put(i, objTypeId);
            Integer objNativeId = Integer.valueOf(prop.getProperty(
                    "object." + i + ".native_id"));
            objectsNativeId.put(i, objNativeId);
        }
    }

    private void generateScript() {
        StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append("use ")
                .append(databaseName)
                .append(";\n\n")
                .append("insert into location_object")
                .append("(\nlocation_object_behavior_type_id,\n")
                .append("location_object_type_id,\n")
                .append("object_native_id,\n")
                .append("rotation_angle,\n")
                .append("position_x,\n")
                .append("position_y,\n")
                .append("location_id)\n")
                .append("values\n(");

        for (int i = 0; i < objectDescriptionsCount; i++) {
            GePosition[] positions = null;

            if (objectsMethod.get(i).equals(RANDOM)) {
                positions = new GePosition[objectsCount.get(i)];

                for (int j = 0; j < positions.length; j++) {
                    positions[j] = new GePosition();
                    positions[j].setX(rand.nextInt(locationWidth));
                    positions[j].setY(rand.nextInt(locationHeight));
                }
            } else if (objectsMethod.get(i).equals(FILL)) {
                int objectX = (int)Math.floor(locationWidth / objectsWidth.get(i));
                int objectY = (int)Math.floor(locationHeight / objectsHeight.get(i));

                positions = new GePosition[objectX * objectY];

                int xCount = 0;
                int yCount = 0;
                int width = objectsWidth.get(i);
                int height = objectsHeight.get(i);
                for (int j = 0; j < positions.length; j++) {
                    positions[j] = new GePosition();
                    positions[j].setX(width * xCount);
                    positions[j].setY(height * yCount);
                    xCount++;

                    if (xCount == objectX) {
                        yCount++;
                        xCount = 0;
                    }
                }
            }

            for (int j = 0; j < positions.length; j++) {
                int rotation_angle = 0;
                if (!objectsTypeId.get(i).equals(OBJECT_TYPE_STAR)) {
                    rotation_angle = rand.nextInt(MAX_DEGREES);
                }

                scriptBuilder.append(locationObjectBehaviorTypeId)
                        .append(", ")
                        .append(objectsTypeId.get(i))
                        .append(", ")
                        .append(objectsNativeId.get(i))
                        .append(", ")
                        .append(rotation_angle)
                        .append(", ")
                        .append(positions[j].getX())
                        .append(", ")
                        .append(positions[j].getY())
                        .append(", ");

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
        new GeLocationObjectsPopulator().populate();
    }
}
