package Controller;

import java.awt.Color;

import Game.Field;
import Game.Player;
import Game.Start;
import Game.Street;
import gui_fields.GUI_Chance;
import gui_fields.GUI_Field;
import gui_fields.GUI_Jail;
import gui_fields.GUI_Ownable;
import gui_fields.GUI_Player;
import gui_fields.GUI_Start;
import gui_fields.GUI_Street;
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
			
			if (field.getType() == "Street") {

				GUI_Street s = new GUI_Street();
				
				GUIFields[field.getFieldNo()] = s;//farven af rammen af alle felter
				
				GUIFields[field.getFieldNo()].setTitle(field.getName());
				GUIFields[field.getFieldNo()].setSubText("Pris: " + ((Street) field).getPrice() + ",- ");
				GUIFields[field.getFieldNo()].setForeGroundColor(Color.black); //tekst farve
				
			} else if (field.getType() == "Jail") {

				GUIFields[field.getFieldNo()] = new GUI_Jail();
				GUIFields[field.getFieldNo()].setTitle(field.getName());

			} else if (field.getType() == "Chancekort") {

				GUIFields[field.getFieldNo()] = new GUI_Chance();

			} else if (field.getType() == "Start") {

				GUIFields[field.getFieldNo()] = new GUI_Start();
				GUIFields[field.getFieldNo()].setTitle("Start");
				GUIFields[field.getFieldNo()].setBackGroundColor(new Color(255, 0 ,0));
				GUIFields[field.getFieldNo()].setSubText("Modtag: " + ((Start) field).getBonus() + ",-");
				
			} else if (field.getType() == "Parking") {

				GUIFields[field.getFieldNo()] = new GUI_Start();
				GUIFields[field.getFieldNo()].setTitle("Gratis Parkering");
				
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
	
	public void setOwner(Player player, int newFieldNo) {
		Color primaryColor = player.getGUI_player().getPrimaryColor();
		GUI_Field field = getField(newFieldNo);
		if (field instanceof GUI_Ownable) {
			((GUI_Ownable) field).setBorder(primaryColor);
		} else {
			System.out.println("Wrong usage of GUI - trying to set owner on non ownable field " + field);
		}
		
	}
	
	public int getPlayerAmount(String text, String[] options) {
		return Integer.parseInt(gui.getUserSelection(text, options));
	}
	
}