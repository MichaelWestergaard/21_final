package Game;

public abstract class Field {

	protected int fieldNo;
	protected String name;

	public Field(int fieldNo, String name) {
		this.fieldNo = fieldNo;
		this.name = name;
	}

	public abstract void landOnField(Player player);
	
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