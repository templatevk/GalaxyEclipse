package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStateResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 *
 */
public class JedisSerializers {
    private JedisSerializers() {

    }

    public static class LocationObjectPacketSerializer implements RedisSerializer<LocationObjectPacket> {
        @Override
        public byte[] serialize(LocationObjectPacket locationObject) throws SerializationException {
            return locationObject.toByteArray();
        }

        @Override
        public LocationObjectPacket deserialize(byte[] bytes) throws SerializationException {
            try {
                return LocationObjectPacket.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                throw new SerializationException("Deserialization error ", e);
            }
        }
    }

    public static class ShipStateResponseSerializer implements RedisSerializer<ShipStateResponse> {
        @Override
        public byte[] serialize(ShipStateResponse shipStateResponse) throws SerializationException {
            return shipStateResponse.toByteArray();
        }

        @Override
        public ShipStateResponse deserialize(byte[] bytes) throws SerializationException {
            try {
                return ShipStateResponse.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                throw new SerializationException("Deserialization error ", e);
            }
        }
    }
}
