package entities;

public class Street extends Buyable {

	private int[] rent;
	private int housePrice;
	private int houseCounter;
	
	public Street(int fieldNo, String name, Player owner, Group group, int price, int[] rent, int housePrice) {
		super(fieldNo, name, owner, group, price);
		this.rent = rent;
		this.housePrice = housePrice;
		this.houseCounter = 0;
	}

	public int getRent() {
		return rent[houseCounter];	
	}

	public void buyHouse() {
		houseCounter++;
	}
	
	public void buyHotel() {
		houseCounter = 5;
	}
	
	public void sellHouse() {
		houseCounter--;
	}
	
	public void sellHotel() {
		houseCounter = 4;
	}
	
	public int getHouse() {
		return houseCounter;
	}
	
	public int getHousePrice() {
		return housePrice;
	}
	
}
