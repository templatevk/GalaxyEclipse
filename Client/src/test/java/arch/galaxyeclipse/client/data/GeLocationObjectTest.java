package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.data.GeLocationInfoHolder.LocationObjectPositionOrdering;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import com.google.common.collect.TreeMultiset;
import org.fest.assertions.Assertions;
import org.testng.annotations.Test;

/**
 *
 */
public class GeLocationObjectTest {

    @Test
    public void testWrapper() throws Exception {
        GeLocationObjectPacket locationObject1 = GeLocationObjectPacket.newBuilder()
                .setObjectId(1)
                .setObjectTypeId(1)
                .setNativeId(1)
                .setPositionX(1)
                .setPositionY(1)
                .setRotationAngle(1)
                .build();
        GeLocationObjectPacket locationObject2 = GeLocationObjectPacket.newBuilder()
                .setObjectId(2)
                .setObjectTypeId(1)
                .setNativeId(1)
                .setPositionX(1)
                .setPositionY(1)
                .setRotationAngle(1)
                .build();

        TreeMultiset<GeLocationObjectPacket> multiset = TreeMultiset.create(
                new LocationObjectPositionOrdering());
        multiset.add(locationObject1);
        multiset.add(locationObject2);

        Assertions.assertThat(multiset)
                .hasSize(2)
                .contains(locationObject1)
                .contains(locationObject2);
    }
}
