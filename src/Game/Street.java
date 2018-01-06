package Game;

public class Street extends Buyable {

	private int[] rent;
	private int housePrice;
	
	public Street(int fieldNo, String name, Player owner, Group group, int price, int[] rent, int housePrice) {
		super(fieldNo, name, owner, group, price);
		this.rent = rent;
		this.housePrice = housePrice;
	}

	public Player getOwner() {
		return super.owner;
	}

	public void setOwner(Player owner) {
		super.owner = owner;
	}

	public int getRent(int i) {
		return rent[i];
	}

}
