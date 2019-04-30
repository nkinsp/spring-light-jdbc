package light.jdbc.db.adapter;

import java.util.List;

import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;

public class UpdateDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public UpdateDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}

	@Override
	public Object cacheAdapter() {
		cacheOperation.delete(query.getCacheKey());
		return dbAdapter();
	}

	@Override
	public Object dbAdapter() {
		List<Object> params = query.getParams();
//		params.addAll(query.getParams());
		String sql = query.getQueryBuilder().buildUpdateSQL();
		log.info("==> execute [sql={},params={}]",sql,params);
		return dbOperation.update(sql,params.toArray());
	}

	@Override
	public ExecuteType executeType() {
		return ExecuteType.UPDATE;
	}

	
}
