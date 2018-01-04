package Game;

public class MoneyCard extends Card {
	
	private int amount;
	
	public MoneyCard(String type, String description, int amount) {
		super(type, description);
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}
}
