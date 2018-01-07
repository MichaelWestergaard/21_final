package Game;

public class Parking extends Field {

	private int amount;

	public Parking(int fieldNo, String name, int amount) {
		super(fieldNo, name);
		amount = 0;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void increaseAmount(int amount) {
		this.amount += amount;
	}
	public int getAmount() {
		return amount;
	}

	public void landOnField(Player player) {
		player.addPoints(amount);	
		setAmount(amount);	
	}


}
