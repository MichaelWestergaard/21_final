package Controller;

import Game.Board;
import Game.Chance;
import Game.DiceCup;
import Game.Field;
import Game.GovernmentTax;
import Game.IncomeTax;
import Game.Parking;
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
					
					// Tjekker om spilleren sidder i f�ngsel
					if (players[i].isJailed() == true) {
						gui_controller.showMessage("Spiller " + (i + 1) + " sidder i f�ngsel. \n Tryk [OK] for at v�lge, hvordan du vil forts�tte");
						//board.getField(players[i].getFieldNo()).landOnField(players[i]);
						
						// Tjekker om spilleren har mulighed for at v�lge, hvordan han vil komme ud af f�ngslet
						if (players[i].getJailCounter() < 3 && players[i].getPoints() >= 1000 && players[i].getJailCard() > 0) {
							String[] options = {"Sl� 2 ens", "Betal kaution", "Brug fængselskort"};
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
								players[i].setJailCounter(0);
								players[i].setJailed(false);
								
							// Hvis spilleren vælger at bruge sit fængselskort	
							} else if (options[2].matches(optionsChoice)) {
								gui_controller.showMessage("Du har brugt dit kort, og bliver hermed løsladt.");
								players[i].setJailCard(-1);
								players[i].setJailCounter(0);
								players[i].setJailed(false);
							}
						
						} else if (players[i].getJailCounter() < 3 && players[i].getPoints() >= 1000 && players[i].getJailCard() == 0) {
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
								players[i].setJailCounter(0);
							}
							
						// Hvis spilleren er tvunget til at betale kaution	
						} else if (players[i].getJailCounter() == 3 && players[i].getPoints() >= 1000) {
							gui_controller.showMessage("Du har opbrugt alle dine fors�g med terningerne, og er tvunget til at betale kaution. \n Kr. 1000,00 vil blive trukket fra din konto.");
							players[i].addPoints(-1000);
							players[i].setJailed(false);
							players[i].setJailCounter(0);
						}
					}

					
					// Tjek om spilleren er bankerot
					// Hvis der er en der er bankerot, så stoppes while loopet
					if (players[i].isBankrupt()) {
						runGame = false;
					}
					
					String nextAction = gui_controller.getPlayerAmount("Vælg handling", new String[] {"Kast terning", "Administrer Ejendomme"});

					if(nextAction == "Kast terning") {
						rollDice();
						
						// Hvis spillerens sl�r 2 ens
						if (diceCup.getDiceValue(0) == diceCup.getDiceValue(1)) {
							players[i].increaseHitDouble();
							
							// Hvis spilleren har sl�et 2 ens for mange (3) gange
							if (players[i].getHitDouble() == 3) {
								gui_controller.showMessage("Du har sl�et 2 ens for mange gange og f�ngsles for at snyde med terningerne!");								
								players[i].setFieldNo(6);
								players[i].setJailed(true);
								gui_controller.movePlayers(players);
							} else {
								checkField(players[i]);
							}
							
							// Giver spilleren en eksta tur
							if (i == 0) {
								i = players.length;
							} else {
								i--;
							}
							
						} else {
							players[i].resetHitDouble();
							checkField(players[i]);
						}
					} else if (nextAction == "Administrer Ejendomme") {
						
						String propertyAction = gui_controller.getPlayerAmount("Vælg handling til administration af dine ejendomme:", new String[] {"Huse/Hoteller", "Pantsæt Ejendom", "Sælg Ejendom"});
						
						if(propertyAction == "Huse/Hoteller") {
						
						} else if(propertyAction == "Pantsæt") {
							int[] ownedFieldNumbers = players[i].getOwnedFieldNumbers();
							
							int ownedStreets = 0;
							
							for (int j = 0; j < ownedFieldNumbers.length; j++) {
								if(ownedFieldNumbers[j] != 0) {
									ownedStreets++;
								}
							}
							
							if(ownedStreets > 0 ) {

								String[] ownedStreetOptions = new String [ownedStreets];
								
								for (int j = 0; j < ownedFieldNumbers.length; j++) {
									if(ownedFieldNumbers[j] != 0) {
										ownedStreetOptions[j] = board.getField(ownedFieldNumbers[j]).getName();	
									}					
								}
								
								String chosenStreet = gui_controller.getPlayerAmount("Hvad vil du pansætte?", ownedStreetOptions);
								//Kald metode til at pantsætte chosenStreet
							} else {
								gui_controller.showMessage("Du ejer ingen ejendomme");
								
								//Man skal ikke kunne miste din tur her...
							}
						} else if(propertyAction == "Sælg Ejendom") {
							
						}
					
					}

					gui_controller.updateBalance(players);
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
		
		if (board.getField(newFieldNo).getType() == "Game.Chance") {
			gui_controller.showMessage(((Chance) board.getField(newFieldNo)).getCardDescription());
			gui_controller.movePlayers(players);
		} else if (board.getField(newFieldNo).getFieldNo() == 30) {
			gui_controller.showMessage("Gå i fængsel");
			gui_controller.movePlayers(players);
		} else if (board.getField(newFieldNo).getType() == "Game.Street") {
			if (!player.equals(((Street) board.getField(newFieldNo)).getOwner())) {
		
				//Hvis feltet ikke ejes af nogle kan det købes
				if(((Street) board.getField(newFieldNo)).getOwner() == null) {
					
					String[] options = {"Køb felt", "Spring over"};
					String optionsChoice = gui_controller.multipleChoice("Vil du købe feltet?", options);
					
					if(options[0].matches(optionsChoice)) {
						if(player.getPoints() >= ((Buyable) board.getField(newFieldNo)).getPrice()) {
							((Street) board.getField(newFieldNo)).landOnField(player, false, true);
							gui_controller.setOwner(player, newFieldNo);
						}
					}
					
				} else {
					((Street) board.getField(newFieldNo)).landOnField(player, true, false);
					int amountToPay = ((Street) board.getField(newFieldNo)).getRent();
					
					if(((Street) board.getField(newFieldNo)).getHouse() == 0 && ((Street) board.getField(newFieldNo)).getHotel() == 0) {
						if(checkMonopoly(newFieldNo)) {
							((Street) board.getField(newFieldNo)).landOnField(player, true, false);
							amountToPay = amountToPay * 2;
						}
					}
					gui_controller.showMessage("Du skal betale " + amountToPay + " kr.");
				}
			}
		} else if (board.getField(newFieldNo).getType() == "Game.Ferry") {
			if (!player.equals(((Ferry) board.getField(newFieldNo)).getOwner())) {
				int ownerOwns = getSameGroupAmount(newFieldNo);
				int amountToPay = ownerOwns * ((Ferry) board.getField(newFieldNo)).getRent();

				gui_controller.showMessage(amountToPay + " Skal betales.");
			}
			//government tax
		} else if (board.getField(newFieldNo).getFieldNo() == 38) {
			System.out.println(((Parking) board.getField(20)).getAmount());
			gui_controller.showMessage("Ekstraordinær statsskat, betal 2000");
			((GovernmentTax) board.getField(newFieldNo)).landOnField(player);
			((Parking) board.getField(20)).increaseAmount(2000);
			System.out.println(((Parking) board.getField(20)).getAmount());
		} else if (board.getField(newFieldNo).getFieldNo() == 4) {
			//incometax
			//Hvis man lander på incometax, skal man betale 4000 eller 10%
			//pengene skal blive ført over til parkeringen (kig på game, hvordan man har gjort det)
			
			System.out.println(player.getPoints());
			String[] options = {"Betal 4000", "Betal 10%"};
			String optionsChoice = gui_controller.multipleChoice("Vil du betale 4000 eller 10 %?", options);
			gui_controller.showMessage("Ekstraordinær statsskat, betal 4000 eller 10 %");
			System.out.println(((Parking) board.getField(20)).getAmount());
			if(options[0].matches(optionsChoice)){
				((IncomeTax) board.getField(newFieldNo)).landOnField(player);
				((Parking) board.getField(20)).increaseAmount(4000);	
				player.addPoints(-4000);
				System.out.println(((Parking) board.getField(20)).getAmount());
			}
			if(options[1].matches(optionsChoice)){
				System.out.println(((Parking) board.getField(20)).getAmount());
				((Parking) board.getField(20)).increaseAmount(player.getPoints()/100*10);
				player.addPoints((player.getPoints()/100*10)*-1);
				System.out.println(((Parking) board.getField(20)).getAmount());
			}

		} else if (board.getField(newFieldNo).getType() == "Game.Parking") {
			gui_controller.showMessage("Du landede på parkeringsfeltet, og modtager derfor: " + ((Parking) board.getField(20)).getAmount() + " kr.");
			((Parking) board.getField(20)).setAmount(0);
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

	
	
	public int getOwnerGroupAmount(int fieldNo) {
		int ownerGroupAmount = 0;

		Field field = board.getField(fieldNo);
		Field[] fields = board.getFields();

		if(field.getType() == "Street") {
			for (Field fieldN : fields) {
				if(((Buyable) fieldN).getOwner() == ((Buyable) field).getOwner() && ((Buyable) fieldN).getGroup() == ((Buyable) field).getGroup()) {
					ownerGroupAmount++;
				}
			}
		}
		return ownerGroupAmount;
	}
	
	
	
	public boolean checkMonopoly(int fieldNo) {
		boolean monopoly = false;
		
		Field field = board.getField(fieldNo);
		String fieldGroup = ((Street) field).getGroup().name();

		if("LIGHTBLUE".equalsIgnoreCase(fieldGroup) || "PURPLE".equalsIgnoreCase(fieldGroup)) {
			if(getOwnerGroupAmount(fieldNo) == 2) {
				monopoly = true;
			}	
		}

		if(	"ORANGE".equalsIgnoreCase(fieldGroup) || "LIGHTGREEN".equalsIgnoreCase(fieldGroup) || "LIGHTGREY".equalsIgnoreCase(fieldGroup) || "RED".equalsIgnoreCase(fieldGroup) || "WHITE".equalsIgnoreCase(fieldGroup) || "YELLOW".equalsIgnoreCase(fieldGroup) ) {
			if(getOwnerGroupAmount(fieldNo) == 3) {
				monopoly = true;
			}
		}
		return monopoly;
	}

}
