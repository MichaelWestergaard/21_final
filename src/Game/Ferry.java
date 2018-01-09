package Game;

public class Ferry extends Buyable {

	int rent;

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
	
	public void landOnField(Player player, int rent, boolean owned, boolean buy) {
		int amountToPay = rent; //Lejen der skal betales
		
				
		Player owner = super.getOwner();

		if (owner == null) {// hvis der er ingen ejer, så køber man feltet
			if (player.getPoints() >= super.getPrice()) {
				player.addPoints(super.getPrice() * -1);
				setOwner(player);
			} 
			else {
				player.setBankrupt(true); //ændre til pantsat. Spørg om det skal fjernes!!.
			}
		} 
		else { // Hvis der er en ejer, så betaler man

			if (player.getPoints() >= amountToPay) {
				player.addPoints(amountToPay * -1);
				owner.addPoints(amountToPay);
			}
		}
	}

	public Player getOwner() {
		return super.getOwner();
	}	
	
}
