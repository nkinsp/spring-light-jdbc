package light.jdbc.query.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import light.jdbc.code.DbConfig;
import light.jdbc.code.DbContext;
import light.jdbc.db.DbOperationAdapter;
import light.jdbc.enums.FieldType;
import light.jdbc.enums.IdType;
import light.jdbc.query.Field;
import light.jdbc.query.Query;
import light.jdbc.query.QueryBuilder;
import light.jdbc.table.TableMapping;
import light.jdbc.util.EntityUtils;

/**
 * Query
 * @author hanjiang.Yue
 * @param <T>
 */
public abstract class AbstractSupportQueryImpl<T> implements Query<T>,QueryBuilder{

	/**
	 * sql builder
	 */
	private  StringBuilder sqlBuilder;
	
	/**
	 * 参数
	 */
	private  List<Object> params;
	
	/**
	 * 查询的表 字段
	 */
	private  List<String> selectColumns;
	
	/**
	 * 查询排除的字段
	 */
	private  Set<String> excludeColumns;

	/**
	 * 更新或者新增的字段
	 */
	private Map<String, Field> fieldMap;
	
	/**
	 * 批量更新或者添加的字段
	 */
	private List<Map<String, Field>> fieldBatch;

	/**
	 * 表映射
	 */
	private TableMapping<T> tableMapping;
	
	/**
	 * 缓存时间 单位秒
	 */
	private long cacheTime;
	
	/**
	 * 缓存的key
	 */
	private String cacheKey;
	
	/**
	 * 是否缓存
	 */
	private boolean cache = false;
	
	/**
	 * 数据配置
	 */
	private DbConfig config;
	
	private DbContext context;
	
	/**
	 * @return the fieldBatch
	 */
	public List<Map<String, Field>> getFieldBatch() {
		return fieldBatch;
	}
	
	@Override
	public TableMapping<T> getTableMapping() {
		return this.tableMapping;
	}

	@Override
	public Query<T> condition(String condition) {
		this.sqlBuilder.append(condition);
		return this;
	}

	@Override
	public Query<T> addParam(Object param) {
		this.params.add(param);
		return this;
	}

	@Override
	public <R> Query<T> and(String sql, Class<R> tableClass, Consumer<Query<R>> consumer) {
		Query<R> query = DbContext.createQuery(tableClass, config);
		consumer.accept(query);
		this.sqlBuilder
			   .append(" (")
			   .append(query.getQueryBuilder().buildSelectSQL())
			   .append(") ");
		this.params.addAll(query.getParams());
		return this;
	}


	@Override
	public List<Object> getParams() {
		// TODO Auto-generated method stub
		return this.params;
	}


	@Override
	public Query<T> select(List<String> fields) {
		this.selectColumns.addAll(fields);
		return this;
	}

	@Override
	public Query<T> selectExcludes(String... fields) {
		this.excludeColumns.addAll(Arrays.asList(fields));
		return this;
	}

	@Override
	public Query<T> set(Map<String, Object> fields) {
		fields.forEach((k,v)->set(tableMapping.getColumnName(k), v));
		return this;
	}

	@Override
	public Query<T> set(String field, Object value) {
		String columnName = tableMapping.getColumnName(field);
		fieldMap.put(columnName, new Field(columnName, value));
		return this;
	}


	@Override
	public List<String> getColumns() {
		List<String> columns = new ArrayList<>();
		columns.addAll(this.selectColumns);
		if (columns.size() == 0) {
			columns.addAll(this.tableMapping.getColumns());
		}
		Set<String> colSet = columns.stream().map(col -> col).collect(Collectors.toSet());
		for (String excol : this.excludeColumns) {
			colSet.remove(excol);
		}
		return colSet.stream().map(col -> col).collect(Collectors.toList());
	}

	@Override
	public String getConditions() {
		return this.sqlBuilder.toString();
	}


	@Override
	public boolean isCache() {
		return cache;
	}

	@Override
	public String getCacheKey() {
		return this.cacheKey;
	}

	@Override
	public Query<T> cache(String key, long second) {
		this.cacheKey = config.getCacheKeyGenerated().generated(tableMapping, key);
		this.cache = true;
		this.cacheTime = second;
		return this;
	}

	@Override
	public long getCacheTime() {
		return this.cacheTime;
	}

	@Override
	public Query<T> batch(List<Map<String, Object>> batch) {
		 batch.forEach(data->{
			 Map<String, Field> map = new LinkedHashMap<>();
			 data.forEach((key,value)->{
				 String columnName = tableMapping.getColumnName(key);
				 map.put(columnName, new Field(columnName, value));
			 });
			 this.fieldBatch.add(map);
		 });
		 return this;
	}

	@Override
	public String getCacheKey(String value) {
		return config.getCacheKeyGenerated().generated(tableMapping, value);
	}

	@Override
	public List<Map<String, Field>> getFiledBatch() {
		return this.fieldBatch;
	}

	@Override
	public Query<T> setSQLStr(String field, String sql, Object... params) {
		this.fieldMap.put(field, new Field(field, sql, FieldType.SQL_STRING));
		addParams(params);
		return this;
	}

	@Override
	public Query<T> set(T t) {
		return set( EntityUtils.entityToMap(t));
	}

	@Override
	public Map<String, Field> getFieldMap() {
		return this.fieldMap;
	}

	public AbstractSupportQueryImpl(TableMapping<T> tableMapping,DbConfig config) {
		this.sqlBuilder = new StringBuilder();
		this.params = new ArrayList<>();
		this.selectColumns = new ArrayList<>();
		this.excludeColumns = new HashSet<>();
		this.fieldMap = new LinkedHashMap<>();
		this.tableMapping = tableMapping;
		this.fieldMap = new LinkedHashMap<>();
		this.fieldBatch = new ArrayList<>();
		this.config = config;
	}
	
	public AbstractSupportQueryImpl(TableMapping<T> tableMapping,DbContext context) {
		this(tableMapping,context.getConfig());
		this.context = context;
	}

	@Override
	public String buildDeleteSQL() {
		return  new StringBuilder()
				.append("DELETE ")
				.append("FROM ")
				.append(tableMapping.getTableName())
				.append(" ")
			    .append(getConditions())
			    .toString();
	}

	
	private String getInsertSQL(Map<String, String> colMap ){
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ");
		builder.append(tableMapping.getTableName());
		builder.append(" (");
		StringBuilder values = new StringBuilder(") VALUES (");
		int i = 0;
		for (Entry<String, String> en : colMap.entrySet()) {
			if (i > 0) {
				builder.append(",");
				values.append(",");
			}
			builder.append(en.getKey());
			values.append(en.getValue());
			i++;
		}
		values.append(")");
		return builder.append(values.toString()).toString();
	}
	
	@Override
	public String buildInsertSQL() {
		String pk = tableMapping.getPrimaryKey();
		List<Field> fields = null;
		if(!getFieldMap().containsKey(pk) ) {
			fields = new ArrayList<>();
			Field field = config.getPrimaryKeyGenerated().generated(tableMapping);
			if(field != null) {
				fields.add(field);
			}
			fields.addAll(getFieldMap().values());
			getFieldMap().put(pk,field);
		}else {
			fields = new ArrayList<>(getFieldMap().values());
		}
		Map<String, String> colMap = new LinkedHashMap<>(fields.size());
		fields.forEach(field->{
			if(field.isTableField()) {
				colMap.put(field.getName(), "?");
				addParam(field.getValue());
			}else {
				colMap.put(field.getName(), field.getValue().toString());
			}
		});
		return getInsertSQL(colMap);
	}
	
	
	
	@Override
	public String buildBatchInsertSQL() {
		String pk = tableMapping.getPrimaryKey();
		Map<String, String> colMap = new LinkedHashMap<>();
		List<Map<String, Field>> batch = getFieldBatch();
		for (int i = 0; i < batch.size(); i++) {
			Map<String, Field> dataMap = batch.get(i);
			List<Object> param = new ArrayList<>();
			if (tableMapping.getIdType() != IdType.AUTO) {
				// 设置主键
				Field id = dataMap.containsKey(pk)?dataMap.get(pk):config.getPrimaryKeyGenerated().generated(tableMapping);
				dataMap.put(pk, id);
				if (id.isTableField()) {
					param.add(id.getValue());
				}
			}
			dataMap.forEach((key,field)->{
				if(!key.equals(pk)  && field.isTableField()) {
					param.add(field.getValue());
				}
			});
			addParam(param.toArray());
			if (i == 0) {
				tableMapping.getColumns().forEach(col -> {
					Field field = dataMap.get(col);
					if (field != null) {
						if (field.isTableField()) {
							colMap.put(field.getName(), "?");
						} else {
							colMap.put(field.getName(), field.getValue().toString());
						}
					}
				});
			}
		}
		return getInsertSQL(colMap);
	}
	
	private String getUpdateSQL(Map<String, String> colMap) {
		// sql
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE ");
		builder.append(tableMapping.getTableName());
		builder.append(" SET ");
		int i = 0;
		for (Entry<String, String> en : colMap.entrySet()) {
			if (i > 0) {
				builder.append(" , ");
			}
			builder.append(en.getKey());
			builder.append(" = ");
			builder.append(en.getValue());
			i++;
		}
		builder.append(" ");
		builder.append(getConditions());
		return builder.toString();
	}

	@Override
	public String buildUpdateSQL() {
		Map<String, String> colMap = new LinkedHashMap<>();
		List<Object> newParams = new ArrayList<>();
		getFieldMap().forEach((key,field)->{
			if(field.getFieldType() == FieldType.TABLE_FIELD) {
				colMap.put(key, "?");
				newParams.add(field.getValue());
			}else {
				colMap.put(key, field.getValue().toString());
			}
		});
		//条件参数
		getParams().forEach(param->newParams.add(param)); 
		//清除原有的参数
		this.params.clear();
		//设置新的参数
		this.params.addAll(newParams);
		return getUpdateSQL(colMap);
	}

	@Override
	public String buildBatchUpdateSQL() {
		Map<String, String> colMap = new LinkedHashMap<>();
		for (int i = 0; i < fieldBatch.size(); i++) {
			Map<String, Field> objectMap = fieldBatch.get(i);
			Object id = objectMap.get(tableMapping.getPrimaryKey());
			if(id == null) {
				throw new RuntimeException("Not Found ID "+objectMap);
			}
			List<Object> param = new ArrayList<>();
			for (Entry<String, Field> en : objectMap.entrySet()) {
				//主键
				if(en.getKey().equals(tableMapping.getPrimaryKey())) {
					continue;
				}
				if(i == 0) {
					colMap.put(en.getKey(),"?");
				}
				param.add(en.getValue().getValue());
			}
			//添加主键值
			param.add(param);
			//添加参数
			this.params.add(param.toArray());
		}
		//where 条件
		where(tableMapping.getPrimaryKey() + " = ? ");
		return getUpdateSQL(colMap);
	}
	
	@Override
	public String buildSelectSQL() {
		List<String> cols = new ArrayList<>();
		if(this.selectColumns.size() == 0) {
			cols.addAll(this.tableMapping.getColumns());
		}else {
			cols.addAll(this.selectColumns);
		}
		List<String> colList = cols.stream().filter(col->!this.excludeColumns.contains(col)).collect(Collectors.toList());
		return buildSelectSQL(colList);
	}

	@Override
	public String buildSelectSQL(List<String> cols) {
		StringBuilder sql = new StringBuilder()
				.append("SELECT ");
		if(cols.size() == 0) {
			sql.append("*");
		}
		for (int i = 0; i < cols.size(); i++) {
			if(i > 0)
				sql.append(",");
			sql.append(cols.get(i));
		}
		sql.append(" FROM ");
		sql.append(tableMapping.getTableName());
		sql.append(" ");
		sql.append(getConditions());
		return sql.toString();
	}
	
	@Override
	public QueryBuilder getQueryBuilder() {
		return this;
	}
	
	
	@Override
	public <R> Query<T> where(String condition,Class<R> tableClass, Consumer<Query<R>> where) {
		Query<R> tableQuery = DbContext.createQuery(tableClass, config);
		where.accept(tableQuery);
		where(condition+" ("+tableQuery.getQueryBuilder().buildSelectSQL(), tableQuery.getParams().toArray());
		return this;
	}
	
	@Override
	public <R> Query<T> in(String field, Class<R> tableClass, Consumer<Query<R>> query) {
		Query<R> tableQuery = DbContext.createQuery(tableClass, config);
		query.accept(tableQuery);
		condition(field);
		condition(" IN (");
		condition(tableQuery.getQueryBuilder().buildSelectSQL());
		condition(") ");
		addParams(tableQuery.getParams().toArray());
		return this;
	}
	
	@Override
	public void release() {
		this.excludeColumns = null;
		this.sqlBuilder = null;
		this.fieldBatch = null;
		this.fieldMap = null;
		this.params = null;
	}


	@Override
	public DbContext getDbContext() {
		return this.context;
	}
	
	@Override
	public String buildPagingSQL(int pageNo, int pageSize) {
		limit((pageNo-1)*pageSize, pageSize);
		return buildSelectSQL();
	}
	
}
