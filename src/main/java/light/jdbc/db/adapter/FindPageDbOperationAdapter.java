package light.jdbc.db.adapter;

import java.util.Arrays;
import java.util.List;

import light.jdbc.code.DbContext;
import light.jdbc.query.Query;
import light.jdbc.result.Page;

/**
 * 返回分页数据
 * @author hanjiang.Yue
 * @param <T>
 * @param <En>
 */
public class FindPageDbOperationAdapter<T,En> extends PagingDbOperationAdapter<T, En>{

	
	public FindPageDbOperationAdapter(DbContext dbContext, Query<T> query, Class<En> enClass,Integer pageNo,Integer pageSize) {
		super(dbContext, query, enClass,pageNo,pageSize);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object dbAdapter() {
		String selectCountSQL = query.getQueryBuilder().buildSelectSQL(Arrays.asList("COUNT(1)"));
		int indexOf = selectCountSQL.toUpperCase().indexOf("ORDER BY");
		if(indexOf !=  -1) {
			selectCountSQL = selectCountSQL.substring(0, indexOf);
		}
		log.info("==> execute [sql={},params={}]",selectCountSQL,query.getParams());
		Integer count = dbOperation.queryUnique(selectCountSQL, Integer.class, query.getParams().toArray());
		List<En> list = (List<En>) super.dbAdapter();
		return new Page<>(count,list);
		
	}

}
