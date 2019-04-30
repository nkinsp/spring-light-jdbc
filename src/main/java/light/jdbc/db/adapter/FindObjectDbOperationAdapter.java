package light.jdbc.db.adapter;

import java.util.List;

import light.jdbc.code.DbContext;
import light.jdbc.query.Query;

public class FindObjectDbOperationAdapter<T,R> extends FindObjectListDbOperationAdapter<T, R>{

	public FindObjectDbOperationAdapter(DbContext context, Query<T> query, Class<R> resultType) {
		super(context, query, resultType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object cacheAdapter() {
		R result = cacheOperation.get(getResultType(), query.getCacheKey());
		if(result == null) {
			result = (R) dbAdapter();
			if(result != null) {
				cacheOperation.put(query.getCacheKey(), result);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object dbAdapter() {
		List<R> results =  (List<R>) super.dbAdapter();
		if(results == null || results.size() == 0) {
			return null;
		}
		return results.get(0);
	}
	
	
	
}
