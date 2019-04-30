package light.jdbc.code;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import light.jdbc.activerecord.ActiveRecord;
import light.jdbc.cache.CacheKeyGenerated;
import light.jdbc.cache.CacheOperation;
import light.jdbc.cache.DefaultCacheKeyGenerated;
import light.jdbc.cache.impl.SimpleHashMapCacheOperation;
import light.jdbc.db.DbOperation;
import light.jdbc.db.springjdbc.SpringJdbcDbOperation;
import light.jdbc.enums.DbType;
import light.jdbc.query.Query;
import light.jdbc.query.impl.DB2DialectQueryImpl;
import light.jdbc.query.impl.MySqlDialectQueryImpl;
import light.jdbc.query.impl.OracleDialectQueryImpl;
import light.jdbc.query.impl.PostgreDialectQueryImpl;
import light.jdbc.query.impl.SQLiteDialectQueryImpl;
import light.jdbc.table.TableMapping;

public class DbContext{
	
	
	private static final Logger log = LoggerFactory.getLogger(DbContext.class);
	
	private static final Map<String, TableMapping<?>> TABLEMAPPING = new ConcurrentHashMap<>();

	private DbConfig config;
	
	/**
	 * 获取表映射
	 * @author hanjiang.Yue
	 * @param tableClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized <T> TableMapping<T>  findTableMapping(Class<T> tableClass) {
		String name = tableClass.getName();
		TableMapping<?> mapping = TABLEMAPPING.get(name);
		if(mapping == null) {
			mapping = new TableMapping<>(tableClass);
			TABLEMAPPING.put(name, mapping);
			if(log.isDebugEnabled()) {
				log.debug("==> init table mapping class:[{}] fields:[{}] ",tableClass,mapping.getColumns());
			}
		}
		return (TableMapping<T>) mapping;
	}
	
	/**
	 * 创建
	 * @author hanjiang.Yue
	 * @param tableClass
	 * @return
	 */
	public <M> Query<M> createQuery(Class<M> tableClass) {
		return createQuery(tableClass, config);
	}
	
	/**
	 * 创建query 对象
	 * @author hanjiang.Yue
	 * @param dbType
	 * @param tableClass
	 * @param config
	 * @return
	 */
	public static <M> Query<M> createQuery(Class<M> tableClass,DbConfig config) {
		TableMapping<M> mapping = findTableMapping(tableClass);
		DbType dbType = config.getDbType();
		if(dbType == DbType.MYSQL) {
			return new MySqlDialectQueryImpl<>(mapping,config);
		}
	   if(dbType == DbType.SQLITE) {
			return new SQLiteDialectQueryImpl<>(mapping,config);
		}
		if(dbType == DbType.POSTGRE_SQL) {
			return new PostgreDialectQueryImpl<>(mapping,config);
		}
		if(dbType == DbType.ORACLE) {
			return new OracleDialectQueryImpl<>(mapping,config);
		}
		if(dbType == DbType.DB2) {
			return new DB2DialectQueryImpl<>(mapping, config);
		}
		return null;
	}
	
	public static <M> Query<M> createQuery(Class<M> tableClass,DbContext context) {
		return createQuery(tableClass, context.getConfig());
	}
	
	
	public static <T,Id> DbRepository<T, Id> create(Class<T> tableClass) {
		
		return ActiveRecord.dbContext.table(tableClass);
		
	}
	



	public DbOperation getDbOperation() {
		return this.config.getDbOperation();
	}

	public void setDbOperation(DbOperation dbOperation) {
		this.config.setDbOperation(dbOperation);
	}

	public CacheOperation getCacheOperation() {
		return this.config.getCacheOperation();
	}

	public void setCacheOperation(CacheOperation cacheOperation) {
		this.config.setCacheOperation(cacheOperation);
	}

	public CacheKeyGenerated getCacheKeyGenerated() {
		return this.config.getCacheKeyGenerated();
	}

	public void setCacheKeyGenerated(CacheKeyGenerated cacheKeyGenerated) {
		this.config.setCacheKeyGenerated(cacheKeyGenerated);
	}
	

	public String getOracleSequencesName() {
		return this.config.getOracleSequencesName();
	}

	public void setOracleSequencesName(String oracleSequencesName) {
		this.config.setOracleSequencesName(oracleSequencesName);
	}


	public DbContext(DbOperation dbOperation) {
		this(dbOperation, new SimpleHashMapCacheOperation());
	}

	public DbContext(DbOperation dbOperation, CacheOperation cacheOperation) {
		this(dbOperation,cacheOperation,new DefaultCacheKeyGenerated());
	}

	public DbContext(DbOperation dbOperation, CacheOperation cacheOperation, CacheKeyGenerated cacheKeyGenerated) {
		this.config = new DbConfig(dbOperation,cacheOperation,cacheKeyGenerated);
		this.config.setDbType(initDbType());
		log.info("init load dbType {} :",this.config.getDbType().name());
		//加载
		log.info("init load activerecord DbContext");
		ActiveRecord.init(this);
	}
	

	public  <T,Id> DbRepository<T, Id> table(Class<T> tableClass){
		return new DbRepository<T, Id>() {

			@Override
			public Class<T> modelClass() {
				return tableClass;
			}

			@SuppressWarnings("unchecked")
			@Override
			public Class<Id> idClass() {
				return (Class<Id>) findTableMapping(tableClass).getIdClassType();
			}
			
			@Override
			public DbContext dbContext() {
				return DbContext.this;
			}
		};
	}
	
	
	public DbContext(DataSource dataSource) {
		this(new SpringJdbcDbOperation(dataSource));
	}

	/**
	 * 加载数据库类型
	 * @author hanjiang.Yue
	 */
	public DbType initDbType() {
		Connection connection = null;
		try {
			connection = this.config.getDbOperation().getDataSource().getConnection();
			DatabaseMetaData metaData =connection.getMetaData();
			String url = metaData.getURL().toLowerCase();
			Map<String, DbType> dbTypeMap = new HashMap<>();
			dbTypeMap.put("jdbc:mysql:", DbType.MYSQL);
			dbTypeMap.put("jdbc:sqlite:", DbType.SQLITE);
			dbTypeMap.put("jdbc:oracle:", DbType.ORACLE);
			dbTypeMap.put("jdbc:postgresql:", DbType.POSTGRE_SQL);
			dbTypeMap.put("jdbc:db2:", DbType.DB2);
			dbTypeMap.put("jdbc:sqlserver:", DbType.SQSERVER);
			for (Entry<String, DbType> en : dbTypeMap.entrySet()) {
				if(url.startsWith(en.getKey())) {
					return en.getValue();
				}
			}
			throw new RuntimeException("No Match DB");
		} catch (Exception e) {
			throw new RuntimeException("init dbType Error", e);
		}finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public DbConfig getConfig() {
		return config;
	}

	public void setConfig(DbConfig config) {
		this.config = config;
	}
	
	
	
	
}
