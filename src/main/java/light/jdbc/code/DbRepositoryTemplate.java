package light.jdbc.code;

import java.lang.reflect.ParameterizedType;




/**
 * @author hanjiang.Yue
 * @param <M> 对应的实体 
 * @param <Id>  主键类型
 */
public abstract class DbRepositoryTemplate<M,Id> implements  DbRepository<M, Id>{

	private Class<Id> idClass;
	
	private Class<M> tableClass;
	
	@SuppressWarnings("unchecked")
	public DbRepositoryTemplate() {
		super();
		this.tableClass = (Class<M>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		this.idClass = (Class<Id>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
	}

	

	/**
	 * 获取实体的class
	 * @author hanjiang.Yue
	 * @return
	 */
	@Override
	public Class<M> modelClass(){
		return this.tableClass;
	}


	@Override
	public Class<Id> idClass() {
		return this.idClass;
	}
}
