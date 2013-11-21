package bank.interfaces;


public interface Customer {

	public abstract void msgGoToTeller(Teller t);

	public abstract void msgAtLocation();

	public abstract void msgHereIsBalance(double balance);

	public abstract void msgHereIsLoan(double loan);
	
	public abstract void msgStopRobber();

	public abstract boolean pickAndExecuteAnAction();
	
	public abstract int getSSN();
	
	public abstract void setGuard(Guard guard);

}