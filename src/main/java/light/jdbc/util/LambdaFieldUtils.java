package light.jdbc.util;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import light.jdbc.code.DbContext;
import light.jdbc.query.Property;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class LambdaFieldUtils<T extends Object> implements MethodInterceptor{

	/**
	 * 缓存
	 */
	private static final Map<String, String> METHOD_TO_FIELD_MAP =  new ConcurrentHashMap<>();
	
	private String fieldName;
	
	@SuppressWarnings("unchecked")
	private <R>  LambdaFieldUtils(Class<T> targetClass,Property<T, R> property) {
		 Object proxy = Enhancer.create(targetClass, this);
		property.apply((T) proxy);
		
	}
	

	public String getFieldName() {
		return fieldName;
	}


	@Override
	public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		String name = method.getName();
		String field = METHOD_TO_FIELD_MAP.get(name);
		if(StrUtils.isEmpty(field)) {
			field = methodNameToField(name);
		}
		this.fieldName = field;
		return null;
	}


	/**
	 * 方法名转换为字段名
	 * @author hanjiang.Yue
	 * @param methodName
	 * @return
	 */
	private String methodNameToField(String methodName) {
		if(methodName.startsWith("get")) {
			char[] charArray = methodName.substring(3, methodName.length()).toCharArray();
			StringBuilder field = new StringBuilder();
			for (int i = 0; i < charArray.length; i++) {
				if(i == 0) {
					char first = Character.toLowerCase(charArray[i]);
					field.append(first);
				}else {
					field.append(charArray[i]);
				}
			}
			return field.toString();
		}
		return methodName;
	}
	
	public static <T,R>  String  getFieldName(Class<T> targetClass,Property<T, R> fun){
		return new LambdaFieldUtils<>(targetClass,fun).getFieldName();
	}

	public static void main(String[] args) {
		String name = getFieldName(DbContext.class,DbContext::toString);
		System.out.println(name);
	}
	
}
