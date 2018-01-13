package controller;

import entities.Buyable;
import entities.DiceCup;
import entities.Field;
import entities.Player;

public class Game {

	private boolean gameStarted = false;
		
	private DiceCup diceCup = new DiceCup();
	private Player[] players;
	private GUI_Controller gui_controller = new GUI_Controller();
	private Board board = new Board();
	private PropertyManager propertyManager = new PropertyManager(gui_controller, board);
	private FieldManager fieldManager = new FieldManager(diceCup, gui_controller, board, propertyManager, this);
	
	public void gameSetup() {

		// Create fields + board
		board.createFields();

		gui_controller.setupGUI(board.getFields());
		
		String[] playerAmountOptions = {"3", "4", "5", "6"};
		
		int playerAmount = Integer.parseInt(gui_controller.getPlayerAmount("Vælg antallet af spillere", playerAmountOptions));

		players = new Player[playerAmount];

		for (int i = 0; i < playerAmount; i++) {
			String name = gui_controller.getUserInput("Spiller " + (i + 1) + " vælger sit navn:");
			players[i] = new Player(name, 30000);
		}

		gui_controller.addPlayers(players);
		
		gameStarted = true;
		play();
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

					playerActions(player);
					
					checkBankrupt(player);
					
					if(players.length == 1) {
						runGame = false;
					}
				}
			}
			getWinner();
		}
	}

	public void playerActions(Player player) {
		
		if (player.isJailed()) {
			playerJailed(player);
		} else {
			gui_controller.updateBalance(players);
			
			String nextAction = gui_controller.getPlayerAmount(player.getName() + "'s tur - Vælg handling", new String[] {"Kast terning", "Administrer Ejendomme"});

			if(nextAction == "Kast terning") {
				rollDice();

				moveAnimation(player);
				
				//Hvis spillerens slår 2 ens
				checkEqualDice(player);
			
			} else if (nextAction == "Administrer Ejendomme") {

				String propertyAction = gui_controller.getPlayerAmount("Vælg handling til administration af dine ejendomme:", new String[] {"Huse/Hoteller", "Pantsæt Ejendom", "Auktionér Ejendom"});

				if(propertyAction == "Huse/Hoteller") {

					propertyManager.manageHousesAndHotels(player);
					
					//Stadig spillerens tur
					playerActions(player);
					
				} else if(propertyAction == "Pantsæt Ejendom") {

					pledgeSequence(player);
					
					//Stadig spillerens tur
					playerActions(player);

				} else if(propertyAction == "Auktionér Ejendom") {
					auctionSequence(player);
					
					//Stadig spillerens tur
					playerActions(player);
				}
			}

			gui_controller.updateBalance(players);
		}
		
	}

	public void moveAnimation(Player player) {
		int field = player.getFieldNo();

		int newFieldNo = field + diceCup.getDiceSum();
		if (newFieldNo > 39) {
			newFieldNo -= 40;
		}
		
		for (int i = 0; i < diceCup.getDiceSum(); i++) {
			if(player.getFieldNo() + 1 > 39) {
				player.setFieldNo(39 - player.getFieldNo());
				if(player.getFieldNo() == 0) {
					if (newFieldNo != 0) {
						movePlayers();
						player.addPoints(4000);
						gui_controller.showMessage("Du kørte over start og modtog derfor 4000,-");
					}
				}
			} else {
				player.setFieldNo(player.getFieldNo() + 1);
			}
			movePlayers();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void movePlayers() {
		gui_controller.movePlayers(players);
	}
	
	private void checkEqualDice(Player player) {
		if (diceCup.getDiceValue(0) == diceCup.getDiceValue(1)) {
			player.increaseHitDouble();

			// Hvis spilleren har sl�et 2 ens for mange (3) gange
			if (player.getHitDouble() == 3) {
				gui_controller.showMessage("Du har sl�et 2 ens for mange gange og fængsles for at snyde med terningerne!");								
				player.setFieldNo(10);
				player.setJailed(true);
				gui_controller.movePlayers(players);
				player.resetHitDouble();
			} else {
				fieldManager.checkField(player);
				//Giver spilleren en eksta tur
				playerActions(player);
			}

		} else {
			player.resetHitDouble();
			fieldManager.checkField(player);
		}
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
					playerActions(player);
				} else {
					gui_controller.showMessage("Du slog ikke 2 ens. Bedre held næste gang! \n Du har nu " + (3 - player.getJailCounter()) + " forsøg tilbage, før du skal betale kaution");
					player.increaseJailCounter();
				}

				// Hvis spilleren v�lger at betale kaution
			} else if (options[1].matches(optionsChoice)) {
				gui_controller.showMessage("Du betaler nu 1000 kr i kaution.");
				player.addPoints(-1000);
				player.releaseFromJail();
				playerActions(player);

				// Hvis spilleren vælger at bruge sit fængselskort	
			} else if (options[2].matches(optionsChoice)) {
				gui_controller.showMessage("Du har brugt dit kort, og bliver hermed løsladt.");
				player.setJailCard(-1);
				player.releaseFromJail();
				playerActions(player);
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
					playerActions(player);
				} else {
					gui_controller.showMessage("Du slog ikke 2 ens. Bedre held næste gang! \n Du har nu " + (3 - player.getJailCounter()) + " forsøg tilbage, før du skal betale kaution");
					player.increaseJailCounter();
				}

				// Hvis spilleren v�lger at betale kaution
			} else if (options[1].matches(optionsChoice)) {
				gui_controller.showMessage("Du betaler nu 1000 kr. i kaution.");
				player.addPoints(-1000);
				player.releaseFromJail();
				playerActions(player);
			}

			// Hvis spilleren er tvunget til at betale kaution	
		} else if (player.getJailCounter() == 3 && player.getPoints() >= 1000) {
			gui_controller.showMessage("Du har opbrugt alle dine forsøg med terningerne, og er tvunget til at betale kaution. \n 1000 kr. vil blive trukket fra din konto.");
			player.addPoints(-1000);
			player.releaseFromJail();
			playerActions(player);
		}
	}

	public void pledgeSequence(Player player) {
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
									player.addPoints(-1 * (((Buyable) board.getField(ownedFieldNumbers[j])).getPledgePrice() + (((Buyable) board.getField(ownedFieldNumbers[j])).getPledgePrice() / 100 * 10))  ); // betaler prisen
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

		gui_controller.updateBalance(players);
	}
	
	public void auctionSequence(Player player) {
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
//					if ( ((Buyable) chosenField).isPledged()) {
//						gui_controller.showMessage("Du har valgt at sælge " + chosenStreetName + "." + "\n Ejendommen er pantsat, så du modtager den halve købspris " + ((Buyable) chosenField).getPledgePrice() + " kr.");
//						player.addPoints(((Buyable) chosenField).getPledgePrice());
//					} else {
//						gui_controller.showMessage("Du har valgt at sælge " + chosenStreetName + "." + "\n Du modtager nu " + ((Buyable) chosenField).getPrice() + " kr.");
//						player.addPoints(((Buyable) chosenField).getPrice());
//					}
					
					
					Player[] biddingPlayers = new Player[players.length - 1];
					int addCounter = 0;
					
					for(int i = 0; i < players.length; i++) {					
						if(players[i] != player) {
							biddingPlayers[addCounter] = players[i];
							addCounter++;
						}
					}
					
					Player highestBidder = null;
					boolean auctioning = true;
					int highestBid = 0;
					
					if(((Buyable) chosenField).isPledged()) {
						highestBid = ((Buyable) chosenField).getPledgePrice();
					} else {
						highestBid = ((Buyable) chosenField).getPrice();
					}
					
					
					while(auctioning == true) {
						for(int i = 0; i < biddingPlayers.length; i++) {
							int currentBid = 0;
							
							try {
								currentBid = Integer.parseInt(gui_controller.getUserInput(biddingPlayers[i].getName() + ", hvor meget vil du byde på " + chosenStreetName + "?"
																							+ "\n Mindst mulige bud = " + highestBid + "kr."
																							+ "\n Indtast en lavere værdi, for at forlade auktionen."));
							} catch (Exception e) {
								gui_controller.showMessage("Fejl: Du skal indtaste et helt, positivt tal, når du byder.");
								break;
							}
							
							if(highestBid < currentBid) {
								highestBid = currentBid;
								highestBidder = biddingPlayers[i];
							} else {
								// Fjerner spilleren fra auktionen
								Player[] newBiddingPlayers = new Player[biddingPlayers.length - 1];
								int addCounter2 = 0;
								
								for(int j = 0; j < biddingPlayers.length; j++) {					
									if(biddingPlayers[j] != biddingPlayers[i]) {
										newBiddingPlayers[addCounter2] = biddingPlayers[j];
										addCounter2++;
									}
								}
								
								biddingPlayers = new Player[newBiddingPlayers.length];
								biddingPlayers = newBiddingPlayers;
							}
							
							if(biddingPlayers.length == 1) {
								highestBidder = biddingPlayers[0];
								auctioning = false;
								break;
							}
						}
					}
					
					String[] finalAuctionOptions = {"Ja", "Nej"};
					String finalAuctionChoice = gui_controller.multipleChoice(player.getName() + ", vil du sælge " + chosenStreetName + " for: " + highestBid + ",-.?", finalAuctionOptions);
					
					// Hvis køberen vælger at fuldføre auktionen
					if(finalAuctionChoice.matches(finalAuctionOptions[0])) {
						if ( ((Buyable) chosenField).isPledged()) {
							gui_controller.showMessage(highestBidder.getName() + " har valgt at købe den pantsatte ejendom: " + chosenStreetName + 
														".\n" + player.getName() + " modtager nu " + highestBid + "kr. fra " + highestBidder.getName());						
						} else {
							gui_controller.showMessage(highestBidder.getName() + " har valgt at købe ejendommen: " + chosenStreetName + 
														".\n" + player.getName() + " modtager nu " + highestBid + "kr. fra " + highestBidder.getName());							
		
						}
						
						player.addPoints(highestBid);
						((Buyable) chosenField).resetOwner(player);
	
						for(int i = 0; i < players.length; i++) {
							if(players[i] == highestBidder) {
								players[i].addPoints(highestBid * -1);
			
								((Buyable) chosenField).setOwner(players[i]);
								gui_controller.setOwner(players[i], chosenField.getFieldNo());
							}
						}
						
					// Hvis køberen vælger at annullere auktionen	
					} else if(finalAuctionChoice.matches(finalAuctionOptions[1])) {
						
					}					
				} else {
					gui_controller.showMessage("Fejl: spillet kunne ikke finde den ønskede ejendom.");
				}
			}								
		} else {
			gui_controller.showMessage("Du ejer ingen ejendomme.");
		}

		gui_controller.updateBalance(players);
	}
	
	public void rollDice() {
		diceCup.rollDices();
		gui_controller.setDice(diceCup.getDiceValue(0),diceCup.getDiceValue(1));
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

	public void checkBankrupt(Player player) {
		if(player.getPoints() <= 0) {
			player.setBankrupt(true);			
			String[] options = {"Forlad Spillet", "Pantsæt ejendomme", "Auktionér Ejendomme"};
			String choice = gui_controller.multipleChoice("Du er gået løbet tør for penge!! \n Du har nu følgende muligheder:", options);
			
			// Hvis spilleren vælger at forlade spillet
			if(choice.matches(options[0])) {
				// Frigør alle spillerens ejendomme
				int[] ownedFields = player.getOwnedFieldNumbers();
				
				for(int i = 0; i < ownedFields.length; i++) {
					Field currentField = board.getField(ownedFields[i]);					
					((Buyable) currentField).resetOwner(player);

					// GUI'en fjerner spilleren som owner af feltet
					gui_controller.setOwner(null, currentField.getFieldNo());
				}
				
				// Fjerner spilleren fra spillet
				Player[] newPlayers = new Player[players.length - 1];
				int addCounter = 0;
				
				for(int i = 0; i < players.length; i++) {					
					if(players[i] != player) {
						newPlayers[addCounter] = players[i];
						addCounter++;
					}
				}
				
				players = new Player[newPlayers.length];
				players = newPlayers;
			
			// Hvis spilleren vælger at pantsætte en ejendom	
			} else if(choice.matches(options[1])) {
				pledgeSequence(player);
				checkBankrupt(player);
				
			// Hvis spilleren vælger at auktionere en ejendom	
			} else if(choice.matches(options[2])) {
				auctionSequence(player);
				checkBankrupt(player);
			}
			
		}
	}
	
}