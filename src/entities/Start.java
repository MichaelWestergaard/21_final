package entities;

public class Start extends Field {

	private int bonus;

	public Start(int fieldNo, int bonus, String name) {
		super(fieldNo, name);
		this.bonus = bonus;
	}
	
	public int getBonus() {
		return bonus;
	}

}
