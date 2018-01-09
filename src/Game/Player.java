package Game;

import gui_fields.GUI_Player;

public class Player {

	private String name;
	private GUI_Player GUI_player;
	private Account account;
	private int fieldNo = 0;
	private boolean isBankrupt = false;
	private boolean isJailed = false;
	private int jailCounter = 0;
	private int jailCard = 0;
	private int hitDouble = 0;
	private int[] ownedFieldNumbers = new int[19];
	
	public Player (String name,int balance) {
		this.name = name;
		this.account = new Account(balance);
	}
	
	public int[] getOwnedFieldNumbers() {
		return ownedFieldNumbers;
	}

	public void setOwnedFieldNumber(int fieldNumber) {
		for (int i = 0; i < ownedFieldNumbers.length; i++) {
			if(ownedFieldNumbers[i] == 0) {
				this.ownedFieldNumbers[i]= fieldNumber;
				break;
			}
		}
	}
	
	public void resetOwnedFieldNumber(int fieldNumber) {
		for (int i = 0; i < ownedFieldNumbers.length; i++) {
			if(ownedFieldNumbers[i] == fieldNumber) {
				this.ownedFieldNumbers[i] = 0;
				break;
			}
		}
	}

	public String getName(){
		return name;	
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
	
	public void setJailCounter(int jailCounter) {
		this.jailCounter = jailCounter;
	}
	public void increaseJailCounter() {
		jailCounter++;
	}
	public int getJailCounter() {
		return jailCounter;
	}
	
	public void setJailCard(int jailCard) {
		this.jailCard += jailCard;
	}
	
	public int getJailCard() {
		return jailCard;
	}
	
	public int getHitDouble() {
		return hitDouble;
	}
	
	public void increaseHitDouble() {
		this.hitDouble++;
	}
	
	public void resetHitDouble() {
		this.hitDouble = 0;
	}
}
