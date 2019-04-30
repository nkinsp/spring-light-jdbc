package light.jdbc.db.adapter;

import java.util.List;

import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;

/**
 * 批量添加
 * @author hanjiangyue
 *
 * @param <T>
 */
public class InsertBatchDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public InsertBatchDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}
	
	

	@Override
	public Object dbAdapter() {
		String sql = query.getQueryBuilder().buildBatchInsertSQL();
		List<Object[]> params = query.getArrayParams();
		log.info("==> execute [sql={}]",sql);
		return dbOperation.batchUpdate(sql, params);
	}



	@Override
	public Object cacheAdapter() {
		
		return null;
	}



	@Override
	public ExecuteType executeType() {
		// TODO Auto-generated method stub
		return ExecuteType.INSERT_BATCH;
	}




	

}
