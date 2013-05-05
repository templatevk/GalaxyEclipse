package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.LocationObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;
import org.testng.annotations.Test;

/**
 *
 */
@Slf4j
public class SerializationPerformanceTest {
    @Test
    public void testSerializationTime() throws Exception {
        LocationObject locationObject = LocationObject.getDefaultInstance();

        long start = System.currentTimeMillis();
        byte[] objectBytes = SerializationUtils.serialize(locationObject);
        log.info("Serialization takes " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        locationObject = (LocationObject)SerializationUtils.deserialize(objectBytes);
        log.info("Deserialization takes " + (System.currentTimeMillis() - start));
    }
}
