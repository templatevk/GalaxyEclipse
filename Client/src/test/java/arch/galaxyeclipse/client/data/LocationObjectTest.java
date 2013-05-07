package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.data.LocationInfoHolder.LocationObjectPositionOrdering;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import com.google.common.collect.TreeMultiset;
import org.fest.assertions.Assertions;
import org.testng.annotations.Test;

/**
 *
 */
public class LocationObjectTest {
    @Test
    public void testWrapper() throws Exception {
        LocationObjectPacket locationObject1 = LocationObjectPacket.newBuilder()
                .setObjectId(1)
                .setObjectTypeId(1)
                .setNativeId(1)
                .setPositionX(1)
                .setPositionY(1)
                .setRotationAngle(1)
                .build();
        LocationObjectPacket locationObject2 = LocationObjectPacket.newBuilder()
                .setObjectId(2)
                .setObjectTypeId(1)
                .setNativeId(1)
                .setPositionX(1)
                .setPositionY(1)
                .setRotationAngle(1)
                .build();

        TreeMultiset<LocationObjectPacket> multiset = TreeMultiset.create(
                new LocationObjectPositionOrdering());
        multiset.add(locationObject1);
        multiset.add(locationObject2);

        Assertions.assertThat(multiset)
                .hasSize(2)
                .contains(locationObject1)
                .contains(locationObject2);
    }
}
