package bank.interfaces;


public interface Teller {

	public abstract void msgDeposit(Customer c, int SSN, double amount);

	public abstract void msgLoan(Customer c, int SSN, double amount);

	public abstract void msgPayment(Customer c, int SSN, double amount);

	public abstract void msgOpen(Customer c, int SSN, double amount, String name);

	public abstract void msgRobbery(Customer c, int SSN, double amount);

	//	SCHEDULER
	public abstract boolean pickAndExecuteAnAction();

	//	UTILITIES
	public abstract void addGuard(Guard guard);

	public abstract void setMaster(MasterTeller masterTeller);

	public abstract void setAcountIndex();

	public abstract void setAccounts();

}