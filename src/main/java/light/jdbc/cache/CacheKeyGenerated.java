package light.jdbc.cache;

import light.jdbc.table.TableMapping;

public interface CacheKeyGenerated {

	String generated(TableMapping<?> mapping,Object value);
}
