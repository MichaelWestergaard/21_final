package entities;

public class Parking extends Field {

	private int amount;

	public Parking(int fieldNo, String name, int amount) {
		super(fieldNo, name);
		this.amount = amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void increaseAmount(int amount) {
		this.amount += amount;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
}
