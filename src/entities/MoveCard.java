package entities;

public class MoveCard extends Card{
	
	private int fieldNo;
	
	public MoveCard(String type, String description, int fieldNo) {
		super(type, description);
		this.fieldNo = fieldNo;
	}
	
	public int getField() {
		return this.fieldNo;
	}
}
