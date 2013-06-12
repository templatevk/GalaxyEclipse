package arch.galaxyeclipse.shared.common;

/**
 *
 */
public class GeMathUtils {

    private GeMathUtils() {

    }

    public static float getLineAngleInDegrees(float centerX, float centerY,
            float pointX, float pointY) {

        float angle = (float) Math.toDegrees(GeMathUtilsCopied.atan2(
                pointX - centerX, centerY - pointY));
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }
}
