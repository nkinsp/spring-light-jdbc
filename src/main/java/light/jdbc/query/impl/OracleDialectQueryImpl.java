package light.jdbc.query.impl;

import light.jdbc.code.DbConfig;
import light.jdbc.code.DbContext;
import light.jdbc.table.TableMapping;

public class OracleDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{

	public OracleDialectQueryImpl(TableMapping<T> tableMapping, DbConfig config) {
		super(tableMapping, config);
		// TODO Auto-generated constructor stub
	}

	public OracleDialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}
	
	@Override
	public String buildPagingSQL(int pageNo, int pageSize) {
		int start = (pageNo - 1) * pageSize;
		int end = pageNo * pageSize;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * ");
		sql.append("FROM (SELECT ROW_.*, ROWNUM ROWNUM_ ");
		sql.append("FROM (");
		sql.append(buildSelectSQL());
		sql.append(") ROW_");
		sql.append("WHERE ROWNUM <= "+end+") ");
		sql.append("WHERE ROWNUM_ >= "+start+" ");
		return sql.toString();
		
	}
	
	

}
