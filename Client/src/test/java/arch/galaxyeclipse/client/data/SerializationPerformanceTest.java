package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfo;
import arch.galaxyeclipse.shared.protocol.ShipStaticInfoCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;
import org.testng.annotations.Test;

import java.io.Serializable;

/**
 *
 */
@Slf4j
public class SerializationPerformanceTest {
    @Test
    public void testSerializationTime() throws Exception {
        ShipStaticInfoCommand shipStaticInfoCommand = new TestCommand();

        long start = System.currentTimeMillis();
        byte[] objectBytes = SerializationUtils.serialize(shipStaticInfoCommand);
        log.info("Serialization takes " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        shipStaticInfoCommand = (ShipStaticInfoCommand)SerializationUtils.deserialize(objectBytes);
        log.info("Deserialization takes " + (System.currentTimeMillis() - start));
    }

    class TestCommand implements ShipStaticInfoCommand, Serializable {
        @Override
        public void perform(ShipStaticInfo argument) {

        }
    }
}
