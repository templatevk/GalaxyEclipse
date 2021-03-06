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

    public static float getDistance(float xStart, float yStart, float xEnd, float yEnd) {
        double x = xStart - xEnd;
        double y = yStart - yEnd;
        return (float)Math.sqrt(x * x + y * y);
    }
}
