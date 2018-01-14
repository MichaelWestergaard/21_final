package controller;

import entities.Buyable;
import entities.Field;
import entities.Player;
import entities.Street;

public class PropertyManager {
	
	private GUI_Controller gui_controller;
	private Board board;
	
	public PropertyManager(GUI_Controller gui_controller, Board board) {
		super();
		this.gui_controller = gui_controller;
		this.board = board;
	}

	public void manageHousesAndHotels(Player player) {
		String[] optionsBuySell = {"Tilbage", "Køb", "Sælg"};
		String buySellChoice = gui_controller.multipleChoice("Vil du købe eller sælge?", optionsBuySell);
		
		int[] ownedFieldNumbers = player.getOwnedFieldNumbers();
		int[] ownedStreetNumbers = new int[ownedFieldNumbers.length];
		int numberOfStreets = 0;
		
		//Sorter alle "Street" i et nyt array
		for(int j = 0; j < ownedFieldNumbers.length; j++) {
			if(board.getField(ownedFieldNumbers[j]).getType() == "entities.Street") {
				ownedStreetNumbers[numberOfStreets] = ownedFieldNumbers[j];
				numberOfStreets++;
			}
		}
		
		String[] ownedStreetNames = new String[numberOfStreets];
		
		for(int j = 0; j < numberOfStreets; j++) {
			ownedStreetNames[j] = board.getField(ownedStreetNumbers[j]).getName();
		}
		
		if(ownedStreetNames.length > 0) {
			String chosenStreetName = gui_controller.getPlayerAmount("Hvilket felt vil du administrere?", ownedStreetNames);
			
			//Bestem det tilh�rende fieldNo
			Field[] fields = board.getFields();
			int chosenStreetNumber = 0;
			
			for(int j = 0; j < fields.length; j++) {
				if(fields[j].getName() == chosenStreetName) {
					chosenStreetNumber = fields[j].getFieldNo();
					break;
				}
			}
			
			boolean evenlyDistributed = false;
			int houseDifference = 0;
			int groupAmount = getOwnerGroupAmount(chosenStreetNumber);
			int[] sameGroupHouses = new int[groupAmount];
			int groupHousesIndex = 0;
			int chosenStreetHouse = ((Street) board.getField(chosenStreetNumber)).getHouse();
							
			for(int j = 0; j < groupAmount; j++) {
				if(((Street) board.getField(chosenStreetNumber)).getGroup() == ((Street) board.getField(ownedStreetNumbers[j])).getGroup()) {
					sameGroupHouses[groupHousesIndex] = ((Street) board.getField(ownedStreetNumbers[j])).getHouse();
					groupHousesIndex++;
				}
			}
			
			if (optionsBuySell[0].matches(buySellChoice)) {
				
			} else if(optionsBuySell[1].matches(buySellChoice)) {
				
				String[] optionsHouseHotel = {"Tilbage", "Hus", "Hotel"};
				String houseHotelChoice = gui_controller.multipleChoice("Hvad vil du bygge?", optionsHouseHotel);
				
				//K�b hus eller hotel
				if(optionsHouseHotel[0].matches(houseHotelChoice)) {
					
				} else if(optionsHouseHotel[1].matches(houseHotelChoice)) {
					buyHouse(player, chosenStreetNumber, evenlyDistributed, groupAmount, sameGroupHouses, chosenStreetHouse);
				} else if(optionsHouseHotel[2].matches(houseHotelChoice)) {
					buyHotel(player, chosenStreetNumber, houseDifference, groupAmount, sameGroupHouses,	chosenStreetHouse);
				}

				
			} else if (optionsBuySell[2].matches(buySellChoice)) {

				String[] optionsHouseHotel = {"Tilbage", "Hus", "Hotel"};
				String choiceHouseHotel = gui_controller.multipleChoice("Hvad vil du sælge", optionsHouseHotel);
				
				//S�lg hus og hotel
				if(optionsHouseHotel[0].matches(choiceHouseHotel)) {
					
				} else if(optionsHouseHotel[1].matches(choiceHouseHotel)) {
					sellHouse(player, chosenStreetNumber, evenlyDistributed, groupAmount, sameGroupHouses, chosenStreetHouse);
				} else if(optionsHouseHotel[2].matches(choiceHouseHotel)) {
					sellHotel(player, chosenStreetNumber);
				}
			}
		} else {
			gui_controller.showMessage("Du ejer ingen ejendomme.");
		}
		
	}

	private void sellHotel(Player player, int chosenStreetNumber) {
		if(((Street) board.getField(chosenStreetNumber)).getHouse() == 5) {
			player.addPoints(((Street) board.getField(chosenStreetNumber)).getHousePrice());
			((Street) board.getField(chosenStreetNumber)).sellHotel();
			gui_controller.setHotel(chosenStreetNumber, false);
			gui_controller.setHouses(chosenStreetNumber, 4);	
		}
		
		else {
			gui_controller.showMessage("Du ejer intet hotel på denne grund.");
		}
	}

	private void sellHouse(Player player, int chosenStreetNumber, boolean evenlyDistributed, int groupAmount, int[] sameGroupHouses, int chosenStreetHouse) {
		int houseDifference;
		if(((Street) board.getField(chosenStreetNumber)).getHouse() > 0 && ((Street) board.getField(chosenStreetNumber)).getHouse() < 5) {
			if(groupAmount == 2) {
				int maxNumberHouse = Math.max(sameGroupHouses[0], sameGroupHouses[1]);
				int minNumberHouse = Math.min(sameGroupHouses[0], sameGroupHouses[1]);
				houseDifference = maxNumberHouse - minNumberHouse;
				
				if(houseDifference < 2) {
					if(chosenStreetHouse == maxNumberHouse) {
						evenlyDistributed = true;
					}
				} else if(houseDifference == 0) {
					evenlyDistributed = true;
				}
			}
			 
			else if(groupAmount == 3) {
				int maxNumberHouse = Math.max(sameGroupHouses[0], Math.max(sameGroupHouses[1], sameGroupHouses[2]));
				int minNumberHouse = Math.min(sameGroupHouses[0], Math.min(sameGroupHouses[1], sameGroupHouses[2]));
				houseDifference = maxNumberHouse - minNumberHouse;
				
				if(houseDifference < 2) {
					if(chosenStreetHouse == maxNumberHouse) {
						evenlyDistributed = true;
					}	
				} else if(houseDifference == 0) {
					evenlyDistributed = true;
				}
			}
		
			if(evenlyDistributed) {
				player.addPoints(((Street) board.getField(chosenStreetNumber)).getHousePrice());
				((Street) board.getField(chosenStreetNumber)).sellHouse();
				gui_controller.setHouses(chosenStreetNumber, chosenStreetHouse - 1);	
			}
			
			else {
				gui_controller.showMessage("Du kan ikke sælge huse på denne grund, da de skal være fordelt ligeligt mellem grundene af samme farve. Sælg fra en anden grund.");
			}	
		}
		
		else {
			gui_controller.showMessage("Du ejer ikke nogen huse på denne grund.");
		}
	}

	private void buyHotel(Player player, int chosenStreetNumber, int houseDifference, int groupAmount, int[] sameGroupHouses, int chosenStreetHouse) {
		if(groupAmount == 2) {
			int minNumberHouse = Math.min(sameGroupHouses[0], sameGroupHouses[1]);
			houseDifference = minNumberHouse - chosenStreetHouse;
		}
		
		if(groupAmount == 3) {
			int minNumberHouse = Math.min(sameGroupHouses[0], Math.min(sameGroupHouses[1], sameGroupHouses[2]));
			houseDifference = minNumberHouse - chosenStreetHouse;
			
		}
		
		if(chosenStreetHouse == 4 && houseDifference >= 0) {
			if(player.getPoints() >= ((Street) board.getField(chosenStreetNumber)).getHousePrice()) {
				player.addPoints(((Street) board.getField(chosenStreetNumber)).getHousePrice() * -1);
				((Street) board.getField(chosenStreetNumber)).buyHotel();
				gui_controller.setHouses(chosenStreetNumber, 0);
				gui_controller.setHotel(chosenStreetNumber, true);
			}
			
			else {
				gui_controller.showMessage("Du har ikke råd til at købe et hotel.");
			}
							
		}
		
		else {
			gui_controller.showMessage("Du skal have 4 huse på en grund for at kunne bygge et hotel.");
		}
	}

	private void buyHouse(Player player, int chosenStreetNumber, boolean evenlyDistributed, int groupAmount, int[] sameGroupHouses, int chosenStreetHouse) {
		int houseDifference;
		if(checkMonopoly(chosenStreetNumber)) {
			if(((Street) board.getField(chosenStreetNumber)).getHouse() < 4) {
				
				if(groupAmount == 2) {
					int maxNumberHouse = Math.max(sameGroupHouses[0], sameGroupHouses[1]);
					int minNumberHouse = Math.min(sameGroupHouses[0], sameGroupHouses[1]);
					
					houseDifference = maxNumberHouse - minNumberHouse;
					
					if(houseDifference < 2) {
						if(chosenStreetHouse == minNumberHouse) {
							evenlyDistributed = true;
						}
					} else if(houseDifference == 0) {
						evenlyDistributed = true;
					}
				}
				
				else if(groupAmount == 3) {
					int maxNumberHouse = Math.max(chosenStreetHouse, Math.max(sameGroupHouses[0], sameGroupHouses[1]));
					int minNumberHouse = Math.min(chosenStreetHouse, Math.min(sameGroupHouses[0], sameGroupHouses[1]));
					houseDifference = maxNumberHouse - minNumberHouse;
					
					if(houseDifference < 2) {
						if(chosenStreetHouse == minNumberHouse) {
							evenlyDistributed = true;
						}	
					} else if(houseDifference == 0) {
						evenlyDistributed = true;
					}
				}
				
				if(evenlyDistributed) {
					if(player.getPoints() >= ((Street) board.getField(chosenStreetNumber)).getHousePrice()) {
						player.addPoints(((Street) board.getField(chosenStreetNumber)).getHousePrice() * -1);
						((Street) board.getField(chosenStreetNumber)).buyHouse();
						gui_controller.setHouses(chosenStreetNumber, chosenStreetHouse + 1);
					}
					
					else {
						gui_controller.showMessage("Du har ikke råd til at købe et hus");
					}
				}
			
				else {
					gui_controller.showMessage("Du skal fordele husene ligeligt mellem grundene af samme farve");
				}
			}
			else {
				gui_controller.showMessage("Du kan maksimalt have 4 huse på én grund.");
			}
		}
		
		else {
			gui_controller.showMessage("Du skal eje alle felter af samme farve for at kunne bygge huse på et af dem.");
		}
	}
	
	public int getOwnerGroupAmount(int fieldNo) {
		int ownerGroupAmount = 0;

		Field field = board.getField(fieldNo);
		Field[] fields = board.getFields();

		if(field.getType() == "entities.Street") {
			for (Field fieldN : fields) {
				if(fieldN instanceof Buyable) {
					if(((Buyable) fieldN).getOwner() == ((Buyable) field).getOwner() && ((Buyable) fieldN).getGroup() == ((Buyable) field).getGroup()) {
						ownerGroupAmount++;
					}
				}
			}
		} else if(field.getType() == "entities.Ferry") {
			for (Field fieldN : fields) {
				if(fieldN instanceof Buyable) {
					if(((Buyable) fieldN).getOwner() == ((Buyable) field).getOwner() && ((Buyable) fieldN).getGroup() == ((Buyable) field).getGroup()) {
						ownerGroupAmount++;
					}
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

		if(	"PINK".equalsIgnoreCase(fieldGroup) || "LIGHTGREEN".equalsIgnoreCase(fieldGroup) || "LIGHTGREY".equalsIgnoreCase(fieldGroup) || "RED".equalsIgnoreCase(fieldGroup) || "WHITE".equalsIgnoreCase(fieldGroup) || "YELLOW".equalsIgnoreCase(fieldGroup) ) {
			if(getOwnerGroupAmount(fieldNo) == 3) {
				monopoly = true;
			}
		}
		return monopoly;
	}
	
}