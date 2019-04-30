package light.jdbc.util;

import java.util.UUID;

/**
 * id生成
 * @author hanjiang.Yue
 */
public class IdWorkUtils {

	  private static final Sequence WORKER = new Sequence();
	/**
	 * 获取UUID
	 * @author hanjiang.Yue
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 序列化id
	 * @author hanjiang.Yue
	 * @return
	 */
	public static Long sequenceId() {
		return WORKER.nextId();
	}
}
