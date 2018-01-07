package Game;

public abstract class Taxation extends Field {

	protected int tax;

	public Taxation(int fieldNo, String name, int tax) {
		super(fieldNo, name);
	}

	public abstract void payTax(Parking amount, Player player);
	
	public void setTax(int tax) {
		this.tax = tax;
	}
	
	public int getTax() {
		return tax;
	}
	
	
	

	@Override
	public void landOnField(Player player) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getFieldNo() {
		// TODO Auto-generated method stub
		return super.getFieldNo();
	}
	
}