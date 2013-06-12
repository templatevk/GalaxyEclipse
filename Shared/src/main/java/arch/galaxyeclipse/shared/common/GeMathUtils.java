package arch.galaxyeclipse.shared.common;

/**
 *
 */
public class GeMathUtils {

    private GeMathUtils() {

    }

    public static float getLineAngleInDegrees(float centerX, float centerY,
            float pointX, float pointY) {

        float deltaX = centerX - pointX;
        float deltaY = centerY - pointY;

        return GeMathUtilsCopied.atan2(deltaY, deltaX);
    }
}
