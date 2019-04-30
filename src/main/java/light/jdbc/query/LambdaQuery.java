package light.jdbc.query;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import light.jdbc.util.LambdaFieldUtils;

public interface LambdaQuery<T> extends Query<T>{


	/**
	 * 查询字段
	 * @author hanjiang.Yue
	 * @param props
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default <R> LambdaQuery<T> select(Property<T, R>...props){
		 select(Stream
				.of(props)
				.map(property->LambdaFieldUtils.getFieldName(getTableMapping().getTableClass(), property))
				.collect(Collectors.toList())
		);
		return this; 
	}
	
	/**
	 * 更新字段
	 * @author hanjiang.Yue
	 * @param prop
	 * @param value
	 * @return
	 */
	default <R> LambdaQuery<T> set(Property<T, R> prop,Object value){
		String fieldName = LambdaFieldUtils.getFieldName(getTableMapping().getTableClass(), prop);
		set(fieldName, value);
		return this;
	}
	
	
	
	
	
	
}
