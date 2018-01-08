package Game;

public class Street extends Buyable {

	private int[] rent;
	private int housePrice;
	private int houseCounter;
	private int hotelCounter;
	
	public Street(int fieldNo, String name, Player owner, Group group, int price, int[] rent, int housePrice) {
		super(fieldNo, name, owner, group, price);
		this.rent = rent;
		this.housePrice = housePrice;
		this.houseCounter = 0;
		this.hotelCounter = 0;
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
	
	
	public Player getOwner() {
		return super.owner;
	}

	public void setOwner(Player owner) {
		super.owner = owner;
	}

	public int getRent() {
		if(hotelCounter == 1) {
			return rent[5];
		}
	
		if(houseCounter > 0) {
			return rent[houseCounter];
		}
	
		else {
			return rent[0];
		}
	
	}

	public void buyHouse() {
		houseCounter++;
	}
	
	public void buyHotel() {
		houseCounter = 0;
		hotelCounter = 1;
	}
	
	public void sellHouse() {
		houseCounter--;
	}
	
	public void sellHotel() {
		hotelCounter = 0;
	}
	
	public int getHouse() {
		return houseCounter;
	}
	
	public int getHotel() {
		return hotelCounter;
	}
	
}
