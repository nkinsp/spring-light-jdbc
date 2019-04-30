package light.jdbc.db.adapter;

import java.util.List;
import java.util.stream.Collectors;

import light.jdbc.activerecord.MapActiveRecord;
import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;
import light.jdbc.util.ClassUtils;

public class FindBeanListDbOperationAdapter<T,En> extends AbstractDbOperationAdapter<T>{

	private Class<En> enClass;
	
	public FindBeanListDbOperationAdapter(DbContext dbContext, Query<T> query,Class<En> enClass) {
		super(dbContext, query);
		this.enClass = enClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object cacheAdapter() {
		List<En> list = cacheOperation.list(enClass, query.getCacheKey());
		if(list == null || list.size() == 0) {
			list = (List<En>) dbAdapter();
			if(list != null && list.size() > 0) {
				cacheOperation.put(query.getCacheKey(),list);
			}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object dbAdapter() {
		String sql = query.getQueryBuilder().buildSelectSQL();
		log.info("==> execute [sql={},params={}]",sql,query.getParams());
		if(MapActiveRecord.class.isAssignableFrom(enClass)) {
			return dbOperation.queryListMap(sql, query.getParams().toArray()).stream().map(map->{
				MapActiveRecord activeRecord = (MapActiveRecord) ClassUtils.newInstance(enClass);
				activeRecord.putAll(map);
				return activeRecord;
			}).collect(Collectors.toList());
		}
		return dbOperation.queryBeanList(enClass, sql, query.getParams().toArray());
	}

	@Override
	public ExecuteType executeType() {
		// TODO Auto-generated method stub
		return ExecuteType.SELECT;
	}



}
