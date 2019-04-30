package example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import example.dao.UserDao;
import example.model.User;
import light.jdbc.code.DbContext;

public class App {


	@Autowired
	private DbContext dbContext;
	
	public void test() {
		
		dbContext.table(User.class).find(100);
	}
}
