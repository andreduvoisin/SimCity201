package bank.interfaces;

import bank.BankDatabase;

public interface Teller {

	public abstract void msgDeposit(Customer c, double amount);

	public abstract void msgLoan(Customer c, double amount);

	public abstract void msgPayment(Customer c, double amount);

	public abstract void msgOpen(Customer c, double amount);

	public abstract void msgRobbery(Customer c, double amount);

	//	SCHEDULER
	public abstract boolean pickAndExecuteAnAction();

	//	UTILITIES
	public abstract void addGuard(Guard guard);

	public abstract void addDatabase(BankDatabase database);

}