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

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPledgePrice() {
		return pledgePrice;
	}

	public void setPledgePrice(int pledgePrice) {
		this.pledgePrice = pledgePrice;
	}

	public boolean isPledged() {
		return isPledged;
	}

	public void setPledged(boolean isPledged) {
		this.isPledged = isPledged;
	}
				
}