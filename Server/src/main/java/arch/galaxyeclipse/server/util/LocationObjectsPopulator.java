package arch.galaxyeclipse.server.util;

import arch.galaxyeclipse.shared.GeConstants;
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
public class LocationObjectsPopulator {
    private static final String PROP_FILE_NAME = "obj_script.properties";
    private static final float MAX_DEGREES = 360f;
    public static final String FILL = "fill";
    public static final String RANDOM = "random";
    public static final int OBJEECT_TYPE_STAR = 3;

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
    private Map<Integer, String> objectsMethod;
    private Map<Integer, Integer> objectsWidth;
    private Map<Integer, Integer> objectsHeight;
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
        locationWidth = Float.valueOf(prop.getProperty("location.width"));
        locationHeight = Float.valueOf(prop.getProperty("location.height"));
        objectDescriptionsCount = Integer.valueOf(prop.getProperty(
                "object_descriptions_count"));

        for (int i = 0; i < objectDescriptionsCount; i++) {
            String objMethod = prop.getProperty(
                    "object." + i + ".method");
            objectsMethod.put(i, objMethod);
            if (objMethod.toString().equals(RANDOM)) {
                Integer objCount = Integer.valueOf(prop.getProperty(
                        "object." + i + ".count"));
                objectsCount.put(i, objCount);
            }
            if (objMethod.toString().equals(FILL)) {
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

            GePosition[] positions = new GePosition[0];
            int posCount = 0;

            if (objectsMethod.get(i).equals(RANDOM)) {
                int count = objectsCount.get(i);
                positions = new GePosition[count];
                posCount = count;

                for (int j = 0; j < positions.length; j++) {
                    positions[j] = new GePosition();
                    positions[j].setX(rand.nextFloat() * locationWidth);
                    positions[j].setY(rand.nextFloat() * locationHeight);
                }
            } else if (objectsMethod.get(i).equals(FILL)) {
                int countX = (int) (locationWidth / (objectsWidth.get(i)
                        / GeConstants.LOCATION_TO_SCREEN_COORDS_COEF));
                int countY = (int) (locationHeight / (objectsHeight.get(i)
                        / GeConstants.LOCATION_TO_SCREEN_COORDS_COEF));

                int count = countX * countY;
                positions = new GePosition[count];
                posCount = count;

                int jX = 0;
                int jY = 0;
                for (int j = 0; j < count; j++) {
                    positions[j] = new GePosition();
                    positions[j].setX((objectsWidth.get(i)
                            / GeConstants.LOCATION_TO_SCREEN_COORDS_COEF) * jX);
                    positions[j].setY((objectsHeight.get(i)
                            / GeConstants.LOCATION_TO_SCREEN_COORDS_COEF) * jY);
                    jX++;
                    if ((objectsWidth.get(i) / GeConstants.LOCATION_TO_SCREEN_COORDS_COEF) * jX > locationWidth) {
                        jY++;
                        jX = 0;
                    }
                }
            }

            for (int j = 0; j < posCount; j++) {
                float rotation_angle = 0;
                if (!objectsTypeId.get(i).equals(OBJEECT_TYPE_STAR)) {
                    rotation_angle = rand.nextFloat() * MAX_DEGREES;
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
        new LocationObjectsPopulator().populate();
    }
}
