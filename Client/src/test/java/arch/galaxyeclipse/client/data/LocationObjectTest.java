package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.data.LocationInfoHolder.LocationObjectPositionOrdering;
import com.google.common.collect.TreeMultiset;
import org.fest.assertions.Assertions;
import org.testng.annotations.Test;

import static arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.LocationObject;

/**
 *
 */
public class LocationObjectTest {
    @Test
    public void testWrapper() throws Exception {
        LocationObject locationObject1 = LocationObject.newBuilder()
                .setObjectId(1)
                .setObjectTypeId(1)
                .setNativeId(1)
                .setPositionX(1)
                .setPositionY(1)
                .setRotationAngle(1)
                .build();
        LocationObject locationObject2 = LocationObject.newBuilder()
                .setObjectId(2)
                .setObjectTypeId(1)
                .setNativeId(1)
                .setPositionX(1)
                .setPositionY(1)
                .setRotationAngle(1)
                .build();

        TreeMultiset<LocationObject> multiset = TreeMultiset.create(
                new LocationObjectPositionOrdering());
        multiset.add(locationObject1);
        multiset.add(locationObject2);

        Assertions.assertThat(multiset)
                .hasSize(2)
                .contains(locationObject1)
                .contains(locationObject2);
    }
}
