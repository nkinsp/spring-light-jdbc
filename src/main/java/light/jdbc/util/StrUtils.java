package light.jdbc.util;

public class StrUtils {

	/**
	 * 驼峰转下划线
	 * @author hanjiang.Yue
	 * @param str
	 * @return
	 */
	public static String  humpToLine(String str) {
		if(str.contains("_")) {
			return str;
		}
		StringBuilder builder = new StringBuilder();
		try {
			char[] array = str.toCharArray();
			for (int i = 0; i < array.length; i++) {
				char charStr = array[i];
				if (i > 0 && Character.isUpperCase(charStr)) {
					builder.append("_");
				}
				builder.append(charStr);
			}
			return builder.toString().toLowerCase();
		} finally {
			builder = null;
		}
	}
	
	/**
	 * 下划线转驼峰
	 * @author hanjiang.Yue
	 * @param str
	 * @return
	 */
	public static String  lineToHump(String str) {
		StringBuilder builder = new StringBuilder();
		try {
			char[] array = str.toCharArray();
			for (int i = 0; i < array.length; i++) {
				char charStr = array[i];
				if (charStr == '_') {
					array[i + 1] = Character.toUpperCase(array[i + 1]);
					continue;
				}
				builder.append(charStr);
			}
			return builder.toString();
		} finally {
			builder = null;
		}
	}
	
	public static boolean isEmpty(Object value) {
		return null == value|| "".equals(value);
	}
	
	public static boolean isNotEmpty(Object value) {
		return !isEmpty(value);
	}
}
