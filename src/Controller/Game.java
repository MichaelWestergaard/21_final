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
import Game.Beverage;

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
		if (!gameStarted) {
			gameSetup();
		} else {

			boolean runGame = true;

			while (runGame) {

				for (int i = 0; i < players.length; i++) {
					Player player = players[i];

					if (player.isJailed()) {
						playerJailed(player);
					} else {
						playerActions(player);
					}

				}
			}
			getWinner();
		}
	}

	public void playerActions(Player player) {

		String nextAction = gui_controller.getPlayerAmount(player.getName() + "'s tur - Vælg handling", new String[] {"Kast terning", "Administrer Ejendomme"});

		if(nextAction == "Kast terning") {
			rollDice();

			// Hvis spillerens sl�r 2 ens
			if (diceCup.getDiceValue(0) == diceCup.getDiceValue(1)) {
				player.increaseHitDouble();

				// Hvis spilleren har sl�et 2 ens for mange (3) gange
				if (player.getHitDouble() == 3) {
					gui_controller.showMessage("Du har sl�et 2 ens for mange gange og f�ngsles for at snyde med terningerne!");								
					player.setFieldNo(10);
					player.setJailed(true);
					gui_controller.movePlayers(players);
				} else {
					checkField(player);
				}

				//Giver spilleren en eksta tur
				playerActions(player);

			} else {
				player.resetHitDouble();
				checkField(player);
			}
		} else if (nextAction == "Administrer Ejendomme") {

			String propertyAction = gui_controller.getPlayerAmount("Vælg handling til administration af dine ejendomme:", new String[] {"Huse/Hoteller", "Pantsæt Ejendom", "Sælg Ejendom"});

			if(propertyAction == "Huse/Hoteller") {

				//Køb huse og hoteller

				playerActions(player);

			} else if(propertyAction == "Pantsæt Ejendom") {

				int[] ownedFieldNumbers = player.getOwnedFieldNumbers();

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
					for (int j = 0; j < ownedFieldNumbers.length; j++) {
						if(ownedFieldNumbers[j] != 0) {
							if (board.getField(ownedFieldNumbers[j]).getName() == chosenStreet) {
								if (((Buyable) board.getField(ownedFieldNumbers[j])).isPledged()) {
									String[] options = {"Køb tilbage", "Gå tilbage"};
									String optionsChoice = gui_controller.multipleChoice("Vil du forsætte?", options);
									if (options[0].matches(optionsChoice)) {
										if (player.getPoints() >= ((Buyable) board.getField(ownedFieldNumbers[j])).getPledgePrice() ) { //tjekker om han råd
											((Buyable) board.getField(ownedFieldNumbers[j])).setPledged(false); //sat til at være ikke pansat
											player.addPoints(-1 * ((Buyable) board.getField(ownedFieldNumbers[j])).getPledgePrice() ); // betaler prisen
										} else {
											gui_controller.showMessage("Du har ikke råd til at købe grunden tilbage");
											// gør noget så man ikke msiter turen
										}

									}

								} else {
									String[] options = {"Pansæt", "Gå tilbage"};
									String optionsChoice = gui_controller.multipleChoice("Vil du forsætte?", options);
									if (options[0].matches(optionsChoice)) {
										((Buyable) board.getField(ownedFieldNumbers[j])).setPledged(true); //sat til at være pansat
										player.addPoints(((Buyable) board.getField(ownedFieldNumbers[j])).getPledgePrice() ); // modtager beløb prisen
									} 
								}								
							} 
						}



					}
					//Kald metode til at pantsætte chosenStreet
				} else {
					gui_controller.showMessage("Du ejer ingen ejendomme");

					//Man skal ikke kunne miste din tur her...
				}

				//Stadig spillerens tur
				playerActions(player);

			} else if(propertyAction == "Sælg Ejendom") {
				int[] ownedFieldNumbers = player.getOwnedFieldNumbers();							
				int ownedStreetsCounter = 0;

				for (int j = 0; j < ownedFieldNumbers.length; j++) {
					if(ownedFieldNumbers[j] != 0) {
						ownedStreetsCounter++;
					}
				}

				if(ownedStreetsCounter > 0 ) {

					String[] ownedStreetOptions = new String [ownedStreetsCounter + 1];
					ownedStreetOptions[0] = "Ingen";

					for (int j = 1; j <= ownedStreetsCounter; j++) {
						ownedStreetOptions[j] = board.getField(ownedFieldNumbers[j - 1]).getName();
					}

					String chosenStreetName = gui_controller.multipleChoice("Hvilken ejendom vil du sælge?", ownedStreetOptions);

					// Hvis spilleren vælger at lade være med at sælge noget alligevel
					if (chosenStreetName.matches(ownedStreetOptions[0])) {
						// Hvis spilleren vælger en ejendom, der skal sælges	
					} else {
						if (board.getFieldFromName(chosenStreetName) != null) {
							Field chosenField = board.getFieldFromName(chosenStreetName);

							gui_controller.showMessage("Du har valgt at sælge " + chosenStreetName + "." + "\n Du modtager nu " + ((Buyable) chosenField).getPrice() + " kr.");

							((Street) chosenField).sellField(player);

							// GUI'en ændrer feltets border-farve til grå og opdaterer spillerens point
							gui_controller.setOwner(null, chosenField.getFieldNo());
							gui_controller.updateBalance(players);

						} else {
							gui_controller.showMessage("Fejl: spillet kunne ikke finde den ønskede ejendom.");
						}
					}								
				} else {
					gui_controller.showMessage("Du ejer ingen ejendomme.");
				}

				//Stadig spillerens tur
				playerActions(player);
			}
		}

		gui_controller.updateBalance(players);
	}

	public void playerJailed(Player player) {

		gui_controller.showMessage(player.getName() + " sidder i fængsel. \n Tryk [OK] for at vælge, hvordan du vil fortsætte");
		//board.getField(players[i].getFieldNo()).landOnField(players[i]);

		// Tjekker om spilleren har mulighed for at v�lge, hvordan han vil komme ud af f�ngslet
		if (player.getJailCounter() < 3 && player.getPoints() >= 1000 && player.getJailCard() > 0) {
			String[] options = {"Slå 2 ens", "Betal kaution", "Brug fængselskort"};
			String optionsChoice = gui_controller.multipleChoice("Vil du prøve at slå 2 ens med terningerne eller betale din kaution på 1000 kr?", options);

			// Hvis spilleren vælger at prøve at slå 2 ens med terningerne
			if (options[0].matches(optionsChoice)) {
				rollDice();

				if (diceCup.getDiceValue(0) == diceCup.getDiceValue(1)) {
					gui_controller.showMessage("Tillykke! Du slog 2 ens, og er blevet løsladt fra fængslet!");
					player.releaseFromJail();
				} else {
					gui_controller.showMessage("Du slog ikke 2 ens. Bedre held næste gang! \n Du har nu " + (3 - player.getJailCounter()) + " forsøg tilbage, før du skal betale kaution");
					player.increaseJailCounter();
				}

				// Hvis spilleren v�lger at betale kaution
			} else if (options[1].matches(optionsChoice)) {
				gui_controller.showMessage("Du betaler nu 1000 kr i kaution.");
				player.addPoints(-1000);
				player.releaseFromJail();

				// Hvis spilleren vælger at bruge sit fængselskort	
			} else if (options[2].matches(optionsChoice)) {
				gui_controller.showMessage("Du har brugt dit kort, og bliver hermed løsladt.");
				player.setJailCard(-1);
				player.releaseFromJail();
			}

		} else if (player.getJailCounter() < 3 && player.getPoints() >= 1000 && player.getJailCard() == 0) {
			String[] options = {"Slå 2 ens", "Betal kaution"};
			String optionsChoice = gui_controller.multipleChoice("Vil du prøve at slå 2 ens med terningerne eller betale din kaution på 1000 kr?", options);

			// Hvis spilleren v�lger at pr�ve at sl� 2 ens med terningerne
			if (options[0].matches(optionsChoice)) {
				rollDice();

				if (diceCup.getDiceValue(0) == diceCup.getDiceValue(1)) {
					gui_controller.showMessage("Tillykke! Du slog 2 ens, og er blevet løsladt fra fængslet!");
					player.releaseFromJail();
				} else {
					gui_controller.showMessage("Du slog ikke 2 ens. Bedre held næste gang! \n Du har nu " + (3 - player.getJailCounter()) + " forsøg tilbage, før du skal betale kaution");
					player.increaseJailCounter();
				}

				// Hvis spilleren v�lger at betale kaution
			} else if (options[1].matches(optionsChoice)) {
				gui_controller.showMessage("Du betaler nu 1000 kr. i kaution.");
				player.addPoints(-1000);
				player.releaseFromJail();
			}

			// Hvis spilleren er tvunget til at betale kaution	
		} else if (player.getJailCounter() == 3 && player.getPoints() >= 1000) {
			gui_controller.showMessage("Du har opbrugt alle dine forsøg med terningerne, og er tvunget til at betale kaution. \n 1000 kr. vil blive trukket fra din konto.");
			player.addPoints(-1000);
			player.releaseFromJail();
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
			gui_controller.showMessage("Tryk [OK] for at trække et chancekort.");
			gui_controller.displayChanceCard(((Chance) board.getField(newFieldNo)).getCardDescription());
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



				if(((Ferry) board.getField(newFieldNo)).getOwner() == null) { //Hvis der ikke findes en ejer.
					String[] options = {"Køb felt", "Spring over"};
					String optionsChoice = gui_controller.multipleChoice("Vil du købe feltet?", options);

					if(options[0].matches(optionsChoice)) {
						if(player.getPoints() >= ((Buyable) board.getField(newFieldNo)).getPrice()) {

						}
					}
				}
				else {
					int ownerOwns = getOwnerGroupAmountFerry(newFieldNo);
					int amountToPay = ((Ferry) board.getField(newFieldNo)).getRent();
					switch (ownerOwns) {
					case 2: amountToPay = amountToPay * 2;
					break;

					case 3: amountToPay = amountToPay * 4;
					break;

					case 4: amountToPay = amountToPay * 8;
					break;

					default: 
						amountToPay = amountToPay*1;
					}

					player.addPoints(amountToPay*-1);
					((Ferry) board.getField(newFieldNo)).getOwner().addPoints(amountToPay);

				}
			}

		} else if (board.getField(newFieldNo).getType() == "Game.Beverage") {
			if (!player.equals(((Beverage) board.getField(newFieldNo)).getOwner())) {

				//Hvis feltet ikke ejes af nogle kan det købes
				if(((Beverage) board.getField(newFieldNo)).getOwner() == null) {

					String[] options = {"Køb felt", "Spring over"};
					String optionsChoice = gui_controller.multipleChoice("Vil du købe feltet?", options);

					if(options[0].matches(optionsChoice)) {
						if(player.getPoints() >= ((Buyable) board.getField(newFieldNo)).getPrice()) {
							((Beverage) board.getField(newFieldNo)).landOnField(player,diceCup.getDiceSum(), 100, false, true);
							gui_controller.setOwner(player, newFieldNo);
							//gui_controller.showMessage("Du skal betale " + amountToPay + " kr.");
						}
					}

				} else {
					((Beverage) board.getField(newFieldNo)).landOnField(player, diceCup.getDiceSum(), 100, true, false);
				}

			}
		}else if (board.getField(newFieldNo).getFieldNo() == 38) {
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


	public int getOwnerGroupAmountFerry(int fieldNo) {
		int ownerGroupAmount = 0;

		Field field = board.getField(fieldNo);
		Field[] fields = board.getFields();

		if(field.getType() == "Ferry") {
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