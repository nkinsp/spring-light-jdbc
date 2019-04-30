package light.jdbc.db.springjdbc;

import org.springframework.beans.factory.annotation.Autowired;

import light.jdbc.code.DbContext;
import light.jdbc.code.DbRepositoryTemplate;

public abstract class SpringJdbcTemplate<M,Id> extends DbRepositoryTemplate<M, Id>{
	
	@Autowired
	 public DbContext dbContext;
	
	@Override
	public DbContext dbContext() {
		return dbContext;
	}
}
