package light.jdbc.db.pk;

import light.jdbc.query.Field;
import light.jdbc.table.TableMapping;

public interface PrimaryKeyGenerated {

	/**
	 * 生成主键id
	 * @author hanjiang.Yue
	 * @param idType
	 * @return
	 */
	Field generated(TableMapping<?> mapping);
}
