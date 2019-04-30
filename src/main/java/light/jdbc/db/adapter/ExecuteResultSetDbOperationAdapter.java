package light.jdbc.db.adapter;

import java.sql.ResultSet;
import java.util.function.Function;

import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;

public class ExecuteResultSetDbOperationAdapter<T,R> extends AbstractDbOperationAdapter<T>{

	private Function<ResultSet, R> callback;
	
	public ExecuteResultSetDbOperationAdapter(DbContext context, Query<T> query) {
		super(context, query);
	}
	

	public ExecuteResultSetDbOperationAdapter(DbContext context, Query<T> query, Function<ResultSet, R> callback) {
		super(context, query);
		this.callback = callback;
	}




	@Override
	public Object cacheAdapter() {
		return null;
	}

	@Override
	public Object dbAdapter() {
		
		return null;
		
	}


	@Override
	public ExecuteType executeType() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
