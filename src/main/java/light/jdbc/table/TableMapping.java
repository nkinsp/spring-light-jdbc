package light.jdbc.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import light.jdbc.activerecord.ConfigColumn;
import light.jdbc.activerecord.MapActiveRecord;
import light.jdbc.annotation.Column;
import light.jdbc.annotation.KeySequence;
import light.jdbc.annotation.Table;
import light.jdbc.enums.IdType;
import light.jdbc.exception.NotFoundTableException;
import light.jdbc.util.ClassUtils;
import light.jdbc.util.StrUtils;

/**
 * 表映射
 * @author hanjiang.Yue
 * @param <T>
 */
public class TableMapping<T> {

	/**
	 * 对应的实体
	 */
	private Class<T> tableClass;
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 主键类型
	 */
	private IdType idType;
	/**
	 * 主键名称
	 */
	private String primaryKey;
	/**
	 * 实体字段对表字段的映射
	 */
	private final Map<String, Property> fieldToColMap;
	
	/**
	 * 表字段对实体字段的映射
	 */
	private final Map<String, Property> colToFieldMap;
	/**
	 * 表所有的列名
	 */
	private final List<String> columns;
	
	private boolean cache ;
	
	private long cacheTime;
	
	//主键属性
	private Property idProperty;
	
	private String keySequence;
	
	/**
	 * @param tableClass
	 */
	public TableMapping(Class<T> tableClass) {
		this.tableClass = tableClass;
		this.columns = new ArrayList<>();
		this.fieldToColMap = new LinkedHashMap<>();
		this.colToFieldMap = new LinkedHashMap<>();
		//加载信息
		init();
	}
	
	
	/**
	 * 初始化加载...
	 * @author hanjiang.Yue
	 */
	@SuppressWarnings("rawtypes")
	private void init() {
		Table table = tableClass.getAnnotation(Table.class);
		//没有@Table 注解
		if(table == null) {
			throw new NotFoundTableException("Not Found Table Class"+this.tableClass);
		}
		this.tableName = table.name();
		this.primaryKey = table.id();
		this.idType = table.idType();
		this.cache = table.cache();
		this.cacheTime = table.cacheTime();
		//默认使用类名
		if(StrUtils.isEmpty(tableName)) {
			this.tableName = StrUtils.humpToLine(tableClass.getSimpleName());
		}
		KeySequence sequence = tableClass.getAnnotation(KeySequence.class);
		if(sequence != null) {
			this.keySequence = sequence.value();
		}
		//实体是一个Map
		if(MapActiveRecord.class.isAssignableFrom(tableClass)) {
			MapActiveRecord record = (MapActiveRecord) ClassUtils.newInstance(tableClass);
			//加载字段
			record.confingColumns(new ConfigColumn() {
				@Override
				public ConfigColumn add(String name, Class<?> classType) {
					addColumn(name, classType, Property.TYPE_MAP);
					return this;
				}
			});
			
			Property property = getPropertyByColumn(primaryKey);
			if(property == null) {
				property = new Property(primaryKey, primaryKey, Property.TYPE_MAP, record.idClass());
			}
			
			//主键信息
			setIdProperty(property);
			
		}else {
			//加载字段信息
			ClassUtils.getPropertyDescriptors(tableClass).forEach(pd->{
				//获取字段信息
				String fieldName = pd.getName(); 
				Field field = getField(fieldName);
				Column column = field.getDeclaredAnnotation(Column.class);
				//表字段
				String colName = column == null?StrUtils.humpToLine(fieldName):column.value();
				//Property 对象
				addColumn(new Property(pd, fieldName, colName));
			});
			//主键信息
			setIdProperty(getPropertyByColumn(primaryKey));
		}

	}

	public TableMapping<T> addColumn(Property property){
		this.columns.add(property.getColumnName());
		this.colToFieldMap.put(property.getColumnName(), property);
		this.fieldToColMap.put(property.getFieldName(), property);
		return this;
	}
	
	public TableMapping<T> addColumn(String name,Class<?> typeClass,int propertyType){
		Property property  = new Property();
		property.setClassType(typeClass);
		property.setColumnName(name);
		property.setFieldName(name);
		property.setType(propertyType);
		return addColumn(property);
	}
	
	/**
	 * 获取字段信息 
	 * @author hanjiang.Yue
	 * @param name
	 * @return
	 */
	private Field getField(String name) {
		try {
			return tableClass.getDeclaredField(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过列名获取
	 * @author hanjiang.Yue
	 * @param columnName
	 * @return
	 */
	public Property getPropertyByColumn(String  columnName) {
		return this.colToFieldMap.get(columnName);
	}
	
	/**
	 * 通过字段名获取
	 * @author hanjiang.Yue
	 * @param fieldName
	 * @return
	 */
	public Property getPropertyByField(String fieldName) {
		return this.fieldToColMap.get(fieldName);
	}
	
	public String getColumnName(String fieldName) {
		Property property = getPropertyByField(fieldName);
		if(property != null) {
			return property.getColumnName();
		}
		return fieldName;
	}
	
	public Class<T> getTableClass() {
		return tableClass;
	}


	public String getTableName() {
		return tableName;
	}


	public IdType getIdType() {
		return idType;
	}


	public String getPrimaryKey() {
		return primaryKey;
	}


	public List<String> getColumns() {
		return columns;
	}

	
	public Collection<Property> getPropertys(){
		return this.colToFieldMap.values();
	}


	public boolean isCache() {
		return cache;
	}


	public void setCache(boolean cache) {
		this.cache = cache;
	}


	public long getCacheTime() {
		return cacheTime;
	}


	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}
	
	public Class<?> getIdClassType(){
		return idProperty.getClassType();
	}


	public Property getIdProperty() {
		return idProperty;
	}


	public void setIdProperty(Property idProperty) {
		this.idProperty = idProperty;
	}


	public String getKeySequence() {
		return keySequence;
	}


	public void setKeySequence(String keySequence) {
		this.keySequence = keySequence;
	}
}
