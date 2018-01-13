package controller;

import entities.Beverage;
import entities.Buyable;
import entities.Chance;
import entities.DiceCup;
import entities.Ferry;
import entities.IncomeTax;
import entities.Parking;
import entities.Player;
import entities.Street;

public class FieldManager {
	
	private DiceCup diceCup;
	private GUI_Controller gui_controller;
	private Board board;
	private PropertyManager propertyManager;
	private Game game;
		
	public FieldManager(DiceCup diceCup, GUI_Controller gui_controller, Board board, PropertyManager propertyManager, Game game) {
		this.diceCup = diceCup;
		this.gui_controller = gui_controller;
		this.board = board;
		this.propertyManager = propertyManager;
		this.game = game;
	}

	public void checkField(Player player) {
		int newFieldNo = player.getFieldNo();

		board.getField(newFieldNo).landOnField(player);

		getFieldLandedOn(player, newFieldNo);
	}

	private void getFieldLandedOn(Player player, int newFieldNo) {
		
		if (board.getField(newFieldNo).getType() == "entities.Chance") {
			gui_controller.showMessage("Tryk [OK] for at trække et chancekort.");
			gui_controller.displayChanceCard(((Chance) board.getField(newFieldNo)).getCardDescription());
			game.movePlayers();
			
		} else if (board.getField(newFieldNo).getFieldNo() == 30) {
			
			gui_controller.showMessage("Gå i fængsel");
			game.movePlayers();
			
		} else if (board.getField(newFieldNo).getType() == "entities.Street") {
			
			landedOnStreet(player, newFieldNo);
			
		} else if (board.getField(newFieldNo).getType() == "entities.Ferry") {
			
			landedOnFerry(player, newFieldNo);
			
		} else if (board.getField(newFieldNo).getType() == "entities.Beverage") {
			
			landedOnBeverage(player, newFieldNo);
			
		} else if (board.getField(newFieldNo).getFieldNo() == 38) {
			
			gui_controller.showMessage("Ekstraordinær statsskat, betal 2000");
			((Parking) board.getField(20)).increaseAmount(2000);
			gui_controller.updateGUIField(20, "subText", ((Parking) board.getField(20)).getAmount() + " kr.");
			
		} else if (board.getField(newFieldNo).getFieldNo() == 4) {
			
			landedOnIncomeTax(player, newFieldNo);

		} else if (board.getField(newFieldNo).getType() == "entities.Parking") {
			gui_controller.showMessage("Du har landet på parkeringsfeltet og modtager " + ((Parking) board.getField(20)).getAmount() + " kr."  );
			((Parking) board.getField(20)).setAmount(0);
			gui_controller.updateGUIField(20, "subText", ((Parking) board.getField(20)).getAmount() + " kr.");
		}
	}

	private void landedOnIncomeTax(Player player, int newFieldNo) {
		String[] options = {"Betal 4000", "Betal 10%"};
		String optionsChoice = gui_controller.multipleChoice("Vil du betale 4000 eller 10 %?", options);
		if(options[0].matches(optionsChoice)){
			((IncomeTax) board.getField(newFieldNo)).landOnField(player);
			((Parking) board.getField(20)).increaseAmount(4000);	
			player.addPoints(-4000);
			gui_controller.updateGUIField(20, "subText", ((Parking) board.getField(20)).getAmount() + " kr.");
		}
		if(options[1].matches(optionsChoice)){
			int playerTotalValue = player.getPoints();
			int[] ownedFieldNumbers = player.getOwnedFieldNumbers();

			for (int j = 0; j < ownedFieldNumbers.length; j++) {
				if(ownedFieldNumbers[j] != 0) {
					playerTotalValue += ((Buyable) board.getField(ownedFieldNumbers[j])).getPrice();
//						playerTotalValue += ((Street) board.getField(ownedFieldNumbers[j])).getHouse() * ((Street) board.getField(ownedFieldNumbers[j])).getHousePrice();
				}
			}
			
			((Parking) board.getField(20)).increaseAmount(playerTotalValue/100*10);
			player.addPoints((playerTotalValue/100*10)*-1);
			gui_controller.updateGUIField(20, "subText", ((Parking) board.getField(20)).getAmount() + " kr.");
		}
	}

	private void landedOnBeverage(Player player, int newFieldNo) {
		int beverageRent =  ((Beverage) board.getField(newFieldNo)).getRent();
		
		Player owner = ((Buyable) board.getField(newFieldNo)).getOwner();
		if(!player.equals(owner)) {
			
			if(((Beverage) board.getField(newFieldNo)).getOwner() == null) {
				String[] options = {"Køb felt", "Spring over"};
				String optionsChoice = gui_controller.multipleChoice("Vil du købe feltet?", options);

				if(options[0].matches(optionsChoice)) {
					if(player.getPoints() >= ((Buyable) board.getField(newFieldNo)).getPrice()) {
						((Beverage) board.getField(newFieldNo)).landOnField(player,diceCup.getDiceSum(), beverageRent, false, true);
						gui_controller.setOwner(player, newFieldNo);
					}
				}
				
			} else {
				if( ((Buyable) board.getField(12)).getOwner() == ((Buyable) board.getField(28)).getOwner() ) {
					beverageRent = beverageRent * 2;	
				}
				((Beverage) board.getField(newFieldNo)).landOnField(player, diceCup.getDiceSum(), beverageRent, true, false);
			}
		}
	}

	private void landedOnFerry(Player player, int newFieldNo) {
		Player owner = ((Buyable) board.getField(newFieldNo)).getOwner();
		if(!player.equals(owner)) {
			if(((Ferry) board.getField(newFieldNo)).getOwner() == null) { //Hvis der ikke findes en ejer.
				String[] options = {"Køb felt", "Spring over"};
				String optionsChoice = gui_controller.multipleChoice("Vil du købe feltet?", options);

				if(options[0].matches(optionsChoice)) {
					if(player.getPoints() >= ((Buyable) board.getField(newFieldNo)).getPrice()) {
						player.addPoints(-1 * ((Buyable) board.getField(newFieldNo)).getPrice());
						((Buyable) board.getField(newFieldNo)).setOwner(player);
								gui_controller.setOwner(player, newFieldNo);
					}
				}
			}
			else {
				
				int ownerOwns = propertyManager.getOwnerGroupAmount(newFieldNo);
				int amountToPay = ((Ferry) board.getField(newFieldNo)).getRent();
				
				String ending;
				
				switch (ownerOwns) {
					
					case 2:
						amountToPay = amountToPay * 2;
						break;
					
					case 3:
						amountToPay = amountToPay * 4;
						break;
					
					case 4:
						amountToPay = amountToPay * 8;
						break;

					default:
						
						break;
				}
				
				if(ownerOwns == 1) {
					ending = "færge";
				} else {
					ending = "færger";
				}
				
				gui_controller.showMessage("Du betaler " + amountToPay + " kr til " + owner.getName() + ", da han ejer " + ownerOwns + " " + ending);
				
				player.addPoints(amountToPay*-1);
				owner.addPoints(amountToPay);

			}
		}
	}

	private void landedOnStreet(Player player, int newFieldNo) {
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

				if(((Street) board.getField(newFieldNo)).getHouse() == 0) {
					if(propertyManager.checkMonopoly(newFieldNo)) {
						((Street) board.getField(newFieldNo)).landOnField(player, true, false);
						amountToPay = amountToPay * 2;
					}
				}
				gui_controller.showMessage("Du skal betale " + amountToPay + " kr.");
			}

		}
	}
	
}