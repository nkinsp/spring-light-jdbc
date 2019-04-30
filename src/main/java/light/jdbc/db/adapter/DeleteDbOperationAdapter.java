package light.jdbc.db.adapter;


import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;

public class DeleteDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public DeleteDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}

	@Override
	public Object cacheAdapter() {
		cacheOperation.delete(query.getCacheKey());
		return dbAdapter();
	}

	@Override
	public Object dbAdapter() {
		String sql = query.getQueryBuilder().buildDeleteSQL();
		log.info("==> execute DELETE [sql={},params={}]",sql,query.getParams());
		return  dbOperation.update(sql, query.getParams().toArray());
	}

	@Override
	public ExecuteType executeType() {
		return ExecuteType.DELETE;
	}








	

}
