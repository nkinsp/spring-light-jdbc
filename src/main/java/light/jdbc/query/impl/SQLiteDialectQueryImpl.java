package light.jdbc.query.impl;

import light.jdbc.code.DbConfig;
import light.jdbc.code.DbContext;
import light.jdbc.query.Query;
import light.jdbc.table.TableMapping;

/**
 * SQLite 数据库分页
 * @author hanjiang.Yue
 * @param <T>
 */
public class SQLiteDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{


	
	public SQLiteDialectQueryImpl(TableMapping<T> tableMapping, DbConfig config) {
		super(tableMapping, config);
	}
	
	

	public SQLiteDialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}



	@Override
	public Query<T> limit(int pageNo, int pageSize) {
		return addFilter("LIMIT ? OFFSET ? ").addParams(pageSize,(pageNo-1)*pageSize);
	}
	

}
