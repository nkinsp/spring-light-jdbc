package light.jdbc.db.adapter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import light.jdbc.cache.CacheOperation;
import light.jdbc.code.DbContext;
import light.jdbc.db.DbOperation;
import light.jdbc.db.DbOperationAdapter;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;
import light.jdbc.query.QueryBuilder;
import light.jdbc.table.TableMapping;
/**
 * @author hanjiang.Yue
 * @param <T>
 */
public abstract class AbstractDbOperationAdapter<T> implements DbOperationAdapter{

	public  DbContext context;
	
	public  Query<T> query;
	
	public  CacheOperation cacheOperation;
	
	public  DbOperation dbOperation;
	
	public  TableMapping<T> tableMapping;
	
	public  Logger log = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * 执行缓存操作
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	public abstract Object cacheAdapter();
	
	public abstract ExecuteType executeType();
	
	
	/**
	 * 执行数据库操作
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	public  abstract Object dbAdapter();
	
	

	@SuppressWarnings("unchecked")
	@Override
	public <R> R adapter() {
		try {
			if (query.isCache()) {
				return (R) cacheAdapter();
			}
			return (R) dbAdapter();
		} finally {
			//释放query
			 QueryBuilder queryBuilder = (QueryBuilder) query;
			 queryBuilder.release();
			 query = null;
		}
	}

	public AbstractDbOperationAdapter(DbContext context, Query<T> query) {
		super();
		this.context = context;
		this.cacheOperation = context.getCacheOperation();
		this.dbOperation = context.getDbOperation();
		this.query = query;
		this.tableMapping = this.query.getTableMapping();
		if(!this.query.isCache()) {
			if(this.tableMapping.isCache()) {
				this.query.cache(this.tableMapping.getCacheTime());
			}
		}
	}


	
	
	
	
}
