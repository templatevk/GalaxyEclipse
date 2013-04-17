package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.shared.protocol.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.util.*;

import java.util.*;

/**
 *
 */
@Slf4j
@Data
public class ShipStaticInfoHolder extends ServerPacketListener {
    private GeProtocol.ShipStaticInfo shipStaticInfo;

    ShipStaticInfoHolder() {

    }

    @Override
    protected void onPacketReceivedImpl(GeProtocol.Packet packet) {

        switch (packet.getType()) {
            case SHIP_STATIC_INFO_COMMAND:
                ShipStaticInfoCommand command = (ShipStaticInfoCommand)SerializationUtils.deserialize(
                        packet.getGameInfoCommandHolder().getSerializedCommand().toByteArray());
                command.perform(shipStaticInfo);
                break;
        }
    }

    @Override
    public List<GeProtocol.Packet.Type> getPacketTypes() {
        return Arrays.asList(GeProtocol.Packet.Type.SHIP_STATIC_INFO_COMMAND);
    }
}
