package light.jdbc.cache.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import light.jdbc.cache.CacheOperation;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;

public class J2CacheOperation implements CacheOperation{

	private String region = "default";
	
	private CacheChannel cacheChannel;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> typeClass, String cacheKey) {
		CacheObject object = cacheChannel.get(region,cacheKey);
		return (T) object.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> list(Class<T> typeClass, String cacheKey) {
		return (List<T>) get(typeClass, cacheKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> list(Class<T> type, Collection<String> keys) {
		return cacheChannel.get(region, keys)
				.values()
				.stream()
				.filter(cache->cache.getValue()!=null)
				.map(cacheObject->(T)cacheObject.getValue())
				.collect(Collectors.toList());
	}

	@Override
	public void put(String cacheKey, Object value) {
		cacheChannel.set(region, cacheKey, value);
	}

	@Override
	public void put(String cacheKey, long expiryTime, Object value) {
		cacheChannel.set(region, cacheKey, value, expiryTime);
	}


	@Override
	public void delete(String cacheKey) {
		cacheChannel.evict(region, cacheKey);
	}

	@Override
	public void delete(Collection<String> cacheKeys) {
		String[] keys = new String[cacheKeys.size()];
		cacheChannel.evict(region,cacheKeys.toArray(keys));
	}


	/**
	 * @return the cacheChannel
	 */
	public CacheChannel getCacheChannel() {
		return cacheChannel;
	}

	/**
	 * @param cacheChannel the cacheChannel to set
	 */
	public void setCacheChannel(CacheChannel cacheChannel) {
		this.cacheChannel = cacheChannel;
	}

	public J2CacheOperation(CacheChannel cacheChannel) {
		super();
		this.cacheChannel = cacheChannel;
	}

	@Override
	public void put(Map<String, Object> elements, long expiryTime) {
		cacheChannel.set(region, elements, expiryTime);
	}

	
	

}
