package light.jdbc.db.adapter;

import java.util.List;
import java.util.Map;

import light.jdbc.code.DbContext;
import light.jdbc.query.Query;

public class FindMapDbOperationAdapter<T> extends FindMapListDbOperationAdapter<T>{

	public FindMapDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}
	
	@Override
	public Object cacheAdapter() {
		Object result = cacheOperation.get(tableMapping.getTableClass(), query.getCacheKey());
		if(result == null) {
			result = dbAdapter();
			if(result != null) {
				cacheOperation.put(query.getCacheKey(),query.getCacheTime(),result);
			}
		}
		return result;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object dbAdapter() {
		// TODO Auto-generated method stub
		List<Map>  list = (List<Map>) super.dbAdapter();
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
}
