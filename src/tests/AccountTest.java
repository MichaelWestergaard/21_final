package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import entities.Account;

public class AccountTest {
	
	Account account = new Account(0);

	@Test
	public void testSetBalance_positiveNumber() {
		account.setBalance(500);

		int expected = 500;
		int actual = account.getBalance();

		assertEquals(expected, actual);
	}

	@Test
	public void testSetBalance_negativeNumber() {
		account.setBalance(-500);

		int expected = 0;
		int actual = account.getBalance();

		assertEquals(expected, actual);
	}	

	@Test
	public void testSetBalance_zero() {
		account.setBalance(0);

		int expected = 0;
		int actual = account.getBalance();

		assertEquals(expected, actual);
	}	

	@Test
	public void testGetBalance() {
		int expected = 0;
		int actual = account.getBalance();

		assertEquals(expected, actual);
	}

}
