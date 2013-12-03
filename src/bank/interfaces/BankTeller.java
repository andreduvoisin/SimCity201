package bank.interfaces;

import base.PersonAgent;


public interface BankTeller {

	public abstract void msgDeposit(BankCustomer c, int SSN, double amount);

	public abstract void msgLoan(BankCustomer c, int SSN, double amount);

	public abstract void msgPayment(BankCustomer c, int SSN, double amount);

	public abstract void msgOpen(BankCustomer c, int SSN, double amount, PersonAgent person);

	public abstract void msgRobbery(BankCustomer c, int SSN, double amount);

	//	SCHEDULER
	public abstract boolean pickAndExecuteAnAction();

	//	UTILITIES
	public abstract void addGuard(BankGuard guard);
	
	public abstract void setMaster(BankMasterTeller masterTeller);

	public abstract void setAccountIndex();

	public abstract void setAccounts();

	public abstract void msgLeaving();

	public abstract int getWindowNumber();

}