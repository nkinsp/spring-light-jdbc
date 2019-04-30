package light.jdbc.cache.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSON;

import light.jdbc.cache.CacheOperation;
import light.jdbc.util.StrUtils;

public class RedisCacheOperation implements CacheOperation{

	private StringRedisTemplate redisTemplate;
	
	@Override
	public <T> T get(Class<T> typeClass, String cacheKey) {
//		redisTemplate.opsForValue().
		String vlaue = redisTemplate.opsForValue().get(cacheKey);
		if(!StrUtils.isEmpty(vlaue)) {
			return JSON.parseObject(vlaue, typeClass);
		}
		return null;
	}

	@Override
	public void put(String cacheKey, Object value) {
		redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(value));
	}

	public String serialize(Object value) {
		return JSON.toJSONString(value);
	}

	public <T> T deserialize(Class<T> classType, String value) {
		 	return JSON.parseObject(value, classType);
	}

	@Override
	public void put(String cacheKey, long expiryTime, Object value) {
		redisTemplate.opsForValue().set(cacheKey, serialize(value), expiryTime,TimeUnit.SECONDS);
		
	}

	@Override
	public void delete(String cacheKey) {
		redisTemplate.delete(cacheKey);
	}

	@Override
	public void delete(Collection<String> cacheKeys) {
		redisTemplate.delete(cacheKeys);
	}


	@Override
	public <T> List<T> list(Class<T> type, Collection<String> keys) {
		return redisTemplate.
				opsForValue().
				multiGet(keys).
				stream().
				filter(data->!StrUtils.isEmpty(data)).
				map(data->deserialize(type,data)).
				collect(Collectors.toList());
	}

	@Override
	public <T> List<T> list(Class<T> type, String cacheKey) {
		String value = redisTemplate.opsForValue().get(cacheKey);
		if(!StrUtils.isEmpty(value)) { 
			 List<T> list = JSON.parseArray(value, type);
			 return list == null?new ArrayList<>():list;
		}
		return new ArrayList<>();
	}


	@Override
	public void put(Map<String, Object> elements, long expiryTime) {
		
		redisTemplate.executePipelined(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				StringRedisSerializer serializer = new StringRedisSerializer();
				elements.forEach((key,value)->{
					connection.setEx(serializer.serialize(key), expiryTime,serializer.serialize(value.toString()));
				});
				return null;
			}
		});
		
	}


	public RedisCacheOperation(StringRedisTemplate redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}

	
	
}
