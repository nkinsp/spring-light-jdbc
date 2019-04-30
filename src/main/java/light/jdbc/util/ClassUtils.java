package light.jdbc.util;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author yue571041524
 *
 */
public class ClassUtils {
	
	private static PropertyDescriptor createPropertyDescriptor(String name, Class<?> beanClass) {
		try {
			return new PropertyDescriptor(name, beanClass);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}

	public static List<PropertyDescriptor> getPropertyDescriptors(Class<?> beanClass) {
		return Stream.of(beanClass.getDeclaredFields())
				.map(field -> createPropertyDescriptor(field.getName(), beanClass))
				.filter(pd -> pd != null)
				.collect(Collectors.toList());
	}
	
	public static <T> T newInstance(Class<T> clasz) {
		try {
			return clasz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
}
