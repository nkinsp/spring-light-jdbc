package jdbc_fplus.jdbc_fplus.model;


import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;

import light.jdbc.activerecord.ActiveRecord;
import light.jdbc.activerecord.ConfigColumn;
import light.jdbc.activerecord.MapActiveRecord;
import light.jdbc.annotation.Table;

@Table
public class User extends MapActiveRecord<User, Integer>{

	
	@Override
	public void confingColumns(ConfigColumn config) {
		
		//config.a
	}
	
	
	public static final User DAO = new User();
	
	
	@Cacheable("user_in")
	public void  UserInfo (String id) {
	
		new User().delete(del->{
			//del.where("", params)
			del.cache();
		});
	}
	
	
	public static void main(String[] args) {
		
		
	
//		new User().find(1000).u
		
	}
	
	
}
