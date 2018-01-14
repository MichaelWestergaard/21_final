package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import entities.Dice;

public class DiceTest {

	Dice die = new Dice();
	
	@Test
	public void testGetFaceValue() {
		int[] values = new int[6];
		
		for (int i = 0; i < 60000; i++) {
			die.rollDice();
			
			int value = die.getFaceValue();
			values[value - 1] ++;
			
		}
		
		System.out.println("Antal 1'ere: " + values[0]);
		System.out.println("Antal 2'ere: " + values[1]);
		System.out.println("Antal 3'ere: " + values[2]);
		System.out.println("Antal 4'ere: " + values[3]);
		System.out.println("Antal 5'ere: " + values[4]);
		System.out.println("Antal 6'ere: " + values[5]);
	}
}
