package light.jdbc.db.adapter;

import light.jdbc.code.DbContext;
import light.jdbc.enums.ExecuteType;
import light.jdbc.query.Query;

public class DbOperationAdapterProxy<T> extends AbstractDbOperationAdapter<T>{

	public DbOperationAdapterProxy(DbContext context, Query<T> query) {
		super(context, query);
	}


	private AbstractDbOperationAdapter<T> dbOperationAdapter;
	
	
	@Override
	public <R> R adapter() {
		return dbOperationAdapter.adapter();
	}


	@Override
	public Object cacheAdapter() {
		return null;
	}


	@Override
	public Object dbAdapter() {
		return dbOperationAdapter.dbAdapter();
	}


	@Override
	public ExecuteType executeType() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
