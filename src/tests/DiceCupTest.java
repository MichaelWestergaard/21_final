package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import entities.Dice;
import entities.DiceCup;

public class DiceCupTest {
	
	DiceCup diceCupTest = new DiceCup();

	@Test
	public void testRollDices() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDiceValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDiceSum() {
		int expected;
		diceCupTest.rollDices();
		int diceSum = diceCupTest.getDiceSum();
		
		switch (diceSum) {
			case 2:
				expected = 2;
				assertEquals(expected, diceSum);
				break;
			case 3:
				expected = 3;
				assertEquals(expected, diceSum);
				break;
			case 4:
				expected = 4;
				assertEquals(expected, diceSum);
				break;
			case 5:
				expected = 5;
				assertEquals(expected, diceSum);
				break;
			case 6:
				expected = 6;
				assertEquals(expected, diceSum);
				break;
			case 7:
				expected = 7;
				assertEquals(expected, diceSum);
				break;
			case 8:
				expected = 8;
				assertEquals(expected, diceSum);
				break;
			case 9:
				expected = 9;
				assertEquals(expected, diceSum);
				break;
			case 10:
				expected = 10;
				assertEquals(expected, diceSum);
				break;
			case 11:
				expected = 11;
				assertEquals(expected, diceSum);
				break;
			case 12:
				expected = 12;
				assertEquals(expected, diceSum);
				break;
	
			default:
				fail("Tallet ligger ikke inden for 2-12");
				break;
		}
	}
}
