package light.jdbc.db.springjdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;

import light.jdbc.db.DbOperation;

/**
 * spring jdbc 
 * @author hanjiang.Yue
 *
 */
public class SpringJdbcDbOperation implements DbOperation{

	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void execute(String sql) {
		jdbcTemplate.execute(sql);
	}

	@Override
	public int update(String sql, Object... params) {
		return jdbcTemplate.update(sql, params);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		return jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	@Override
	public <T> T queryUnique(String sql, Class<T> resultType, Object... params) {
		return jdbcTemplate.queryForObject(sql, resultType, params);
	}

	@Override
	public Map<String, Object> queryMap(String sql, Object... params) {
		return jdbcTemplate.queryForMap(sql, params);
	}

	@Override
	public List<Map<String, Object>> queryListMap(String sql, Object... params) {
		return jdbcTemplate.queryForList(sql, params);
	}

	@Override
	public <T> List<T> queryBeanList(Class<T> entityClass, String sql, Object[] params) {
		return jdbcTemplate.query(sql, new BeanRowMapper<T>(entityClass), params);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public SpringJdbcDbOperation(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public SpringJdbcDbOperation(DataSource dataSource) {
		this(new JdbcTemplate(dataSource));
	}

	@Override
	public DataSource getDataSource() {
		return this.jdbcTemplate.getDataSource();
	}

	@Override
	public <T> List<T> execute(String sql, Function<ResultSet,T> fun, Object... params) {
		return jdbcTemplate.query(sql, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				return  fun.apply(rs);
			}
		}, params);
	}

	@Override
	public <T> List<T> queryObjectList(Class<T> typeClass, String sql, Object[] params) {
		return jdbcTemplate.queryForList(sql, typeClass, params);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T instertIdAuto(Class<T> idClass, String sql, Object[] params) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
				PreparedStatement statement = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				for (int j = 0; j <params.length; j++) {
					statement.setObject((j+1),params[j]);
				}
				return statement;
			}
		}, keyHolder);
		
		
		
		if(idClass == int.class || idClass == Integer.class) {
			return (T) new Integer(keyHolder.getKey().intValue());
		}
		if(idClass == long.class || idClass == Long.class) {
			return (T) new Long(keyHolder.getKey().longValue());
		}
		return null;
	}

	@Override
	public <T> T prepareCall(String call, Consumer<CallableStatement> callableStatementConsumer,
			Function<ResultSet, T> resultSetFun) {
		return jdbcTemplate.execute(new ConnectionCallback<T>() {

			@Override
			public T doInConnection(Connection con) throws SQLException, DataAccessException {
				CallableStatement statement  = null;
				ResultSet resultSet = null;
				try {
					statement = con.prepareCall(call);
					callableStatementConsumer.accept(statement);
					resultSet = statement.executeQuery();
					return resultSetFun.apply(resultSet);
				} catch (Exception e) {
					// TODO: handle exception
					throw new RuntimeException(e);
				}finally {
					try {
						JdbcUtils.closeResultSet(resultSet);
					} finally {
						JdbcUtils.closeStatement(statement);
					}
					
				}
			}
		});
	}







	
}
