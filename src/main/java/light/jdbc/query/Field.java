package light.jdbc.query;

import light.jdbc.enums.FieldType;

public class Field {

	private String name;
	
	private Object value;
	
	private FieldType fieldType;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the fieldType
	 */
	public FieldType getFieldType() {
		return fieldType;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public Field(String name, Object value, FieldType fieldType) {
		super();
		this.name = name;
		this.value = value;
		this.fieldType = fieldType;
	}

	public Field(String name, Object value) {
		this(name,value,FieldType.TABLE_FIELD);
	}
	
	
	public boolean isTableField() {
		return this.fieldType == FieldType.TABLE_FIELD;
	}
	

	
	
	
}
