package bank.interfaces;


public interface Customer {

	public abstract void msgGoToTeller(Teller t);

	public abstract void msgAtLocation();

	public abstract void msgHereIsBalance(double balance);

	public abstract void msgHereIsLoan(double loan);

	public abstract boolean pickAndExecuteAnAction();

}