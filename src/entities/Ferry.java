package entities;

public class Ferry extends Buyable {

	private int rent;

	public Ferry(int fieldNo, String name, Player owner, Group group, int price, int rent) {
		super(fieldNo, name, owner, group, price);
		this.rent = rent;
	}

	public int getRent() {
		return rent;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}

}
