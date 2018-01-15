package entities;

public class Card {
	
	protected String type;
	private String description;

	public Card(String type, String description) {
		this.type = type;
		this.setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}