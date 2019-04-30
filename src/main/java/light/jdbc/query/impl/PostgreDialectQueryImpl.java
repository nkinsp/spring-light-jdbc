package light.jdbc.query.impl;

import light.jdbc.code.DbConfig;
import light.jdbc.query.Query;
import light.jdbc.table.TableMapping;

/**
 * Postgre 数据库分页
 * @author hanjiang.Yue
 *
 * @param <T>
 */
public class PostgreDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{

	
	

	public PostgreDialectQueryImpl(TableMapping<T> tableMapping, DbConfig config) {
		super(tableMapping, config);
	}

	@Override
	public Query<T> limit(int pageNo, int pageSize) {
		return addFilter("LIMIT ? OFFSET ? ").addParams(pageSize,(pageNo-1)*pageSize);
	}
}
