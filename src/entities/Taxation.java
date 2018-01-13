package entities;

public class Taxation extends Field {

	protected int tax;

	public Taxation(int fieldNo, String name, int tax) {
		super(fieldNo, name);
		this.tax = tax;
	}
	
	public void setTax(int tax) {
		this.tax = tax;
	}
	
	public int getTax() {
		return tax;
	}
	
}