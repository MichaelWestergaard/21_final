package Controller;

import Game.Board;
import Game.Chance;
import Game.DiceCup;
import Game.Field;
import Game.Player;
import Game.Street;
import Game.Ferry;
import Game.Buyable;

//Vi mangler at lave delen, med at finde ud af hvem der er ejeren.
//Hvis man ejer to felter, skal man betale dobbelt så meget.

public class Game {

	private boolean gameStarted = false;

	private DiceCup diceCup = new DiceCup();
	private Player[] players;
	private GUI_Controller gui_controller = new GUI_Controller();
	private Board board = new Board();

	private String[] playerAmountOptions = {"3", "4", "5", "6"};

	public void gameSetup() {

		// Create fields + board
		board.createFields();

		gui_controller.setupGUI(board.getFields());

		int playerAmount = Integer.parseInt(gui_controller.getPlayerAmount("Vælg antallet af spillere", playerAmountOptions));

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

				for (int i = 0; i < players.length; i++) {
					if (players[i].isJailed()) {
						players[i].setJailed(false);
						break;
					}
					
					// Tjekker om spilleren sidder i f�ngsel
					if (players[i].isJailed() == true) {
						gui_controller.showMessage("Spiller " + (i + 1) + " sidder i f�ngsel. \n Tryk [OK] for at v�lge, hvordan du vil forts�tte");
						//board.getField(players[i].getFieldNo()).landOnField(players[i]);
						
						// Tjekker om spilleren har mulighed for at v�lge, hvordan han vil komme ud af f�ngslet
						if (players[i].getJailCounter() < 3 && players[i].getPoints() >= 1000) {
							String[] options = {"Sl� 2 ens", "Betal kaution"};
							String optionsChoice = gui_controller.multipleChoice("Vil du pr�ve at sl� 2 ens med terningerne eller betale din kaution p� kr. 1000,00?", options);
							
							// Hvis spilleren v�lger at pr�ve at sl� 2 ens med terningerne
							if (options[0].matches(optionsChoice)) {
								rollDice();
								
								if (diceCup.getDiceValue(0) == diceCup.getDiceValue(1)) {
									gui_controller.showMessage("Tillykke! Du slog 2 ens, og er blevet l�sladt fra f�ngslet!");
									players[i].setJailed(false);
									players[i].setJailCounter(0);
								} else {
									gui_controller.showMessage("Du slog ikke 2 ens. Bedre held n�ste gang! \n Du har nu " + (3 - players[i].getJailCounter()) + " fors�g tilbage, f�r du skal betale kaution");
									players[i].increaseJailCounter();
									break;
								}
							
							// Hvis spilleren v�lger at betale kaution
							} else if (options[1].matches(optionsChoice)) {
								gui_controller.showMessage("Du betaler nu kr. 1000,00 i kaution.");
								players[i].addPoints(-1000);
								players[i].setJailed(false);
							}
						
						// Hvis spilleren er tvunget til at betale kaution	
						} else if (players[i].getJailCounter() == 3 && players[i].getPoints() >= 1000) {
							gui_controller.showMessage("Du har opbrugt alle dine fors�g med terningerne, og er tvunget til at betale kaution. \n Kr. 1000,00 vil blive trukket fra din konto.");
							players[i].addPoints(-1000);
							players[i].setJailed(false);
						}
					}

					
					String nextAction = gui_controller.getPlayerAmount("Vælg handling", new String[] {"Kast terning", "Køb huse/hoteller", "Pansæt"});

					if(nextAction == "Kast terning") {
						rollDice();
						checkField(players[i]);
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
	
	public void rollDice() {
		diceCup.rollDices();
		gui_controller.setDice(diceCup.getDiceValue(0),diceCup.getDiceValue(1));
	}
	
	public void checkField(Player player) {
		int field = player.getFieldNo();

		int newFieldNo = field + diceCup.getDiceSum();

		if (newFieldNo < 40) {
			player.setFieldNo(newFieldNo);
			gui_controller.movePlayers(players);
		} else {
			newFieldNo -= 40;
			player.setFieldNo(newFieldNo);
			gui_controller.movePlayers(players);
			gui_controller.showMessage("Du kørte over start og modtog derfor 4000,-");
			if (newFieldNo != 0) {
				player.addPoints(4000);
			}
		}

		board.getField(newFieldNo).landOnField(player);

		if (board.getField(newFieldNo).getType() == "Chancekort") {
			gui_controller.showMessage(((Chance) board.getField(newFieldNo)).getCardDescription());
			gui_controller.movePlayers(players);
		} else if (board.getField(newFieldNo).getFieldNo() == 30) {
			gui_controller.showMessage("Gå i fængsel");
			gui_controller.movePlayers(players);
		} else if (board.getField(newFieldNo).getType() == "Street") {
			if (!player.equals(((Street) board.getField(newFieldNo)).getOwner())) {

				//Hvis feltet ikke ejes af nogle kan det købes
				if(((Street) board.getField(newFieldNo)).getOwner() == null) {
					//Køb felt
				} else {
					int amountToPay = ((Street) board.getField(newFieldNo)).getRent();
					
					if(((Street) board.getField(newFieldNo)).getHouse() == 0 && ((Street) board.getField(newFieldNo)).getHotel() == 0) {
						if(checkMonopoly(newFieldNo)) {
							amountToPay = amountToPay * 2;
						}
					}
					gui_controller.showMessage("Du skal betale " + amountToPay + " kr.");
				}
			}
		} else if (board.getField(newFieldNo).getType() == "Ferry") {
			if (!player.equals(((Ferry) board.getField(newFieldNo)).getOwner())) {
				int ownerOwns = getSameGroupAmount(newFieldNo);
				int amountToPay = ownerOwns * ((Ferry) board.getField(newFieldNo)).getRent();

				gui_controller.showMessage(amountToPay + " Skal betales.");
			}
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

	public int getSameGroupAmount(int fieldNo) {
		int sameGroupAmount = 0;

		Field field = board.getField(fieldNo);
		Field[] fields = board.getFields();

		for (Field fieldN : fields) {
			if(fieldN.getType() == field.getType() && ((Buyable) fieldN).getGroup() == ((Buyable) field).getGroup()) {
				sameGroupAmount++;
			}
		}

		return sameGroupAmount;
	}

	public boolean checkMonopoly(int fieldNo) {
		boolean monopoly = false;

		Field field = board.getField(fieldNo);
		String fieldGroup = ((Street) field).getGroup().name();

		if("LIGHTBLUE".equalsIgnoreCase(fieldGroup) || "PURPLE".equalsIgnoreCase(fieldGroup)) {
			if(getSameGroupAmount(fieldNo) == 2) {
				monopoly = true;
			}	
		}

		if(	"ORANGE".equalsIgnoreCase(fieldGroup) || "LIGHTGREEN".equalsIgnoreCase(fieldGroup) || "LIGHTGREY".equalsIgnoreCase(fieldGroup) || "RED".equalsIgnoreCase(fieldGroup) || "WHITE".equalsIgnoreCase(fieldGroup) || "YELLOW".equalsIgnoreCase(fieldGroup) ) {
			if(getSameGroupAmount(fieldNo) == 3) {
				monopoly = true;
			}
		}
		return monopoly;
	}


}
