package light.jdbc.activerecord;


import java.lang.reflect.ParameterizedType;

import light.jdbc.code.DbContext;
import light.jdbc.code.DbRepository;

/**
 * activerecord Model
 * @author hanjiang.Yue
 * @param <M>
 * @param <Id>
 */
@SuppressWarnings("unchecked")
public class ActiveRecord<M,Id> implements DbRepository<M, Id>{
	
	public transient static  DbContext dbContext;
	
	private transient Class<Id> idClass =  (Class<Id>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
	
	public ActiveRecord() {
		super();
	}
	

	/**
	 * 加载DbContent 
	 * @author hanjiang.Yue
	 * @param context
	 */
	public static void init(DbContext context) {
		dbContext = context;
	}
	


	protected M modelThis() {
		return (M) this;
	}
	
	@Override
	public Class<M> modelClass() {
		return (Class<M>) this.getClass();
	}

	@Override
	public Class<Id> idClass() {
		return this.idClass;
	}

	@Override
	public DbContext dbContext() {
		return dbContext;
	}
	
	/**
	 * 新增
	 * @author hanjiang.Yue
	 * @return
	 */
	public Id save() {
		return save(modelThis());
	}
	
	/**
	 * 删除
	 * @author hanjiang.Yue
	 * @return
	 */
	public int delete() {
		Id id  = DbContext.findTableMapping(modelClass()).getIdProperty().getValue(this);
		return delete(id);
	}
	
	/**
	 * 更新
	 * @author hanjiang.Yue
	 * @return
	 */
	public int update() {
		return update(modelThis());
	}
	
	

}
