package light.jdbc.db.dbutils.handler;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import light.jdbc.annotation.Table;
import light.jdbc.code.DbContext;
import light.jdbc.table.TableMapping;

public class BeanHandler<T> extends BeanListHandler<T>{

	private Class<? extends T> typeClass;
	
	public BeanHandler(Class<? extends T> type) {
		super(type);
		this.typeClass = type;
	}
	
	private T createBean() {
		try {
			return typeClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<T> handle(ResultSet rs) throws SQLException {
		Table table = typeClass.getAnnotation(Table.class);
		if(table == null) {
			return super.handle(rs);
		}
		List<T> results = new ArrayList<>();
		TableMapping<? extends T> mapping = DbContext.findTableMapping(typeClass);
		if(mapping == null) {
			return results;
		}
		while (rs.next()) {
			T bean = createBean();
			mapping.getPropertys().forEach(p->{
				PropertyDescriptor property = p.getProperty();
				try {
					p.setValue(bean, rs.getObject(p.getColumnName(), property.getPropertyType()));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			results.add(bean);
		}
		return results;
	}
	

}
