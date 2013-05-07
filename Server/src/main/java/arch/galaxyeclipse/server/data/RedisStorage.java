package arch.galaxyeclipse.server.data;

/**
 * Database structure:
 *      *** LocationObjectPacket are stored in two sorted sets and a hash
 *          key in hash is composed of prepended unique byte and id bytes
 *          key and field in hash are the same
 *          key in sorted set is composed of prepended unique byte and location id bytes
 *          value in sorted set represents the hash key to obtain location object
 *          scores - x and y coords.
 */
public class RedisStorage {
    private static final int BITS_PER_BYTE = 8;
    private static final int FIRST_8_BITS_SET = 255;
    private static final int KEY_BYTES_COUNT = 5;

    private static final byte LOCATION_OBJECT_HASH_KEY_PREPENDED_BYTE = 1;
    private static final byte LOCATION_OBJECT_SORTED_SET_X_KEY_PREPENDED_BYTE = 2;
    private static final byte LOCATION_OBJECT_SORTED_SET_Y_KEY_PREPENDED_BYTE = 3;
    private static final byte LOCATION_OBJECT_BUF_SET_X_KEY_PREPENDED_BYTE = 4;
    private static final byte LOCATION_OBJECT_BUF_SET_Y_KEY_PREPENDED_BYTE = 5;

    private RedisStorage() {
    }

    public static byte[] getLocationObjectPacketHashKey(int locationObjectId) {
        return getKeyForId(locationObjectId, LOCATION_OBJECT_HASH_KEY_PREPENDED_BYTE);
    }

    public static byte[] getLocationObjectPacketSortedSetXKey(int locationId) {
        return getKeyForId(locationId, LOCATION_OBJECT_SORTED_SET_X_KEY_PREPENDED_BYTE);
    }

    public static byte[] getLocationObjectPacketSortedSetYKey(int locationId) {
        return getKeyForId(locationId, LOCATION_OBJECT_SORTED_SET_Y_KEY_PREPENDED_BYTE);
    }

    public static byte[] getLocationObjectPacketBufSetXKey(int locationObjectId) {
        return getKeyForId(locationObjectId, LOCATION_OBJECT_BUF_SET_X_KEY_PREPENDED_BYTE);
    }

    public static byte[] getLocationObjectPacketBufSetYKey(int locationObjectId) {
        return getKeyForId(locationObjectId, LOCATION_OBJECT_BUF_SET_Y_KEY_PREPENDED_BYTE);
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