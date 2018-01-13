package entities;

import entities.Dice;

public class DiceCup {
	
	private Dice[] dices = new Dice[2];

	public DiceCup() {
		for (int i = 0; i < 2; i++) {
			dices[i] = new Dice();
		}
	}
	
	public void rollDices() {
		for (int i = 0; i < dices.length; i++) {
			dices[i].rollDice();
		}
	}
	
	public int getDiceValue(int i) {
		return dices[i].getFaceValue();
	}
	
	public int getDiceSum() {
		int diceSum = 0;
		
		for (int i = 0; i < dices.length; i++) {
			diceSum += dices[i].getFaceValue();
		}
		return 1;
	}
	
}
