package Main;

import Controller.Game;

public class PlayGame {

	public static void main(String[] args) {
		Game game = new Game();
		game.gameSetup();
		game.play();
	}

}
