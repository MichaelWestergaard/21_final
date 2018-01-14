package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import entities.Player;

public class PlayerTest {

	Player player = new Player("PlayerNameTest", 0);

	@Test
	public void testGetName() {
		String expected = "PlayerNameTest";
		String actual = player.getName();

		assertEquals(expected, actual);
	}

	@Test
	public void testAddPoints_negative() {
		player.addPoints(-2500);
		int expected = 0;
		int actual = player.getPoints();

		assertEquals(expected, actual);
	}

	@Test
	public void testAddPoints_positive() {
		player.addPoints(2500);
		int expected = 2500;
		int actual = player.getPoints();

		assertEquals(expected, actual);
	}

	@Test
	public void testGetPoints() {
		int expected = 0;
		int actual = player.getPoints();

		assertEquals(expected, actual);
	}

	@Test
	public void testGetFieldNo() {
		int expected = 0;
		int actual = player.getFieldNo();

		assertEquals(expected, actual);
	}

	@Test
	public void testSetFieldNo() {
		player.setFieldNo(10);

		int expected = 10;
		int actual = player.getFieldNo();

		assertEquals(expected, actual);
	}

	@Test
	public void testIsBankrupt_false() {

		boolean expected = false;
		boolean actual = player.isBankrupt();

		assertEquals(expected, actual);	
	}

	@Test
	public void testSetBankrupt() {
		player.setBankrupt(true);

		boolean expected = true;
		boolean actual = player.isBankrupt();

		assertEquals(expected, actual);	
	}

	@Test
	public void testIsJailed() {

		boolean expected = false;
		boolean actual = player.isJailed();

		assertEquals(expected, actual);	
	}

	@Test
	public void testSetJailed() {
		player.setJailed(true);

		boolean expected = true;
		boolean actual = player.isJailed();

		assertEquals(expected, actual);	
	}
	@Test
	public void testSetJailCounter() {
		player.setJailCounter(2);

		int expected = 2;
		int actual = player.getJailCounter();

		assertEquals(expected, actual);
	}

	@Test
	public void testIncreaseJailCounter() {

		player.increaseJailCounter();

		int expected = 1;
		int actual = player.getJailCounter();

		assertEquals(expected, actual);
	}

	@Test
	public void testGetJailCounter() {
		int expected = 0;
		int actual = player.getJailCounter();

		assertEquals(expected, actual);
	}

	@Test
	public void testSetJailCard() {

		player.setJailCard(1);

		int expected = 1;
		int actual = player.getJailCard();

		assertEquals(expected, actual);
	}

	@Test
	public void testGetJailCard() {

		int expected = 0;
		int actual = player.getJailCard();

		assertEquals(expected, actual);
	}

	@Test
	public void testGetHitDouble() {

		int expected = 0;
		int actual = player.getHitDouble();

		assertEquals(expected, actual);
	}

	@Test
	public void testIncreaseHitDouble() {
		player.increaseHitDouble();

		int expected = 1;
		int actual = player.getHitDouble();

		assertEquals(expected, actual);
	}

	@Test
	public void testResetHitDouble() {

		player.resetHitDouble();

		int expected = 0;
		int actual = player.getHitDouble();

		assertEquals(expected, actual);
	}

}
