package light.jdbc.table;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * 属性映射
 * @author hanjiang.Yue
 */
public class Property {
	
	public static final int TYPE_BEAN = 0;
	
	public static final int TYPE_MAP = 1;

	
	private PropertyDescriptor property;
	
	/**
	 * 实体字段
	 */
	private String fieldName;
	
	/**
	 * 表字段
	 */
	private String columnName;
	
	/**
	 * 属性类型
	 */
	private int type = TYPE_BEAN;
	
	private Class<?> classType;
	
	/**
	 *  
	 * @author hanjiang.Yue
	 * @param target
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getValue(Object target) {
		if(type == TYPE_MAP) {
			Map map = (Map) target;
			return (T) map.get(fieldName);
		}
		try {
			return (T) property.getReadMethod().invoke(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setValue(Object target,Object value) {
		if(type == TYPE_MAP) {
			Map map = (Map) target;
			map.put(fieldName, value);
			return;
		}
		try {
			property.getWriteMethod().invoke(target,value);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	

	public PropertyDescriptor getProperty() {
		return property;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public Property(PropertyDescriptor property, String fieldName, String columnName) {
		super();
		this.property = property;
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.classType = property.getPropertyType();
		
	}
	
	

	public Property(String fieldName, String columnName, int type, Class<?> classType) {
		super();
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.type = type;
		this.classType = classType;
	}

	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}
	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Property() {
		super();
	}
	
	
}
