package light.jdbc.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class EntityUtils {

	private static Map<String, Object> getEntityToMap(Object entity){
		Map<String, Object> entityMap = new LinkedHashMap<>();
		ClassUtils.getPropertyDescriptors(entity.getClass()).forEach(prop->{
			try {
				 Object value = prop.getReadMethod().invoke(entity);
				 if(value != null) {
						entityMap.put(prop.getName(),value);
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return entityMap;
	}
	
	/**
	 * 把实体转换为Map
	 * @author hanjiang.Yue
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> entityToMap(Object entity){
		if(entity == null) {
			return null;
		}
		if(Map.class.isAssignableFrom(entity.getClass())) {
			System.out.println(entity+"sss");
			return (Map<String, Object>) entity;
		}
		return getEntityToMap(entity);
	}
	
	/**
	 * @author hanjiang.Yue
	 * @param entitys
	 * @return
	 */
	public static <T> List<Map<String, Object>> entityListToMapList(List<T> entitys){
		if(entitys == null || entitys.size() == 0) {
			return new ArrayList<>(0);
		}
		return entitys.stream().map(entity->entityToMap(entity)).collect(Collectors.toList());
	}
}
