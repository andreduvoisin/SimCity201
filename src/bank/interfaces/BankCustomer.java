package bank.interfaces;


public interface BankCustomer {

	public abstract void msgGoToTeller(BankTeller t);

	public abstract void msgAtLocation();

	public abstract void msgHereIsBalance(double balance);

	public abstract void msgHereIsLoan(double loan);
	
	public abstract void msgStopRobber();

	public abstract boolean pickAndExecuteAnAction();
	
	public abstract int getSSN();
	
	public abstract void setGuard(BankGuard guard);

}