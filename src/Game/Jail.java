package Game;

public class Jail extends Field {

	public Jail(int fieldNo, String name, String type) {
		super(fieldNo, name);
	}

	@Override
	public void landOnField(Player player) {
		if (player.getFieldNo() == 18) {
			player.setFieldNo(6);
			player.setJailed(true);
		}

	}

}
