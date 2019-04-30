package light.jdbc.db.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;

public class FindListByIdsDbOperationAdapter<T> extends AbstractDbOperationAdapter<T> {

	public  FindListByIdsDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}
	
	private List<T> executeDbQuery(List<String> ids){
		Query<T> listQuery = context.createQuery(tableMapping.getTableClass()).where().idIn(ids.toArray());
		return query(listQuery);
	}
	
	protected  List<T> query(Query<T> query){
		String sql = query.getQueryBuilder().buildSelectSQL();
		log.info("==> execute [sql={},params={}]", sql, query.getParams());
		Object[] params = query.getParams().toArray();
		return dbOperation.queryBeanList(query.getTableMapping().getTableClass(), sql, params);
	}
	

	@Override
	public Object cacheAdapter() {
		
		
		//keys
		Set<String> keys = query.getParams()
				.stream()
				.distinct()
				.map(key->key.toString())
				.collect(Collectors.toSet());
		//从缓存中获取
		List<T> list = cacheOperation.list(
				tableMapping.getTableClass(),
				keys.stream().map(k->query.getCacheKey(k)).collect(Collectors.toList())
		);
		//缓存中存在的
		List<String> cacheKeys = list.stream()
				.map(object ->tableMapping.getIdProperty().getValue(object).toString())
				.collect(Collectors.toList());
		
		//没有被缓存的key
		List<String> noCacheKeys  = keys.stream()
				.filter(key->!cacheKeys.contains(key))
				.collect(Collectors.toList());
		if(noCacheKeys.size() > 0) {
			List<T> results = executeDbQuery(noCacheKeys);
			if(results.size() > 0) {
				//返回数据
				list.addAll(results);
				//加入缓存
				long time = query.getCacheTime();
				Map<String, Object> cacheMap = new HashMap<>();
				results.forEach(data->{
					String key = tableMapping.getIdProperty().getValue(data).toString();
					cacheMap.put(query.getCacheKey(key), data);
				});
				cacheOperation.put(cacheMap,time);
			}
		}
		return list;
	}

	@Override
	public Object dbAdapter() {
		return query(this.query);
	}

	@Override
	public ExecuteType executeType() {
		return ExecuteType.FIND_LIST;
	}

}
