package light.jdbc.query.impl;

import light.jdbc.code.DbConfig;
import light.jdbc.code.DbContext;
import light.jdbc.query.Query;
import light.jdbc.table.TableMapping;

/**
 * @author hanjiang.Yue
 * @param <T>
 */
public class DB2DialectQueryImpl<T> extends AbstractSupportQueryImpl<T> {

	public DB2DialectQueryImpl(TableMapping<T> tableMapping, DbConfig config) {
		super(tableMapping, config);
	}

	public DB2DialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}

	@Override
	public String buildPagingSQL(int pageNo, int pageSize) {
		int start = (pageNo - 1) * pageSize;
		int end = pageNo * pageSize;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ");
		sql.append(buildSelectSQL());
		sql.append(" ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID BETWEEN "+start+" AND "+end);
		return sql.toString();
	}

}
