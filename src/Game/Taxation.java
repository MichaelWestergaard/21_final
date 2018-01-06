package Game;

public class Taxation extends Field {

	int tax;

	public Taxation(int fieldNo, String name, int tax) {
		super(fieldNo, name);
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