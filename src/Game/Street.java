package Game;

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

	public void landOnField(Player player, boolean owned, boolean buy) {
		
		//Hvis feltet ikke ejes af en anden spiller
		if(!owned) {
			//Hvis spilleren vælger at købe feltet
			if(buy) {
					player.addPoints((super.price * -1));
					setOwner(player);
			}	
		}
		
		//Hvis feltet ejes af en anden spiller
		if(owned) {
			player.addPoints(getRent() * -1);
			super.owner.addPoints(getRent());
		}
	}
	
	public void sellField(Player player) {
		player.addPoints(super.price);
		resetOwner(player);
	}
	
	public Player getOwner() {
		return super.owner;
	}

	public void resetOwner(Player owner) {
		super.owner = null;
		owner.resetOwnedFieldNumber(super.fieldNo);
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
		houseCounter = 0;
	}
	
	public int getHouse() {
		return houseCounter;
	}
	
	public int getHousePrice() {
		return housePrice;
	}
	
}
