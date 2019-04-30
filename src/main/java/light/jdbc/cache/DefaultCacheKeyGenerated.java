package light.jdbc.cache;

import light.jdbc.table.TableMapping;
import light.jdbc.util.MD5Utils;

public class DefaultCacheKeyGenerated implements CacheKeyGenerated{

	@Override
	public String generated(TableMapping<?> mapping, Object value) {
		return  new StringBuilder(mapping.getTableName())
				.append(":cache:")
				.append(MD5Utils.md5(String.valueOf(value)))
				.toString();
	}

}
