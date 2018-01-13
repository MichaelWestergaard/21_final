package entities;

public class Account {

	private int balance;
	
	public Account(int balance) {
		this.balance = balance;
	}
	
	public void setBalance(int points) {
		if(balance + points < 0) {
			balance = 0;
		} else {
			balance += points;
		}
	}
	
	public int getBalance() {
		return balance;
	}
}
