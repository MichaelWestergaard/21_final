package entities;

public abstract class Field {

	protected int fieldNo;
	protected String name;

	public Field(int fieldNo, String name) {
		this.fieldNo = fieldNo;
		this.name = name;
	}

	public String getType() {
		return this.getClass().getName();
	}
	
	public int getFieldNo() {
		return fieldNo;
	}
	
	public String getName() {
		return name;
	}
	
}