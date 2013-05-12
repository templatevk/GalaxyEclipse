package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.DynamicObjectsHolder.LocationObjectsHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket.Builder;
import org.fest.assertions.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;

/**
 *
 */
public class DynamicObjectsHolderTest {

    private Builder lopBuilder5;
    private Builder lopBuilder4;
    private Builder lopBuilder3;
    private Builder lopBuilder2;
    private Builder lopBuilder1;
    private LocationObjectsHolder sut;

    @BeforeMethod
    public void setUp() throws Exception {
        lopBuilder1 = LocationObjectPacket.newBuilder().setPositionX(100).setPositionY(100).setNativeId(1).setObjectId(1).setObjectTypeId(1).setRotationAngle(1);
        lopBuilder2 = LocationObjectPacket.newBuilder().setPositionX(200).setPositionY(200).setNativeId(1).setObjectId(2).setObjectTypeId(1).setRotationAngle(1);
        lopBuilder3 = LocationObjectPacket.newBuilder().setPositionX(300).setPositionY(300).setNativeId(1).setObjectId(3).setObjectTypeId(1).setRotationAngle(1);
        lopBuilder4 = LocationObjectPacket.newBuilder().setPositionX(400).setPositionY(400).setNativeId(1).setObjectId(4).setObjectTypeId(1).setRotationAngle(1);
        lopBuilder5 = LocationObjectPacket.newBuilder().setPositionX(500).setPositionY(500).setNativeId(1).setObjectId(5).setObjectTypeId(1).setRotationAngle(1);
        sut = new DynamicObjectsHolder().getLocationObjectsHolder(1);
    }

    @Test
    public void testDuplicates() throws Exception {
        sut.addLopBuilder(lopBuilder1);
        sut.addLopBuilder(lopBuilder2);
        sut.addLopBuilder(lopBuilder3);
        sut.addLopBuilder(lopBuilder4);
        sut.addLopBuilder(lopBuilder5);
        sut.addLopBuilder(lopBuilder5);
        sut.addLopBuilder(lopBuilder5);
        sut.addLopBuilder(lopBuilder5);
        sut.addLopBuilder(lopBuilder5);
        sut.addLopBuilder(lopBuilder5);

        Collection<Builder> matchingObjects = sut.getMatchingObjects(lopBuilder5);
        Assertions.assertThat(matchingObjects)
                .containsOnly(lopBuilder5)
                .hasSize(1);
    }

    @Test
    public void testMatchingObjects() throws Exception {
        sut.addLopBuilder(lopBuilder1);
        sut.addLopBuilder(lopBuilder2);
        sut.addLopBuilder(lopBuilder3);

        Collection<Builder> matchingObjects1 = sut.getMatchingObjects(lopBuilder3);
        Assertions.assertThat(matchingObjects1)
                .containsOnly(lopBuilder3)
                .hasSize(1);

        sut.updateLopBuilderX(lopBuilder3, 200);
        sut.updateLopBuilderY(lopBuilder3, 200);
        Collection<Builder> matchingObjects2 = sut.getMatchingObjects(lopBuilder3);
        Assertions.assertThat(matchingObjects2)
                .hasSize(2)
                .containsOnly(lopBuilder2, lopBuilder3);
    }
}

