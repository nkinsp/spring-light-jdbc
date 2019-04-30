package light.jdbc.db.adapter;

import java.util.List;

import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;

public class FindObjectListDbOperationAdapter<T,R> extends AbstractDbOperationAdapter<T>{

	private Class<R> resultType;
	
	public FindObjectListDbOperationAdapter(DbContext context, Query<T> query,Class<R> resultType) {
		super(context, query);
		this.resultType = resultType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object cacheAdapter() {
		List<R> list = cacheOperation.list(resultType, query.getCacheKey());
		if(list == null || list.size() == 0) {
			list = (List<R>) dbAdapter();
			if(list != null && list.size() > 0) {
				cacheOperation.put(query.getCacheKey(),list);
			}
		}
		return list;
	}

	@Override
	public Object dbAdapter() {
		String sql = query.getQueryBuilder().buildSelectSQL();
		log.info("==> execute [sql={},params={}]",sql,query.getParams());
		return dbOperation.queryObjectList(resultType, sql, query.getParams().toArray());
	}

	/**
	 * @return the resultType
	 */
	public Class<R> getResultType() {
		return resultType;
	}

	@Override
	public ExecuteType executeType() {
		// TODO Auto-generated method stub
		return ExecuteType.SELECT;
	}


	
}
