package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.protocol.GeProtocolMessageFactory;
import arch.galaxyeclipse.shared.context.ContextHolder;

/**
 *
 */
public class JedisOperations {
    private static final int BITS_PER_BYTE = 8;
    private static final int FIRST_8_BITS_SET = 255;
    private static final int KEY_BYTES_COUNT = 5;

    private static final byte LOCATION_OBJECT_KEY_PREPENDED_BYTE = 0;
    private static final byte SHIP_STATE_RESPONSE_KEY_PREPENDED_BYTE = 1;
    private GeProtocolMessageFactory geProtocolMessageFactory;

    public JedisOperations() {
        geProtocolMessageFactory = ContextHolder.getBean(GeProtocolMessageFactory.class);
    }

    public static byte[] getLocationObjectKey(int id) {
        return getKeyForId(id, LOCATION_OBJECT_KEY_PREPENDED_BYTE);
    }

    public static byte[] getShipStateResponseKey(int id) {
        return getKeyForId(id, SHIP_STATE_RESPONSE_KEY_PREPENDED_BYTE);
    }

    private static byte[] getKeyForId(int id, byte prependedByte) {
        byte[] key = new byte[KEY_BYTES_COUNT];
        key[0] = prependedByte;
        for (int i = KEY_BYTES_COUNT - 1; i > 0 ; i--) {
            key[i] = (byte)(FIRST_8_BITS_SET & id);
            id >>= BITS_PER_BYTE;
        }
        return key;
    }
}