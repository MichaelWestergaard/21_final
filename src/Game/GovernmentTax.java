package Game;

public class GovernmentTax extends Taxation {

	public GovernmentTax(int fieldNo, String name, int tax) {
		super(fieldNo, name, tax);
	}
	
	@Override
	public void landOnField(Player player) {
		if (player.getPoints() > getTax()) {
			player.addPoints(getTax() * -1);
		} //else {
		//Pansæt
		//falit
	}
	

}
