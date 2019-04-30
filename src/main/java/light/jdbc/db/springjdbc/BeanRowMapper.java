package light.jdbc.db.springjdbc;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.ClassUtils;

import light.jdbc.annotation.Table;
import light.jdbc.code.DbContext;
import light.jdbc.table.Property;
import light.jdbc.table.TableMapping;

public class BeanRowMapper<T> extends BeanPropertyRowMapper<T>{

	private Log log = LogFactory.getLog(getClass());
	



	public BeanRowMapper(Class<T> entityClass) {
		super.setMappedClass(entityClass);
	}



	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Class<T> mappedClass = getMappedClass();
		if(mappedClass.getAnnotation(Table.class) == null) {
			return super.mapRow(rs, rowNum);
		}
		return getResultObject(rs, mappedClass);
	}




	private T getResultObject(ResultSet rs, Class<T> mappedClass) throws SQLException {
		TableMapping<T> tableMapping = DbContext.findTableMapping(mappedClass);
		T mappedObject = BeanUtils.instantiateClass(mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		ResultSetMetaData metaData = rs.getMetaData();
		int count = metaData.getColumnCount();
		for (int index = 1; index <= count; index++) {
			String columnName = JdbcUtils.lookupColumnName(metaData, index);
			Property propertyMapping = tableMapping.getPropertyByColumn(columnName);
			if(propertyMapping == null) {
				continue;
			}
			PropertyDescriptor pd = propertyMapping.getProperty();
			try {
				Object value = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
				bw.setPropertyValue(pd.getName(), value);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				log.error("Mapping column '" + columnName + "' to property '" + pd.getName() +
						"' of type '" + ClassUtils.getQualifiedName(pd.getPropertyType()) + " fail'");
			}
		}
		return mappedObject;
	}

}
