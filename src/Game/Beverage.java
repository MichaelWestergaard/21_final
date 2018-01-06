package Game;

public class Beverage extends Buyable {

	int rent;

	public Beverage(int fieldNo, String name, Player owner, Group group, int price, int rent) {
		super(fieldNo, name, owner, group, price);
		this.rent = rent; 
	}
	public void landOnField(Player player,int diceSum, int rent) {
		int amountToPay = diceSum*rent; //renten der skal betales

		Player owner = super.getOwner();
		if (owner == null) {// hvis der er ingen ejer, så køber man feltet
			if (player.getPoints() >= super.getPrice()) {
				player.addPoints(super.getPrice() * -1);
				setOwner(player);
			} 
			else {
				player.setBankrupt(true); //ændre til pantsat
			}
		} 
		else { // Hvis der er en ejer, så betaler man

			if (player.getPoints() >= amountToPay || player.getPoints() >= (amountToPay * 2)) {
				player.addPoints(amountToPay * -1);
				owner.addPoints(amountToPay);
			} else {
				player.setBankrupt(true); //ændre til pantsat
				
			}
		}
		
		
		
	}
}



