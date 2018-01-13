package Controller;

import java.awt.Color;

import Game.Buyable;
import Game.Field;
import Game.Player;
import Game.Start;
import Game.Street;
import gui_fields.GUI_Brewery;
import gui_fields.GUI_Chance;
import gui_fields.GUI_Field;
import gui_fields.GUI_Jail;
import gui_fields.GUI_Ownable;
import gui_fields.GUI_Player;
import gui_fields.GUI_Refuge;
import gui_fields.GUI_Shipping;
import gui_fields.GUI_Start;
import gui_fields.GUI_Street;
import gui_fields.GUI_Tax;
import gui_main.GUI;

public class GUI_Controller {

	private GUI_Field[] fields;
	private GUI gui;

	public void setupGUI(Field[] fields) {
		createGUIFields(fields);
		this.gui = new GUI(this.fields);
	}

	public void createGUIFields(Field[] fields) {
				
		GUI_Field[] GUIFields = new GUI_Field[fields.length];

		for (Field field : fields) {

			if (field.getType() == "Game.Street") {

				GUI_Street s = new GUI_Street();

				GUIFields[field.getFieldNo()] = s;//farven af rammen af alle felter
				
				GUIFields[field.getFieldNo()].setTitle(field.getName());
				GUIFields[field.getFieldNo()].setSubText("Pris: " + ((Street) field).getPrice() + ",- ");
				GUIFields[field.getFieldNo()].setDescription("Leje: " + ((Street) field).getRent());
				
				switch(((Street) field).getGroup().toString()) {
					case "LIGHTBLUE":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.BLUE);
						break;
					case "PINK":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.PINK);
						break;
					case "LIGHTGREEN":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.GREEN);
						break;
					case "LIGHTGREY":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.GRAY);
						break;
					case "RED":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.RED);
						break;
					case "WHITE":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.WHITE);
						break;
					case "YELLOW":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.YELLOW);
						break;
					case "PURPLE":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.decode("#551A8B"));
						break;
					case "BLACK":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.BLACK);
						GUIFields[field.getFieldNo()].setForeGroundColor(Color.WHITE);
						break;
					case "BROWN":
						GUIFields[field.getFieldNo()].setBackGroundColor(Color.MAGENTA);
						break;
				}
						
				
			} else if (field.getType() == "Game.Jail") {

				GUIFields[field.getFieldNo()] = new GUI_Jail();
				GUIFields[field.getFieldNo()].setTitle(field.getName());
				GUIFields[field.getFieldNo()].setSubText("Fængsel");

			} else if (field.getType() == "Game.Chance") {

				GUIFields[field.getFieldNo()] = new GUI_Chance();
				GUIFields[field.getFieldNo()].setBackGroundColor(Color.BLACK);
				GUIFields[field.getFieldNo()].setForeGroundColor(Color.WHITE);

			} else if (field.getType() == "Game.Start") {

				GUIFields[field.getFieldNo()] = new GUI_Start();
				GUIFields[field.getFieldNo()].setTitle("Start");
				GUIFields[field.getFieldNo()].setBackGroundColor(new Color(255, 0, 0));
				GUIFields[field.getFieldNo()].setForeGroundColor(new Color(255, 255, 255));
				GUIFields[field.getFieldNo()].setSubText(((Start) field).getBonus() + ",-");
				
			} else if (field.getType() == "Game.Parking") {

				GUIFields[field.getFieldNo()] = new GUI_Refuge();
				GUIFields[field.getFieldNo()].setSubText("0 kr.");
				
			} else if (field.getType() == "Game.Ferry") {

				GUIFields[field.getFieldNo()] = new GUI_Shipping();
				
				GUIFields[field.getFieldNo()].setTitle(field.getName());
				GUIFields[field.getFieldNo()].setDescription(field.getName());
				GUIFields[field.getFieldNo()].setSubText(((Buyable) field).getPrice() + " kr.");
				
			} else if (field.getType() == "Game.Beverage") {

				GUIFields[field.getFieldNo()] = new GUI_Brewery();
				GUIFields[field.getFieldNo()].setDescription(field.getName());
				GUIFields[field.getFieldNo()].setTitle(field.getName());
				GUIFields[field.getFieldNo()].setSubText(((Buyable) field).getPrice() + " kr.");
				
			} else if (field.getType() == "Game.GovernmentTax" || field.getType() == "Game.IncomeTax") {

				GUIFields[field.getFieldNo()] = new GUI_Tax();
				GUIFields[field.getFieldNo()].setDescription(field.getName());
				GUIFields[field.getFieldNo()].setTitle(field.getName());
				
			}
			
		}
		
		this.fields = GUIFields;
	}

	public void addPlayers(Player[] players) {
		
		Color[] colors = {Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.RED};
		int i = 0;
		for (Player player : players) {

			GUI_Player GUI_player = new GUI_Player(player.getName(), player.getPoints());
			gui.addPlayer(GUI_player);
			player.setGUI_player(GUI_player);
			player.getGUI_player().getCar().setPrimaryColor(colors[i]);
			
			i++;
		}

		movePlayers(players);
	}
	
	public GUI_Field getField(int fieldNo) {
		return fields[fieldNo];
	}
	
	public void movePlayers(Player[] players) {
		
		for(GUI_Field field : fields) {
			field.removeAllCars();
		}
		
		for(Player player : players) {
			GUI_Field field = getField(player.getFieldNo());
			field.setCar(player.getGUI_player(), true);
		}
	}
	
	public void updateBalance(Player[] players) {
		for(Player player : players) {
			player.getGUI_player().setBalance(player.getPoints());
		}
	}
	
	public void setDice(int faceValue1, int faceValue2) {
		gui.setDice(faceValue1, faceValue2);
	}
	
	public String getUserInput(String msg) {
		return gui.getUserString(msg);
	}
	
	public void showMessage(String msg) {
		gui.showMessage(msg);
	}
	
	public void displayChanceCard(String msg) {
		gui.displayChanceCard(msg);
	}
	
	public void setOwner(Player player, int newFieldNo) {
		Color primaryColor;
		if (player != null) {
			primaryColor = player.getGUI_player().getPrimaryColor();
		} else {
			primaryColor = Color.GRAY;
		}
		GUI_Field field = getField(newFieldNo);
		if (field instanceof GUI_Ownable) {
			((GUI_Ownable) field).setBorder(primaryColor);
		} else {
			System.out.println("Wrong usage of GUI - trying to set owner on non ownable field " + field);
		}
		
	}
	
	public String getPlayerAmount(String text, String[] options) {
		return gui.getUserSelection(text, options);
	}
	
	public String multipleChoice(String message, String[] buttonNames) {
		return gui.getUserButtonPressed(message, buttonNames);
	}
	
	public void updateGUIField(int fieldNo, String method, String updatedValue) {

		switch(method) {
			case "subText":
				getField(fieldNo).setSubText(updatedValue);
				break;
			//Tilføj flere hvis der skulle komme brug for det.
		}
	}

	public void setHouses(int fieldNo, int houses) {
		if(getField(fieldNo) instanceof GUI_Street) {
			((GUI_Street) getField(fieldNo)).setHouses(houses);
		}
	}
	
	public void setHotel(int fieldNo, boolean hotel) {
		if(getField(fieldNo) instanceof GUI_Street) {
			((GUI_Street) getField(fieldNo)).setHotel(hotel);
		}
	}
	
}