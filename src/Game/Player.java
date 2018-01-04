package Game;

import gui_fields.GUI_Player;

public class Player {

	private String name;
	private GUI_Player GUI_player;
	private Account account;
	private int fieldNo = 0;
	private boolean isBankrupt = false;
	private boolean isJailed = false;
	
	public String getName(){
		return name;	
	}
	
	public Player (String name,int balance) {
		this.name = name;
		this.account = new Account(balance);
		
	}
	
	public void addPoints(int points){
		account.setBalance(points);
	}
	
	public int getPoints(){
		return account.getBalance();
	}
	
	public int getFieldNo(){
		return fieldNo;
	}
	
	public void setFieldNo(int fieldNo){
		this.fieldNo = fieldNo;	
	}

	public GUI_Player getGUI_player() {
		return GUI_player;
	}

	public void setGUI_player(GUI_Player gUI_player) {
		GUI_player = gUI_player;
	}

	public boolean isBankrupt() {
		return isBankrupt;
	}

	public void setBankrupt(boolean isBankrupt) {
		this.isBankrupt = isBankrupt;
	}

	public boolean isJailed() {
		return isJailed;
	}

	public void setJailed(boolean isJailed) {
		this.isJailed = isJailed;
	}
	
}
