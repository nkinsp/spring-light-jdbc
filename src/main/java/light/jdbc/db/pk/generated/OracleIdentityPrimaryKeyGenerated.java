package light.jdbc.db.pk.generated;


import light.jdbc.enums.FieldType;
import light.jdbc.enums.IdType;
import light.jdbc.query.Field;
import light.jdbc.table.TableMapping;
import light.jdbc.util.StrUtils;

/**
 * Oracle 自增 id
 * @author hanjiang.Yue
 *
 */
public class OracleIdentityPrimaryKeyGenerated extends DefaultPrimaryKeyGenerated{

	
	
	@Override
	public Field generated(TableMapping<?> mapping) {
		if(mapping.getIdType() == IdType.AUTO) {
			String keySequence = mapping.getKeySequence();
			if(StrUtils.isEmpty(keySequence)) {
				throw new RuntimeException("Not Found  @KeySequence");
			}
			return new Field(mapping.getPrimaryKey(), keySequence, FieldType.SQL_STRING);
		}
		return super.generated(mapping);
	}
}
