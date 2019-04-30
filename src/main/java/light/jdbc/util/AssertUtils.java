package light.jdbc.util;

public class AssertUtils {
	
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new RuntimeException(message);
        }
    }

    public static void isFalse(boolean expression, String message) {
        isTrue(!expression, message);
    }

    public static void isNull(Object object, String message) {
        isTrue(object == null, message);
    }

    public static void notNull(Object object, String message) {
        isTrue(object != null, message);
    }

}
