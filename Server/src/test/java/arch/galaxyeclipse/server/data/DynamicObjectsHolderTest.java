package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.model.LocationObject;
import org.fest.assertions.Assertions;
import org.testng.annotations.Test;

import java.util.Collection;

/**
 *
 */
public class DynamicObjectsHolderTest {
    @Test
    public void testGetDynamicObjects() throws Exception {
        LocationObject lo1 = new LocationObject();
        LocationObject lo2 = new LocationObject();
        LocationObject lo3 = new LocationObject();
        lo1.setLocationId(1);
        lo1.setLocationObjectId(1);
        lo1.setPositionX(100);
        lo1.setPositionY(100);
        lo2.setLocationId(1);
        lo2.setLocationObjectId(2);
        lo2.setPositionX(900);
        lo2.setPositionY(900);
        lo3.setLocationId(1);
        lo3.setLocationObjectId(3);
        lo3.setPositionX(500);
        lo3.setPositionY(500);

        DynamicObjectsHolder sut = new DynamicObjectsHolder();
        sut.addLocationObject(lo1);
        sut.addLocationObject(lo3);
        sut.addLocationObject(lo2);

        Collection<LocationObject> dynamicObjects1 = sut.getDynamicObjects(lo2);
        Assertions.assertThat(dynamicObjects1)
                .containsOnly(lo2);

        lo2.setPositionX(110);
        lo2.setPositionY(110);

        Collection<LocationObject> dynamicObjects2 = sut.getDynamicObjects(lo2);
        Assertions.assertThat(dynamicObjects2)
                .containsOnly(lo1, lo2);
    }
}

