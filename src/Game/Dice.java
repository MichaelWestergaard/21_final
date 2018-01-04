package Game;

public class Dice {

	private int faceValue;
	
	public void rollDice() {
		faceValue = (int)(Math.random()*6) + 1; //Terning 1 sættes til at være 1-6
	}
	
	public int getFaceValue() {
		return faceValue;
	}
	
}