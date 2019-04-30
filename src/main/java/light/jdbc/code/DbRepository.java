package light.jdbc.code;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import light.jdbc.db.DbOperationAdapter;
import light.jdbc.db.adapter.DeleteBatchDbOperationAdapter;
import light.jdbc.db.adapter.DeleteDbOperationAdapter;
import light.jdbc.db.adapter.FindBeanListDbOperationAdapter;
import light.jdbc.db.adapter.FindDbOperationAdapter;
import light.jdbc.db.adapter.FindListByIdsDbOperationAdapter;
import light.jdbc.db.adapter.FindMapDbOperationAdapter;
import light.jdbc.db.adapter.FindMapListDbOperationAdapter;
import light.jdbc.db.adapter.FindObjectDbOperationAdapter;
import light.jdbc.db.adapter.FindObjectListDbOperationAdapter;
import light.jdbc.db.adapter.FindPageDbOperationAdapter;
import light.jdbc.db.adapter.InsertBatchDbOperationAdapter;
import light.jdbc.db.adapter.InsertDbOperationAdapter;
import light.jdbc.db.adapter.UpdateBatchDbOperationAdapter;
import light.jdbc.db.adapter.UpdateDbOperationAdapter;
import light.jdbc.query.Query;
import light.jdbc.result.Page;
import light.jdbc.table.TableMapping;
import light.jdbc.util.EntityUtils;
import light.jdbc.util.StrUtils;

/** 
 * @author hanjiang.Yue
 * @param <M>
 * @param <Id>
 */
public interface DbRepository<M,Id> {

/**
	 * 实体类型
	 * @author hanjiang.Yue
	 * @return
	 */
	Class<M> modelClass();
	
	/**
	 * 主键类型
	 * @author hanjiang.Yue
	 * @return
	 */
	Class<Id> idClass();
	
	/**
	 * 获取dbContext 对象
	 * @author hanjiang.Yue
	 * @return
	 */
	DbContext dbContext();
	
	/**
	 * 创建query执行对象
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default Query<M> createQuery(Consumer<Query<M>> consumer){
		Query<M> query = dbContext().createQuery(modelClass());
		consumer.accept(query);
		return query;
	}
	
	/**
	 * 执行存储过程
	 * @author hanjiang.Yue
	 * @param call
	 * @param callableStatementConsumer
	 * @param resultSetFun
	 * @return
	 */
	default <T> T call(String call,Consumer<CallableStatement> callableStatementConsumer,Function<ResultSet,T> resultSetFun) {
		return dbContext().getDbOperation().prepareCall(call, callableStatementConsumer, resultSetFun);
	}
	
	/**
	 * 执行
	 * @author hanjiang.Yue
	 * @param adapter
	 * @return
	 */
	default <T> T execute(DbOperationAdapter adapter) {
		try {
			return adapter.adapter();
		} finally {
			adapter = null;
		}
	}
	
	/**
	 * 通过id获取
	 * @author hanjiang.Yue
	 * @param id
	 * @return
	 */
	default M find(Id id) {
		return execute(new FindDbOperationAdapter<M>(
				dbContext(),
				createQuery(q->q.where().idEq(id)))
		);
	}
	
//	default M find(String sql,Function<ResultSet,M> consumer,Object...params) {
//		execute(new ExecuteSQLDbOperation<>(getDbContext(), sql, fun, params))
//	}
	
	/**
	 * Find Map 
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default Map<String, Object> findMap(Consumer<Query<M>> consumer){
		return execute(new FindMapDbOperationAdapter<>(dbContext(), createQuery(consumer)));
	}
	
	/**
	 * Find List<Map>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<Map<String, Object>> findMapList(Consumer<Query<M>> consumer){
		return execute(new FindMapListDbOperationAdapter<>(dbContext(),createQuery(consumer)));
	}
	
	/**
	 * ids 主键
	 * @author hanjiang.Yue
	 * @param ids
	 * @return
	 */
	default List<M> findList(@SuppressWarnings("unchecked") Id...ids) {
		return findList(Arrays.asList(ids));
	}
	
	/**
	 * List id 主键
	 * @author hanjiang.Yue
	 * @param ids
	 * @return
	 */
	default List<M> findList(List<Id> ids) {
		if(ids == null || ids.size() == 0) {
			return Arrays.asList();
		}
		Query<M> query = createQuery(q->q.where().idIn(ids.toArray()));
		return execute(new FindListByIdsDbOperationAdapter<M>(dbContext(), query));
	}
	
	/**
	 * 获取所有数据
	 * @author hanjiang.Yue
	 * @return
	 */
	default List<M> findAll(){
		return findList(query->{});
	}
	
	/**
	 * 分页查询
	 * @author hanjiang.Yue
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @return
	 */
	default List<M> findList(Integer pageNo,Integer pageSize,Consumer<Query<M>> query){
		Query<M> limitQuery = createQuery(query);
		query.accept(limitQuery);
		limitQuery.limit(pageNo, pageSize);
		return findList(query);
	}
	
	/**
	 * 返回实体对象 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default M find(Consumer<Query<M>> query) {
		return execute(new FindDbOperationAdapter<>(dbContext(), createQuery(query)));
	}
	
	default M find(Map<String, Object> queryMap) {
		return find(query->{
			query.where(queryMap);
		});
	}
	
	
	
	/**
	 * 返回列表 数据
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	 default List<M>  findList(Consumer<Query<M>> consumer){
		 return findList(modelClass(), consumer);
	 }
	 
	 /**
	  * find List<T>
	  * @author hanjiang.Yue
	  * @param classType
	  * @param consumer
	  * @return
	  */
	 default <T> List<T> findList(Class<T> classType,Consumer<Query<M>> consumer){
		 return execute(new FindBeanListDbOperationAdapter<>(dbContext(), createQuery(consumer),classType));
	 }

	
	/**
	 * 返回唯一数据
	 * @author hanjiang.Yue
	 * @param typeClass
	 * @param query
	 * @return
	 */
	default <T> T findUnique(Class<T> typeClass,Consumer<Query<M>> query) {
		return execute(new FindObjectDbOperationAdapter<>(dbContext(), createQuery(query), typeClass));
	}
	
	/**
	 * 返回唯一的列表数据
	 * @author hanjiang.Yue
	 * @param typeClass
	 * @param query
	 * @return
	 */
	default <T> List<T> findUniqueList(Class<T> typeClass,Consumer<Query<M>> query){
		return execute(new FindObjectListDbOperationAdapter<>(dbContext(), createQuery(query), typeClass));
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default Integer findToInt(Consumer<Query<M>> query) {
		return findUnique(Integer.class, query);
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default Long findToLong(Consumer<Query<M>> query) {
		return findUnique(Long.class, query);
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default Integer findCount(Consumer<Query<M>> query) {
		Query<M> createQuery = createQuery(q->{});
		query.accept(createQuery);
		return findToInt(e->{
			e.count("1");
			String conditions = createQuery.getConditions();
			int indexOf = conditions.toUpperCase().indexOf("ORDER BY");
			if(indexOf !=  -1) {
				conditions = conditions.substring(0, indexOf);
			}
			e.addFilter(conditions);
			e.addParams(createQuery.getParams().toArray());
		});
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default String findToString(Consumer<Query<M>> query) {
		return findUnique(String.class, query);
	}
	
	/**
	 * 返回 List<String>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<String> findStringList(Consumer<Query<M>> consumer){
		return findUniqueList(String.class, consumer);
	}
	
	/**
	 * 返回 List<Integer>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<Integer> findIntegerList(Consumer<Query<M>> consumer){
		return findUniqueList(Integer.class, consumer);
	}
	
	/**
	 * 返回 List<Long>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<Long> findLongList(Consumer<Query<M>> consumer){
		return findUniqueList(Long.class, consumer);
	}
	
	/**
	 * 返回 List<Double>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<Double> findDoubleList(Consumer<Query<M>> consumer){
		return findUniqueList(Double.class, consumer);
	}
	
	
	/**
	 * 分页查询
	 * @author hanjiang.Yue
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @return
	 */
	default Page<M> findPage(Integer pageNo,Integer pageSize,Consumer<Query<M>> query){
		return execute(new FindPageDbOperationAdapter<>(dbContext(), createQuery(query),modelClass(),pageNo,pageSize));
	}
	
	/**
	 * 条件删除
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default int delete(Consumer<Query<M>> consumer) {
		Query<M> query = createQuery(consumer);
		return execute(new DeleteDbOperationAdapter<M>(dbContext(), query));
	}
	
	/**
	 * 通过主键删除
	 * @author hanjiang.Yue
	 * @param id
	 * @return
	 */
	default int delete(Id id) {
		return delete(q->q.where().idEq(id));
	}
	
	/**
	 * 批量删除
	 * @author hanjiang.Yue
	 * @param ids
	 * @return
	 */
	default int delete(@SuppressWarnings("unchecked") Id...ids) {
		return delete(Arrays.asList(ids));
	}
	
	default int delete(List<Id> ids) {
		Query<M> query = createQuery(q->q.where().idIn(ids.toArray()));
		return execute(new DeleteBatchDbOperationAdapter<M>(dbContext(), query));
	}
	
	
	
	/**
	 * 添加
	 * @author hanjiang.Yue
	 * @param model
	 * @return
	 */
	default Id save(M model) {
		return save(EntityUtils.entityToMap(model));
	}
	
	/**
	 * 保存
	 * @param modelMap
	 * @return
	 */
	default Id save(Map<String, Object> modelMap) {
		Query<M> query = createQuery(q->q.set(modelMap));
		return  execute(new InsertDbOperationAdapter<M>(dbContext(), query));
	}
	
	/**
	 * 批量添加
	 * @author hanjiang.Yue
	 * @param models
	 */
	default void save(List<M> models) {
		 execute(
				new InsertBatchDbOperationAdapter<M>(
						dbContext(),
						createQuery(q->q.batch(EntityUtils.entityListToMapList(models)))
				)				
		);
	}
	
	/**
	 * 更新
	 * @author hanjiang.Yue
	 * @param model
	 * @return
	 */
	 default int update(M model) {
		return update(EntityUtils.entityToMap(model));
	 }
	
	/**
	 * 批量更新
	 * @author hanjiang.Yue
	 * @param models
	 */
	default void update(List<M> models) {
		if(models == null || models.size() == 0) {
			return;
		}
		execute(
				new UpdateBatchDbOperationAdapter<M>(
						dbContext(),
						createQuery(q->{
							q.batch(EntityUtils.entityListToMapList(models));
						})
				)
		);
	}
	
	/**
	 * 更新
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default int update(Consumer<Query<M>> consumer) {
		return execute(new UpdateDbOperationAdapter<M>(dbContext(),createQuery(consumer)));
	}
	
	/**
	 * 更新数据
	 * @author hanjiang.Yue
	 * @param modelMap
	 * @return
	 */
	default int update(Map<String, Object> modelMap) {
		return update(query->{
			TableMapping<M> tm = query.getTableMapping();
			String id = tm.getPrimaryKey();
			 Object idValue = modelMap.get(id);
			 if(StrUtils.isEmpty(idValue)) {
				 throw new RuntimeException("not found "+modelMap+" id value");
			 }
			 //移除主键值
			 modelMap.forEach((k,v)->{
				 if(!k.equals(id)) {
					 query.set(tm.getColumnName(k), v);
				 }
			 });
			 query.where().idEq(idValue);
		});
	}
	
	/**
	 * 更新并且返回数据
	 * @author hanjiang.Yue
	 * @param modelMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default M updateMerge(Map<String, Object> modelMap) {
		TableMapping<M> mapping = DbContext.findTableMapping(modelClass());
		Id id = (Id) modelMap.get(mapping.getPrimaryKey());
		//更新
		update(modelMap);
		return find(id);
	}
	
	
	/**
	 * 添加或者新镇
	 * @author hanjiang.Yue
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default M saveOrUpdateMerge(M model) {
		TableMapping<M> mapping = DbContext.findTableMapping(modelClass());
		Map<String, Object> entityToMap = EntityUtils.entityToMap(model);
		Id id = null;
		if(entityToMap.containsKey(mapping.getPrimaryKey())) {
			id = (Id) entityToMap.get(mapping.getPrimaryKey());
			update(entityToMap);
		}else {
			id = save(model);
		}
		return find(id);
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default Id saveOrUpdate(M model) {
		TableMapping<M> mapping = DbContext.findTableMapping(modelClass());
		Map<String, Object> entityToMap = EntityUtils.entityToMap(model);
		Id id = null;
		if(entityToMap.containsKey(mapping.getPrimaryKey())) {
			id = (Id) entityToMap.get(mapping.getPrimaryKey());
			update(entityToMap);
		}else {
			id = save(model);
		}
		return id;
	}
	
}
