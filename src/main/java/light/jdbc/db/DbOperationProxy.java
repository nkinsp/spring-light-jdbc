package light.jdbc.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DbOperationProxy implements InvocationHandler{
	
	private Logger log = 	LoggerFactory.getLogger(getClass());

	private DbOperation dbOperation;
	

	public DbOperationProxy(DbOperation dbOperation) {
		super();
		this.dbOperation = dbOperation;
	}



	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		log.info("==> execute [method={}] ",method.getName());
		log.info("==> params [params={}]: ",args);
		try {
			return method.invoke(dbOperation, args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}

	public DbOperation getProxy() {
		return (DbOperation) Proxy.newProxyInstance(dbOperation.getClass().getClassLoader(), dbOperation.getClass().getInterfaces(), this);
	}
	
	
	
	
	
	

}
