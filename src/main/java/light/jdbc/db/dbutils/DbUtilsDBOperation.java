package light.jdbc.db.dbutils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import light.jdbc.db.DbOperation;
import light.jdbc.db.dbutils.handler.BeanHandler;

/**
 * db utils
 * @author hanjiang.Yue
 *
 */
public class DbUtilsDBOperation implements DbOperation{

	private QueryRunner queryRunner;
	
	@Override
	public DataSource getDataSource() {
		return queryRunner.getDataSource();
	}

	private int execute(String sql,Object...params) {
		try {
			return queryRunner.execute(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void execute(String sql) {
		execute(sql, new Object[] {});
	}

	@Override
	public int update(String sql, Object... params) {
		return execute(sql, params);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		Object[][] params = new Object[batchArgs.size()][];
		for (int i = 0; i < batchArgs.size(); i++) {
			params[i] = batchArgs.get(i);
		}
		try {
			return queryRunner.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T queryUnique(String sql, Class<T> resultType, Object... params) {
		List<T> queryObjectList = queryObjectList(resultType, sql, params);
		if(queryObjectList!=null && queryObjectList.size() > 0) {
			return queryObjectList.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> queryMap(String sql, Object... params) {
		List<Map<String,Object>> list = queryListMap(sql, params);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> queryListMap(String sql, Object... params) {
		try {
			return queryRunner.query(sql, new MapListHandler(), params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> List<T> queryBeanList(Class<T> entityClass, String sql, Object[] params) {
		try {
			return queryRunner.query(sql, new BeanHandler<>(entityClass), params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public DbUtilsDBOperation(DataSource dataSource) {
		super();
		this.queryRunner = new QueryRunner(dataSource);
	}

	@Override
	public <T> List<T> execute(String sql,Function<ResultSet,T> fun, Object... params) {
		 try {
			 return queryRunner.execute(sql, new ResultSetHandler<T>() {
				@Override
				public T handle(ResultSet rs) throws SQLException {
					return fun.apply(rs);
				}}, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> List<T> queryObjectList(Class<T> typeClass, String sql, Object[] params) {
		return execute(sql,rs->{
			try {
				return rs.getObject(0, typeClass);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		},params);
	}


	@Override
	public <T> T instertIdAuto(Class<T> idClass, String sql, Object[] params) {
		try {
			queryRunner.insert(sql, new ResultSetHandler<T>() {
				@Override
				public T handle(ResultSet rs) throws SQLException {
					if(rs.next()) {
						return rs.getObject(0, idClass);
					}
					return null;
				}
			},params);
		} catch (SQLException e) {
			 throw new RuntimeException(e);
		} 
		return null;
	}

	@Override
	public <T> T prepareCall(String call, Consumer<CallableStatement> callableStatementConsumer,
			Function<ResultSet, T> resultSetFun) {
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet resultSet = null;
		 try {
			connection = queryRunner.getDataSource().getConnection();
			callableStatement = connection.prepareCall(call);
			callableStatementConsumer.accept(callableStatement);
			resultSet  = callableStatement.executeQuery();
			return resultSetFun.apply(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			DbUtils.closeQuietly(connection, callableStatement, resultSet);
		}
	}



	
}
