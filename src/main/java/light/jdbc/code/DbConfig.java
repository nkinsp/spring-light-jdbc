package light.jdbc.code;

import light.jdbc.cache.CacheKeyGenerated;
import light.jdbc.cache.CacheOperation;
import light.jdbc.cache.DefaultCacheKeyGenerated;
import light.jdbc.db.DbOperation;
import light.jdbc.db.pk.PrimaryKeyGenerated;
import light.jdbc.db.pk.generated.DefaultPrimaryKeyGenerated;
import light.jdbc.enums.DbType;

public class DbConfig {

	/**
	 * 数据库类型 默认MySQL
	 */
	private DbType dbType;
	/**
	 * db 执行对象
	 */
	private  DbOperation dbOperation;
	
	/**
	 * 缓存操作
	 */
	private CacheOperation cacheOperation;
	
	/**
	 * 缓存key生成规则
	 */
	private CacheKeyGenerated  cacheKeyGenerated = new DefaultCacheKeyGenerated();
	
	/**
	 * 主键生成
	 */
	private PrimaryKeyGenerated primaryKeyGenerated = new DefaultPrimaryKeyGenerated();
	
	/**
	 * oracle 自增id的名称
	 */
	private String oracleSequencesName;

	/**
	 * @return the dbType
	 */
	public DbType getDbType() {
		return dbType;
	}

	/**
	 * @return the dbOperation
	 */
	public DbOperation getDbOperation() {
		return dbOperation;
	}

	/**
	 * @return the cacheOperation
	 */
	public CacheOperation getCacheOperation() {
		return cacheOperation;
	}

	/**
	 * @return the cacheKeyGenerated
	 */
	public CacheKeyGenerated getCacheKeyGenerated() {
		return cacheKeyGenerated;
	}

	/**
	 * @return the primaryKeyGenerated
	 */
	public PrimaryKeyGenerated getPrimaryKeyGenerated() {
		return primaryKeyGenerated;
	}

	/**
	 * @return the oracleSequencesName
	 */
	public String getOracleSequencesName() {
		return oracleSequencesName;
	}

	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}

	/**
	 * @param dbOperation the dbOperation to set
	 */
	public void setDbOperation(DbOperation dbOperation) {
		this.dbOperation = dbOperation;
	}

	/**
	 * @param cacheOperation the cacheOperation to set
	 */
	public void setCacheOperation(CacheOperation cacheOperation) {
		this.cacheOperation = cacheOperation;
	}

	/**
	 * @param cacheKeyGenerated the cacheKeyGenerated to set
	 */
	public void setCacheKeyGenerated(CacheKeyGenerated cacheKeyGenerated) {
		this.cacheKeyGenerated = cacheKeyGenerated;
	}

	/**
	 * @param primaryKeyGenerated the primaryKeyGenerated to set
	 */
	public void setPrimaryKeyGenerated(PrimaryKeyGenerated primaryKeyGenerated) {
		this.primaryKeyGenerated = primaryKeyGenerated;
	}

	/**
	 * @param oracleSequencesName the oracleSequencesName to set
	 */
	public void setOracleSequencesName(String oracleSequencesName) {
		this.oracleSequencesName = oracleSequencesName;
	}

	public DbConfig(DbOperation dbOperation, CacheOperation cacheOperation, CacheKeyGenerated cacheKeyGenerated,
			PrimaryKeyGenerated primaryKeyGenerated) {
		super();
		this.dbOperation = dbOperation;
		this.cacheOperation = cacheOperation;
		this.cacheKeyGenerated = cacheKeyGenerated;
		this.primaryKeyGenerated = primaryKeyGenerated;
	}

	public DbConfig(DbType dbType, DbOperation dbOperation, CacheOperation cacheOperation,
			CacheKeyGenerated cacheKeyGenerated) {
		super();
		this.dbType = dbType;
		this.dbOperation = dbOperation;
		this.cacheOperation = cacheOperation;
		this.cacheKeyGenerated = cacheKeyGenerated;
	}

	public DbConfig(DbOperation dbOperation, CacheOperation cacheOperation, CacheKeyGenerated cacheKeyGenerated) {
		super();
		this.dbOperation = dbOperation;
		this.cacheOperation = cacheOperation;
		this.cacheKeyGenerated = cacheKeyGenerated;
	}
	
	
	
	
}
