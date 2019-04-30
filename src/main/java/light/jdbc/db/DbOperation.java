package light.jdbc.db;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.sql.DataSource;



/**
 * 数据库操作
 * @author hanjiang.Yue
 *
 */
public interface DbOperation {
	
	
	

	/**
	 * 获取数据源
	 * @author hanjiang.Yue
	 * @return
	 */
	DataSource getDataSource();
	
	/**
	 * 执行SQL
	 * @author hanjiang.Yue
	 * @param sql
	 */
	public void execute(String sql);

	/**
	 * update
	 * @author hanjiang.Yue
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(String sql, Object... params);

	/**
	 * batchUpdate
	 * @author hanjiang.Yue
	 * @param sql
	 * @param batchArgs
	 * @return
	 */
	public int[] batchUpdate(String sql, List<Object[]> batchArgs);

	/**
	 * 返回唯一结果
	 * @author hanjiang.Yue
	 * @param sql
	 * @param resultType
	 * @param params
	 * @return
	 */
	public <T> T queryUnique(String sql, Class<T> resultType, Object... params);

	/**
	 * 返回Map
	 * @author hanjiang.Yue
	 * @param sql
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryMap(String sql, Object... params);

	/**
	 * 返回 List Map
	 * @author hanjiang.Yue
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> queryListMap(String sql, Object... params);

	/**
	 * 
	 * @author hanjiang.Yue
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> List<T> queryBeanList(Class<T> entityClass, String sql, Object[] params);

	/**
	 * 
	 * @author hanjiang.Yue
	 * @param sql
	 * @param fun
	 * @param params
	 * @return
	 */
	public <T> List<T> execute(String sql,Function<ResultSet,T> fun, Object... params);

	/**
	 * 
	 * @author hanjiang.Yue
	 * @param typeClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> List<T> queryObjectList(Class<T> typeClass, String sql, Object[] params);

	/**
	 * 
	 * @author hanjiang.Yue
	 * @param idClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> T instertIdAuto(Class<T> idClass, String sql, Object[] params);

	/**
	 * 
	 * @author hanjiang.Yue
	 * @param call
	 * @param callableStatementConsumer
	 * @param resultSetFun
	 * @return
	 */
	public <T> T prepareCall(String call, Consumer<CallableStatement> callableStatementConsumer,Function<ResultSet, T> resultSetFun);

}