package light.jdbc.activerecord;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import light.jdbc.util.CastUtils;

public abstract class MapActiveRecord<M,Id> extends ActiveRecord<M, Id> implements Map<String,Object>{

	private Map<String, Object> data = new LinkedHashMap<>();
	
	public void confingColumns(ConfigColumn config) {
		
	}
	
	

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return data.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return data.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T) data.get(name);
	}
	
	public <T> T get(String name,Class<T> typeClass) {
		return CastUtils.cast(get(name), typeClass);
	}
	
	public Integer getToInteger(String name) {
		return get(name,Integer.class);
	}
	
	public Long getToLong(String name) {
		return get(name,Long.class);
	}
	public Date getToDate(String name) {
		return get(name,Date.class);
	}
	
	public Boolean getToBoolean(String name) {
		return get(name,Boolean.class);
	}
	
	@SuppressWarnings("unchecked")
	private M thisObject() {
		return (M) this;
	}
	
	public M set(String name,Object value) {
		put(name, value);
		return thisObject();
	}

	@Override
	public Object put(String key, Object value) {
		// TODO Auto-generated method stub
		return data.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return data.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		data.putAll(m);
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public Set<String> keySet() {
		return data.keySet();
	}

	@Override
	public Collection<Object> values() {
		return data.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return data.entrySet();
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
	
	/**
	 * 创建 (k,v,k,v)
	 * @param objects
	 * @return
	 */
	public static Map<String, Object> create(Object...objects) {
		Map<String, Object> maps = new LinkedHashMap<>(objects.length/2);
		for (int i = 0; i < objects.length; i++) {
			if(i%2 == 1) {
				maps.put(String.valueOf(objects[i-1]), objects[i]);
			}
		}
		return maps;
	}
	
	@Override
	public Id save() {
		return super.save(data);
	}
	
	@Override
	public int update() {
		return super.update(data);
	}
}
