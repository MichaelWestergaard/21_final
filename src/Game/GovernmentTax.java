package Game;

public class GovernmentTax extends Taxation {

	public GovernmentTax(int fieldNo, String name, int tax) {
		super(fieldNo, name, tax);
	}
	
	public  void payTax(Parking amount, Player player) {
		setTax(2000);
		if (player.getPoints() > tax) {
			player.addPoints(-1 * tax);
			amount.increaseAmount(tax);
		} //else {
		//Pansæt
		//falit
	}
}
