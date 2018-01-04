package Game;

public abstract class Field {

	protected int fieldNo;
	protected String name;
	protected String type;

	public Field(int fieldNo, String name, String type) {

		this.fieldNo = fieldNo;
		this.name = name;
		this.type = type;
	}

	public abstract void landOnField(Player player);
	
	public String getType() {
		return type;
	}
	
	public int getFieldNo() {
		return fieldNo;
	}
	
	public String getName() {
		return name;
	}
	
}