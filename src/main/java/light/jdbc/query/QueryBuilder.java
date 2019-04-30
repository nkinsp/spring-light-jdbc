package light.jdbc.query;

import java.util.List;

public interface QueryBuilder {

	/**
	 * 创建删除 DELETE sql
	 * @author hanjiang.Yue
	 * @return
	 */
	 String buildDeleteSQL();
	 
	/**
	 * 构建新增INSERT sql
	 * @author hanjiang.Yue
	 * @param fields
	 * @return
	 */
	 String buildInsertSQL();
	
	 /**
	  * 批量新增sql 
	  * @author hanjiang.Yue
	  * @return
	  */
	 String buildBatchInsertSQL();
	
	/**
	 * 构建更新UPDATE sql 
	 * @author hanjiang.Yue
	 * @return
	 */
	 String  buildUpdateSQL();
	
	 /**
	  *  构建批量更新 UPDATE sql 
	  * @author hanjiang.Yue
	  * @return
	  */
	 String buildBatchUpdateSQL();
	
	/**
	 * 构建查询 SELECT sql
	 * @author hanjiang.Yue
	 * @param table
	 * @param conditions
	 * @param columns
	 * @return
	 */
	 String buildSelectSQL();
	 
	 String buildSelectSQL(List<String> cols);
	 
	 String buildPagingSQL(int pageNo,int pageSize);
	 
	void release();
	 
	 

}
