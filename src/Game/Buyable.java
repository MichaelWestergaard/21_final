package Game;

public class Buyable extends Field {
	
	protected Player owner;
	protected int price;
	protected int pledgePrice;
	protected boolean isPledged = false;
		
	public Buyable(int fieldNo, String name, Player owner, int price, int pledgePrice) {
		super(fieldNo, name);
		this.owner = owner;
		this.price = price;
		this.pledgePrice = pledgePrice;
	}

	@Override
	public void landOnField(Player player) {
				
	}
	
	
		
}
