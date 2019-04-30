package light.jdbc.db.adapter;

import java.util.List;

import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.enums.IdType;
import light.jdbc.query.Field;
import light.jdbc.query.Query;

public class InsertDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public InsertDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}

	@Override
	public Object cacheAdapter() {
		return dbAdapter();
	}

	@Override
	public Object dbAdapter() {
		List<Object> params = query.getParams();
		String sql = query.getQueryBuilder().buildInsertSQL();
		if(log.isInfoEnabled()) {
			log.info("==> execute [sql={},params={}]",sql,params);
		}
		//自增id 
		if(tableMapping.getIdType() == IdType.AUTO) {
			return dbOperation.instertIdAuto(tableMapping.getIdClassType(), sql, params.toArray());
		}
		dbOperation.update(sql, params.toArray());
		Field idField = query.getFieldMap().get(tableMapping.getPrimaryKey());
		return idField.getValue();
	}

	@Override
	public ExecuteType executeType() {
		// TODO Auto-generated method stub
		return ExecuteType.INSERT;
	}

	
	

	

}
