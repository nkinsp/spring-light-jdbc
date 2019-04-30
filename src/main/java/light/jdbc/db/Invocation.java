package light.jdbc.db;

import light.jdbc.code.DbContext;
import light.jdbc.query.Query;

public class Invocation {

	private DbContext context;
	
	private Class<DbOperationAdapter> adapterClass;
	
	private Query<?> query;

	private DbOperationAdapter dbOperationAdapter;
	
	public Object proceed() {
		return dbOperationAdapter.adapter();
	}
	
	/**
	 * @return the context
	 */
	public DbContext getContext() {
		return context;
	}

	/**
	 * @return the adapterClass
	 */
	public Class<DbOperationAdapter> getAdapterClass() {
		return adapterClass;
	}

	/**
	 * @return the query
	 */
	public Query<?> getQuery() {
		return query;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(DbContext context) {
		this.context = context;
	}

	/**
	 * @param adapterClass the adapterClass to set
	 */
	public void setAdapterClass(Class<DbOperationAdapter> adapterClass) {
		this.adapterClass = adapterClass;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(Query<?> query) {
		this.query = query;
	}
	
	
	
	
	
}
