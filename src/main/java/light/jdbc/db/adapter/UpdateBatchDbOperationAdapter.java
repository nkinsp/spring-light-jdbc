package light.jdbc.db.adapter;

import java.util.List;
import java.util.stream.Collectors;

import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;

public class UpdateBatchDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public UpdateBatchDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}

	@Override
	public Object cacheAdapter() {
		List<String> keys = query.getFiledBatch()
				.stream()
				.map(data->query.getCacheKey(data.get(tableMapping.getPrimaryKey()).toString()))
				.distinct()
				.collect(Collectors.toList());
		cacheOperation.delete(keys);
		return dbAdapter();
	}

	@Override
	public Object dbAdapter() {
		List<Object[]> params = query.getArrayParams();
		String updateSQL = query.getQueryBuilder().buildBatchUpdateSQL();
		log.info("==> execute [sql={},params={}]",updateSQL,params);
		return dbOperation.batchUpdate(updateSQL,params);
	}

	@Override
	public ExecuteType executeType() {
		// TODO Auto-generated method stub
		return ExecuteType.UPDATE_BATCH;
	}



}
