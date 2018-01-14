package controller;

import entities.Buyable;
import entities.DiceCup;
import entities.Field;
import entities.Player;
import entities.Street;

public class Game {

	private boolean gameStarted = false;
	boolean runGame = true;
	private boolean turnSkipped = false;
	private int skippedTurn = 0;

	public Player[] players;
	private DiceCup diceCup = new DiceCup();
	private GUI_Controller gui_controller = new GUI_Controller();
	private Board board = new Board();
	private PropertyManager propertyManager = new PropertyManager(gui_controller, board);
	private FieldManager fieldManager = new FieldManager(gui_controller, board, propertyManager, this, diceCup);
	
	public void gameSetup() {

		// Create fields + board
		board.createFields();

		gui_controller.setupGUI(board.getFields());
		
		String[] playerAmountOptions = {"3", "4", "5", "6"};
		
		int playerAmount = Integer.parseInt(gui_controller.getPlayerAmount("Vælg antallet af spillere", playerAmountOptions));

		players = new Player[playerAmount];
		
		int addCounter = 0;

		for (int i = 0; i < playerAmount; i++) {
			String name = gui_controller.getUserInput("Spiller " + (i + 1) + " vælger sit navn:");
			
			// Hvis det er spiller nummer 2 (eller derover), der skal tilf�jes
			if(addCounter != 0) {
				boolean nameTaken = false;
				
				// Tjekker om det indtastede navn er optaget
				for(int j = 0; j < addCounter; j++) {
					if(players[j].getName().matches(name) == true) {
						nameTaken = true;
					}
				}
				
				// Hvis navnet ikke er optaget
				if(nameTaken == false) {
					players[i] = new Player(name, 30000);
					addCounter++;
					
				// Hvis navnet er optaget
				} else {
					gui_controller.showMessage("Navnet: " + name + " er allerede taget. Prøv igen!");
					i--;
				}
			
			//Hvis det er f�rste spiller, der skal tilf�jes	
			} else {
				players[i] = new Player(name, 30000);
				addCounter++;
			}
		}

		gui_controller.addPlayers(players);
		
		gameStarted = true;
		play();
	}

	private void play() {

		// N�r en har tabt, skal der t�lles hvem har flest penge. N�r det er
		// talt, skal
		// konsollen udv�lge med en besked hvem der har vundet
		if (!gameStarted) {
			gameSetup();
		} else {

			while (runGame) {

				for (int i = 0; i < players.length; i++) {
					if(turnSkipped) {
						i = skippedTurn;
						turnSkipped = false;
					}
					
					Player player = players[i];

					playerActions(player);
				}
			}
			getWinner();
		}
	}

	private void playerActions(Player player) {
		
		if (player.isJailed()) {
			playerJailed(player);
		} else {
			gui_controller.updateBalance(players);
			
			String nextAction = gui_controller.multipleChoice(player.getName() + "'s tur - Vælg handling", new String[] {"Kast terning", "Administrer Ejendomme"});

			if(nextAction == "Kast terning") {
				rollDice();

				moveAnimation(player);
				
				//Hvis spillerens sl�r 2 ens
				checkEqualDice(player);
			
			} else if (nextAction == "Administrer Ejendomme") {

				String propertyAction = gui_controller.multipleChoice("Vælg handling til administration af dine ejendomme:", new String[] {"Huse/Hoteller", "Pantsæt Ejendom", "Auktionér Ejendom"});

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

	private void moveAnimation(Player player) {
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
						player.addPoints(4000);
						movePlayers();
						gui_controller.showMessage("Du kørte over start og modtog derfor 4000,-");
						gui_controller.updateBalance(players);
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
				gui_controller.showMessage("Du har slået 2 ens for mange gange og fængsles for at snyde med terningerne!");								
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

	private void playerJailed(Player player) {

		gui_controller.showMessage(player.getName() + " sidder i fængsel. \n Tryk [OK] for at vælge, hvordan du vil fortsætte");
		//board.getField(players[i].getFieldNo()).landOnField(players[i]);

		// Tjekker om spilleren har mulighed for at v�lge, hvordan han vil komme ud af f�ngslet
		if (player.getJailCounter() < 3 && player.getPoints() >= 1000 && player.getJailCard() > 0) {
			String[] options = {"Slå 2 ens", "Betal kaution", "Brug fængselskort"};
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
				gui_controller.showMessage("Du betaler nu 1000 kr i kaution.");
				player.addPoints(-1000);
				player.releaseFromJail();
				playerActions(player);

				// Hvis spilleren v�lger at bruge sit f�ngselskort	
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
					gui_controller.showMessage("Tillykke! Du slog 2 ens, og er blevet løsladt fra føngslet!");
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

	private void pledgeSequence(Player player) {
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
								if (player.getPoints() >= ((Buyable) board.getField(ownedFieldNumbers[j])).getPledgePrice() ) { //tjekker om han r�d
									((Buyable) board.getField(ownedFieldNumbers[j])).setPledged(false); //sat til at v�re ikke pansat
									player.addPoints(-1 * (((Buyable) board.getField(ownedFieldNumbers[j])).getPledgePrice() + (((Buyable) board.getField(ownedFieldNumbers[j])).getPledgePrice() / 100 * 10))  ); // betaler prisen
									
									gui_controller.updateGUIField(ownedFieldNumbers[j], "subText", "Pris: " + ((Buyable) board.getField(ownedFieldNumbers[j])).getPrice() + ",-");
								} else {
									gui_controller.showMessage("Du har ikke råd til at købe grunden tilbage");
								}

							}

						} else {
							String[] options = {"Pansæt", "Gå tilbage"};
							String optionsChoice = gui_controller.multipleChoice("Vil du forsætte?", options);
							if (options[0].matches(optionsChoice)) {
								((Buyable) board.getField(ownedFieldNumbers[j])).setPledged(true); //sat til at v�re pansat
								player.addPoints(((Buyable) board.getField(ownedFieldNumbers[j])).getPledgePrice() ); // modtager bel�b prisen
								gui_controller.updateGUIField(ownedFieldNumbers[j], "subText", "Pantsat");
							} 
						}								
					} 
				}
			}
		} else {
			gui_controller.showMessage("Du ejer ingen ejendomme");
		}

		gui_controller.updateBalance(players);
	}
	
	private void auctionSequence(Player player) {
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

			int skipCounter = 0;
			
			for (int j = 1; j <= ownedFieldNumbers.length; j++) {
				if(board.getField(ownedFieldNumbers[j - 1]) != board.getField(0)) {
					ownedStreetOptions[j - skipCounter] = board.getField(ownedFieldNumbers[j - 1]).getName();
				} else {
					skipCounter++;
				}
			}


			String chosenStreetName = gui_controller.getPlayerAmount("Hvilken ejendom vil du sælge?", ownedStreetOptions);

			// Hvis spilleren v�lger at lade v�re med at s�lge noget alligevel
			if (chosenStreetName.matches(ownedStreetOptions[0])) {
			
			// Hvis spilleren v�lger en ejendom, der skal s�lges	
			} else {
				Field chosenStreet = board.getFieldFromName(chosenStreetName);
				int chosenStreetNumber = chosenStreet.getFieldNo();
				
				if (chosenStreet != null) {
					
					boolean hasHouses = false; 
					
					if(chosenStreet instanceof Street) {
						int houses = 0;
						if(propertyManager.checkMonopoly(chosenStreetNumber)) {
							
							int[] ownedFields = player.getOwnedFieldNumbers();
							int[] ownedStreetNumbers = new int[ownedFieldNumbers.length];
							int numberOfStreets = 0;
							
							//Sorter alle "Street" i et nyt array
							for(int j = 0; j < ownedFields.length; j++) {
								if(board.getField(ownedFields[j]).getType() == "entities.Street") {
									if(((Buyable) board.getField(ownedFields[j])).getGroup() == ((Buyable) chosenStreet).getGroup()) {
										ownedStreetNumbers[numberOfStreets] = ownedFields[j];
										numberOfStreets++;
									}
								}
							}
							
							int groupAmount = propertyManager.getOwnerGroupAmount(chosenStreetNumber);
											
							for(int j = 0; j < groupAmount; j++) {
								if(((Buyable) chosenStreet).getGroup() == ((Buyable) board.getField(ownedStreetNumbers[j])).getGroup()) {
									houses += ((Street) board.getField(ownedStreetNumbers[j])).getHouse();
								}
							}
														
						}
						
						if(houses > 0) {
							hasHouses = true;
						} else {
							hasHouses = false;
						}
						
					}
					
					if(!hasHouses) {
						Field chosenField = board.getFieldFromName(chosenStreetName);					
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
						
						int auctionTurn = 0;
						
						while(auctioning == true) {
							if(auctionTurn == biddingPlayers.length) {
								auctionTurn = 0;
							}
							
							for(int i = auctionTurn; i < biddingPlayers.length; i++) {
								int currentBid = 0;
								
								if(biddingPlayers[i].getPoints() >= highestBid) {
									try {
										int proposedBid = Integer.parseInt(gui_controller.getUserInput(biddingPlayers[i].getName() + ", hvor meget vil du byde på " + chosenStreetName + "?"
																								+ "\n Mindst mulige bud = " + highestBid + "kr."
																								+ "\n Indtast en lavere værdi, for at forlade auktionen."));
										if(biddingPlayers[i].getPoints() >= proposedBid) {
											currentBid = proposedBid;
										} else {
											gui_controller.showMessage(biddingPlayers[i].getName() + ", du har ikke penge nok til at byde dette beløb."
																	+ "\n Du byder nu automatisk alle dine kontanter - undtagen en 50'er."
																	+ "\n Hvis det automatiske bud ikke er h�jt nok, fjernes du fra auktionen.");
											currentBid = biddingPlayers[i].getPoints() - 50;										
										}
									} catch (Exception e) {
										gui_controller.showMessage("Fejl: Du skal indtaste et helt, positivt tal, når du byder.");
										break;
									}
								} else {
									gui_controller.showMessage(biddingPlayers[i].getName() + ", du har ikke penge nok til at deltage i auktionen og fjernes nu derfra.");
								}
								
								if(highestBid <= currentBid) {
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
								
								auctionTurn++;
							}
						}
						
						String[] finalAuctionOptions = {"Ja", "Nej"};
						String finalAuctionChoice = gui_controller.multipleChoice(player.getName() + ", vil du sælge " + chosenStreetName 
																									+ " til " + highestBidder.getName() 
																									+ " for: " + highestBid + ",-.?", finalAuctionOptions);
						
						// Hvis k�beren v�lger at fuldf�re auktionen
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
							
						// Hvis k�beren v�lger at annullere auktionen	
						} else if(finalAuctionChoice.matches(finalAuctionOptions[1])) {
							
						}
					} else {
						gui_controller.showMessage("Ejendommen, eller andre af samme type, har huse på sig.\nDisse skal sælges før du kan auktionere dette felt.");
					}
				}
			}								
		} else {
			gui_controller.showMessage("Du ejer ingen ejendomme.");
		}

		gui_controller.updateBalance(players);
	}
	
	private void rollDice() {
		diceCup.rollDices();
		gui_controller.setDice(diceCup.getDiceValue(0),diceCup.getDiceValue(1));
	}

	private void getWinner() {
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
			String[] options = {"Forlad Spillet", "Pantsæt ejendomme", "Auktionær Ejendomme"};
			String choice = gui_controller.multipleChoice("Du er løbet tør for penge!! \n Du har nu følgende muligheder:", options);
			
			// Hvis spilleren v�lger at forlade spillet
			if(choice.matches(options[0])) {
				// Frig�r alle spillerens ejendomme
				int[] ownedFields = player.getOwnedFieldNumbers();
				
				for(int i = 0; i < ownedFields.length; i++) {
					Field currentField = board.getField(ownedFields[i]);
					
					if(currentField != board.getField(0)) {
						((Buyable) currentField).resetOwner(player);

					// GUI'en fjerner spilleren som owner af feltet
					gui_controller.setOwner(null, currentField.getFieldNo());
					}			
				}
				
				// Fjerner spilleren fra spillet
				player.getGUI_player().setName(player.getName() + " [Forladt]");
				
				Player[] newPlayers = new Player[players.length - 1];
				int addCounter = 0;
				
				for(int i = 0; i < players.length; i++) {
					if(players[i] != player) {
						newPlayers[addCounter] = players[i];
						addCounter++;
					}
					
					if(players[i] == player) {
						if(i != players.length - 1) {
							turnSkipped = true;
							skippedTurn = i;
						}
					}
				}
				
				players = new Player[newPlayers.length];
				players = newPlayers;
				
				if(players.length == 1) {
					runGame = false;
				}
			
			// Hvis spilleren v�lger at pants�tte en ejendom	
			} else if(choice.matches(options[1])) {
				pledgeSequence(player);
				checkBankrupt(player);
				
			// Hvis spilleren v�lger at auktionere en ejendom	
			} else if(choice.matches(options[2])) {
				auctionSequence(player);
				checkBankrupt(player);
			}
			
		}
	}
	
}