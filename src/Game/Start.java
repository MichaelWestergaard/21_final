package Game;

public class Start extends Field {

	private int bonus;

	public Start(int fieldNo, int bonus, String name) {
		super(fieldNo, name, "Start");
		this.bonus = bonus;
	}

	@Override
	public void landOnField(Player player) {
		player.addPoints(bonus);
	}
	
	public int getBonus() {
		return bonus;
	}

}
