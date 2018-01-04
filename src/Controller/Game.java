package Controller;

import Game.Board;
import Game.Chance;
import Game.Dice;
import Game.Field;
import Game.Player;
import Game.Street;

//Vi mangler at lave delen, med at finde ud af hvem der er ejeren.
//Hvis man ejer to felter, skal man betale dobbelt så meget.

public class Game {

	private boolean gameStarted = false;

	private Dice dice = new Dice();
	private Player[] players;
	private GUI_Controller gui_controller = new GUI_Controller();
	private Board board = new Board();

	public void gameSetup() {

		// Create fields + board
		board.createFields();

		gui_controller.setupGUI(board.getFields());

		int playerAmount = Integer.parseInt(gui_controller.getUserInput("Indtast antallet af spillere"));
		while (playerAmount > 4 || playerAmount < 2) {
			playerAmount = Integer.parseInt(gui_controller.getUserInput("Indtast antallet af spillere (2-4)"));
		}

		players = new Player[playerAmount];

		for (int i = 0; i < playerAmount; i++) {

			String name = gui_controller.getUserInput("Spiller " + (i + 1) + " vælger sit navn:");

			players[i] = new Player(name, 30000);
		}

		gui_controller.addPlayers(players);

		gameStarted = true;
	}

	public void play() {

		// Når en har tabt, skal der tælles hvem har flest penge. Når det er
		// talt, skal
		// konsollen udvælge med en besked hvem der har vundet
		if (gameStarted) {
			boolean runGame = true;

			while (runGame) {

				// Udkommentér dette for at teste med ejer af to felter
				// Field[] fields = board.getFields();
				//
				// for (Field fieldN : fields) {
				// if(fieldN.getType() == "Street") {
				// ((Street) fieldN).setOwner(players[0]);
				// }
				// }

				for (int i = 0; i < players.length; i++) {
					if (players[i].isJailed()) {
						players[i].setJailed(false);
						break;
					}

					gui_controller.showMessage("Spiller " + (i + 1) + "s tur \n Tryk [OK] for at slå med terningerne");

					dice.rollDice();
					int faceValue = dice.getFaceValue();
					gui_controller.setDie(faceValue);

					int field = players[i].getFieldNo();

					int newFieldNo = field + faceValue;

					if (newFieldNo < 24) {
						players[i].setFieldNo(newFieldNo);
						gui_controller.movePlayers(players);
					} else {
						newFieldNo -= 24;
						players[i].setFieldNo(newFieldNo);
						gui_controller.movePlayers(players);
						gui_controller.showMessage("Du kørte over start og modtog derfor 3,-");
						if (newFieldNo != 0) {
							players[i].addPoints(3);
						}
					}

					board.getField(newFieldNo).landOnField(players[i]);

					if (board.getField(newFieldNo).getType() == "Chancekort") {
						gui_controller.showMessage(((Chance) board.getField(newFieldNo)).getCardDescription());
						gui_controller.movePlayers(players);
					} else if (board.getField(newFieldNo).getFieldNo() == 18) {
						gui_controller.showMessage("Gå i fængsel");
						gui_controller.movePlayers(players);
					} else if (board.getField(newFieldNo).getType() == "Street") {
						if (!players[i].equals(((Street) board.getField(newFieldNo)).getOwner())) {
							if (checkOwner(newFieldNo)) {
								gui_controller.showMessage(((Street) board.getField(newFieldNo)).getOwner().getName() + " Ejer to af samme felter, og du betaler derfor dobbelt husleje.");
								board.getField(newFieldNo).landOnField(players[i]); // Betaler igen
							} else {
								gui_controller.showMessage("Du betalte " + ((Street) board.getField(newFieldNo)).getOwner().getName() + " "+ ((Street) board.getField(newFieldNo)).getRent() + ",- for opholdet.");
							}
						}
					}

					 if(board.getField(newFieldNo).getType() == "Street") {
					 if(((Street) board.getField(newFieldNo)).getOwner() ==
					 players[i]) {
					 gui_controller.setOwner(players[i], newFieldNo);
					 }
					 }

					gui_controller.updateBalance(players);

					// Tjek om spilleren er bankerot
					// Hvis der er en der er bankerot, så stoppes while loopet
					if (players[i].isBankrupt()) {
						runGame = false;
					}

				}
			}
			getWinner();
		} else {

			gameSetup();

		}
	}

	public void getWinner() {
		int highscore = 0;
		Player winner = null;
		for (int i = 0; i < players.length; i++) {
			if (!players[i].isBankrupt()) {
				if (players[i].getPoints() > highscore) {
					winner = players[i];
					highscore = players[i].getPoints();
				}
			}

		}
		gui_controller.showMessage(winner.getName() + " har vundet med " + winner.getPoints() + ",-");
	}

	public boolean checkOwner(int fieldNo) {
		boolean sameOwner = false;

		Field field = board.getField(fieldNo);
		Field[] fields = board.getFields();

		for (Field fieldN : fields) {
			if (fieldN.getType() == "Street" && ((Street) field).getGroup() == ((Street) fieldN).getGroup()) {
				if (!fieldN.equals(field)) {
					if (((Street) field).getOwner() != null && ((Street) field).getOwner().equals(((Street) fieldN).getOwner())) {
						sameOwner = true;
					}
				}
			}
		}
		return sameOwner;

	}

}
