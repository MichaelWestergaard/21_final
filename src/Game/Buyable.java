package Game;

public class Buyable extends Field {
	
	protected Player owner;
	protected Group group;
	protected int price;
	protected int pledgePrice;
	protected boolean isPledged = false;
	
	public Buyable(int fieldNo, String name, Player owner, Group group, int price) {
		super(fieldNo, name);
		this.owner = owner;
		this.group = group;
		this.price = price;
		this.pledgePrice = price/2;
	}

	@Override
	public void landOnField(Player player) {
		//Test af commit [Tim]		
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
		owner.setOwnedFieldNumber(super.fieldNo);
	}
	
	public void sellField(Player player) {
		player.addPoints(this.price);
		resetOwner(player);
	}
	
	public void resetOwner(Player owner) {
		this.owner = null;
		owner.resetOwnedFieldNumber(super.fieldNo);
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