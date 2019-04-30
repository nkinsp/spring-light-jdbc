package light.jdbc.db.adapter;

import java.sql.ResultSet;
import java.util.function.Function;

import light.jdbc.code.DbContext;
import light.jdbc.db.DbOperationAdapter;

public class ExecuteSQLDbOperation<T> implements DbOperationAdapter{
	
	public DbContext  context;
	
	private String sql;
	
	private Object[] params;
	
	private Function<ResultSet,T> fun;
	
	public ExecuteSQLDbOperation(DbContext context, String sql, Function<ResultSet,T> fun,Object...params) {
		super();
		this.context = context;
		this.sql = sql;
		this.fun = fun;
		this.params = params;
	}



	@Override
	public <R> R adapter() {
		
		 context.getDbOperation().execute(sql, fun, params);
		 return null;
	}

}
