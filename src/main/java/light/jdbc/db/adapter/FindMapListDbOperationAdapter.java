package light.jdbc.db.adapter;


import java.util.List;
import java.util.Map;

import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;

public class FindMapListDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public FindMapListDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object cacheAdapter() {
		List<Map> list = cacheOperation.list(Map.class, query.getCacheKey());
		if(list == null || list.size() == 0) {
			list = (List<Map> ) dbAdapter();
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
		return dbOperation.queryListMap(sql, query.getParams().toArray());
	}

	@Override
	public ExecuteType executeType() {
		// TODO Auto-generated method stub
		return ExecuteType.SELECT;
	}

	

}
