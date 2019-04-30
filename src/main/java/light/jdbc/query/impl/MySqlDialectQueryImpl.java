package light.jdbc.query.impl;

import java.util.Map;

import light.jdbc.code.DbConfig;
import light.jdbc.code.DbContext;
import light.jdbc.table.TableMapping;

/**
 * mysql  数据库分页
 * @author hanjiang.Yue
 *
 * @param <T>
 */
public class MySqlDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{

	public MySqlDialectQueryImpl(TableMapping<T> tableMapping,DbConfig config) {
		super(tableMapping, config);
	}

	public MySqlDialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
		// TODO Auto-generated constructor stub
	}

	
	




}
