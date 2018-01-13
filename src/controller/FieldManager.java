package controller;

import entities.Beverage;
import entities.Buyable;
import entities.Card;
import entities.Chance;
import entities.Ferry;
import entities.MoneyCard;
import entities.MoveCard;
import entities.Parking;
import entities.Player;
import entities.Street;
import entities.Taxation;

public class FieldManager {
	
	private GUI_Controller gui_controller;
	private Board board;
	private PropertyManager propertyManager;
	private Game game;
		
	public FieldManager(GUI_Controller gui_controller, Board board, PropertyManager propertyManager, Game game) {
		this.gui_controller = gui_controller;
		this.board = board;
		this.propertyManager = propertyManager;
		this.game = game;
	}

	public void checkField(Player player) {
		int newFieldNo = player.getFieldNo();

		getFieldLandedOn(player, newFieldNo);
	}

	private void getFieldLandedOn(Player player, int newFieldNo) {
		
		if (board.getField(newFieldNo).getType() == "entities.Chance") {
			
			landedOnChance(player, newFieldNo);
			
		} else if (board.getField(newFieldNo).getFieldNo() == 30) {
			
			landedOnJail(player);
			
		} else if (board.getField(newFieldNo).getFieldNo() == 0) {
			
			player.addPoints(4000);
			
		} else if (board.getField(newFieldNo).getType() == "entities.Street") {
			
			landedOnStreet(player, newFieldNo);
			
		} else if (board.getField(newFieldNo).getType() == "entities.Ferry") {
			
			landedOnFerry(player, newFieldNo);
			
		} else if (board.getField(newFieldNo).getType() == "entities.Beverage") {
			
			landedOnBeverage(player, newFieldNo);
			
		} else if (board.getField(newFieldNo).getFieldNo() == 38) {
						
			landedOnTax(player, newFieldNo);
			
		} else if (board.getField(newFieldNo).getFieldNo() == 4) {
			
			landedOnIncomeTax(player, newFieldNo);

		} else if (board.getField(newFieldNo).getType() == "entities.Parking") {
			landedOnParking(player);
		}
	}

	private void landedOnTax(Player player, int newFieldNo) {
		gui_controller.showMessage("Ekstraordinær statsskat, betal 2000");
		player.addPoints(((Taxation) board.getField(newFieldNo)).getTax() * -1);
		((Parking) board.getField(20)).increaseAmount(2000);
		gui_controller.updateGUIField(20, "subText", ((Parking) board.getField(20)).getAmount() + " kr.");
	}

	private void landedOnParking(Player player) {
		gui_controller.showMessage("Du har landet på parkeringsfeltet og modtager " + ((Parking) board.getField(20)).getAmount() + " kr."  );
		player.addPoints(((Parking) board.getField(20)).getAmount());
		((Parking) board.getField(20)).setAmount(0);
		gui_controller.updateGUIField(20, "subText", ((Parking) board.getField(20)).getAmount() + " kr.");
	}

	private void landedOnChance(Player player, int newFieldNo) {
		Chance currentField = ((Chance) board.getField(newFieldNo));
		currentField.createCardList();
		Card drawncard = currentField.getCard();
		
		System.out.println(((Chance) board.getField(newFieldNo)).getCardDescription());
		
		if(drawncard instanceof MoneyCard) {
			player.addPoints(((MoneyCard) drawncard).getAmount());

			System.out.println(((MoneyCard) drawncard).getAmount());
		} else if(drawncard instanceof MoveCard) {

			System.out.println(((MoveCard) drawncard).getField());
			if (((MoveCard) drawncard).getField() < 0) {
				if (player.getFieldNo() < 3) {
					player.setFieldNo(player.getFieldNo() + 40 + ((MoveCard) drawncard).getField());
				} else {
					player.setFieldNo(player.getFieldNo() + ((MoveCard) drawncard).getField());
				}
			} else if(((MoveCard) drawncard).getField() == 10) {
				player.setJailed(true);
				player.setFieldNo(((MoveCard) drawncard).getField());
			} else {
				player.setFieldNo(((MoveCard) drawncard).getField());
			}
		}
		
		gui_controller.showMessage("Tryk [OK] for at trække et chancekort.");
		gui_controller.displayChanceCard(((Chance) board.getField(newFieldNo)).getCardDescription());
		game.movePlayers();
	}

	private void landedOnJail(Player player) {
		if (player.getFieldNo() == 30) {
			player.setFieldNo(10);
			player.setJailed(true);
		}
		
		gui_controller.showMessage("Gå i fængsel");
		game.movePlayers();
	}

	private void landedOnIncomeTax(Player player, int newFieldNo) {
		String[] options = {"Betal 4000", "Betal 10%"};
		String optionsChoice = gui_controller.multipleChoice("Vil du betale 4000 eller 10 %?", options);
		if(options[0].matches(optionsChoice)){
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
				}
			}
			
			((Parking) board.getField(20)).increaseAmount(playerTotalValue/100*10);
			player.addPoints((playerTotalValue/100*10)*-1);
			gui_controller.updateGUIField(20, "subText", ((Parking) board.getField(20)).getAmount() + " kr.");
		}
	}

	private void landedOnBeverage(Player player, int newFieldNo) {
		int beverageRent = ((Beverage) board.getField(newFieldNo)).getRent();

		Player owner = ((Buyable) board.getField(newFieldNo)).getOwner();
		
		if(!player.equals(owner)) {
			
			if(((Beverage) board.getField(newFieldNo)).getOwner() == null) {
				String[] options = {"Køb felt", "Spring over"};
				String optionsChoice = gui_controller.multipleChoice("Vil du købe feltet?", options);

				if(options[0].matches(optionsChoice)) {
					if(player.getPoints() >= ((Buyable) board.getField(newFieldNo)).getPrice()) {
						player.addPoints(((Buyable) board.getField(newFieldNo)).getPrice() * -1);
						((Buyable) board.getField(newFieldNo)).setOwner(player);
						gui_controller.setOwner(player, newFieldNo);
					}
				}
				
			} else {
				if( ((Buyable) board.getField(12)).getOwner() == ((Buyable) board.getField(28)).getOwner() ) {
					beverageRent = beverageRent * 2;	
				}
				player.addPoints(beverageRent * -1);
				((Street) board.getField(newFieldNo)).getOwner().addPoints(beverageRent);
				gui_controller.showMessage("Du skal betale " + beverageRent + " kr.");
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
			} else {
				
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
				
				player.addPoints(amountToPay*-1);
				owner.addPoints(amountToPay);
				
				gui_controller.showMessage("Du betaler " + amountToPay + " kr til " + owner.getName() + ", da han ejer " + ownerOwns + " " + ending);
				
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
						player.addPoints(((Buyable) board.getField(newFieldNo)).getPrice() * -1);
						((Buyable) board.getField(newFieldNo)).setOwner(player);
						gui_controller.setOwner(player, newFieldNo);
					}
				}

			} else {
				int amountToPay = ((Street) board.getField(newFieldNo)).getRent();

				if(((Street) board.getField(newFieldNo)).getHouse() == 0) {
					if(propertyManager.checkMonopoly(newFieldNo)) {
						amountToPay = amountToPay * 2;
					}
					player.addPoints(amountToPay * -1);
					((Street) board.getField(newFieldNo)).getOwner().addPoints(amountToPay);
				}
				gui_controller.showMessage("Du skal betale " + amountToPay + " kr.");
				
			}

		}
	}
	
}