package light.jdbc.db.pk.generated;

import light.jdbc.db.pk.PrimaryKeyGenerated;
import light.jdbc.enums.IdType;
import light.jdbc.query.Field;
import light.jdbc.table.TableMapping;
import light.jdbc.util.IdWorkUtils;

public class DefaultPrimaryKeyGenerated implements PrimaryKeyGenerated{
	
	@Override
	public Field generated(TableMapping<?> mapping) {
		IdType idType = mapping.getIdType();
		if(idType == IdType.UUID) {
			return new Field(mapping.getPrimaryKey(),IdWorkUtils.uuid());
		}
		if(idType == IdType.SEQUENCE) {
			return new Field(mapping.getPrimaryKey(), IdWorkUtils.sequenceId());
		}
		return null;
	}

	public DefaultPrimaryKeyGenerated() {
		super();
	}
	
	

}
