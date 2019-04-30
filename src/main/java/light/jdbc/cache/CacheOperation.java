package light.jdbc.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 缓存操作
 * 
 * @author hanjiang.Yue
 *
 */
public interface CacheOperation {

	<T> T get(Class<T> typeClass, String cacheKey);
	
	<T> List<T> list(Class<T>  type,String cacheKey);
	
	<T> List<T> list(Class<T>  type,Collection<String> keys);

	void put(String cacheKey, Object value);
	
	void put(String cacheKey,long expiryTime,Object value);
	
	void put(Map<String,Object> elements,long expiryTime);
	
	void delete(String cacheKey);
	
	void delete(Collection<String> cacheKeys);
	

}
