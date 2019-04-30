package light.jdbc.db.adapter;

import light.jdbc.code.DbContext;
import light.jdbc.query.Query;

public class PagingDbOperationAdapter<T, En> extends FindBeanListDbOperationAdapter<T, En> {

	private int pageNo;

	private int pageSize;

	public PagingDbOperationAdapter(DbContext dbContext, Query<T> query, Class<En> enClass, int pageNo, int pageSize) {
		super(dbContext, query, enClass);
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	@Override
	protected String builderQuerySql() {
		return query.getQueryBuilder().buildPagingSQL(pageNo, pageSize);
	}

}
