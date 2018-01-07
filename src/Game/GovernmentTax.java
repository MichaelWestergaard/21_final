package Game;

public class GovernmentTax extends Taxation {

	public GovernmentTax(int fieldNo, String name, int tax) {
		super(fieldNo, name, tax = 2000);
	}
	
	public void setTax(int tax) {
		this.tax = tax;
	}
	
	public  void payTax(Parking amount, Player player) {
		if (player.getPoints() > tax) {
			player.addPoints(-1 * tax);
			amount.increaseAmount(tax);
		} //else {
		//Pansæt
		//falit
	}
}
