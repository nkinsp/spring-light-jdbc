package light.jdbc.db;

/**
 * 数据库操作执行
 * @author hanjiang.Yue
 *
 */
public interface DbOperationAdapter{

	
	<R> R adapter();
	
}
