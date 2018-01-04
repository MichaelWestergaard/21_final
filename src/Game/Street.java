package Game;

import Controller.Game;

public class Street extends Field {

	private Player owner;
	private int rent;
	private Group group;

	public Street(int fieldNo, String name, Player owner, int rent, Group group) {
		super(fieldNo, name, "Street");
		this.owner = owner;
		this.rent = rent;
		this.setGroup(group);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getRent() {
		return rent;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public void landOnField(Player player) {

		if (owner == null) {// hvis der er ingen ejer, så køber man feltet
			if (player.getPoints() >= getRent()) {
				player.addPoints(getRent() * -1);
				setOwner(player);
			} else {
				player.setBankrupt(true);
			}
		} else { // Hvis der er en ejer, så betaler man

			if (player.getPoints() >= getRent() || player.getPoints() >= (getRent() * 2)) {
				player.addPoints(getRent() * -1);
				owner.addPoints(getRent());
			} else {
				player.setBankrupt(true);
			}

		}

	}
}
