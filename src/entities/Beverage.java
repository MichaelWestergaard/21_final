package entities;

public class Beverage extends Buyable {

	private int rent;

	public Beverage(int fieldNo, String name, Player owner, Group group, int price, int rent) {
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