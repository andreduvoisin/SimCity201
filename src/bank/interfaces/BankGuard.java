package bank.interfaces;

public interface BankGuard {

	public abstract void msgNeedService(BankCustomer c);

	public abstract void msgReadyToWork(BankTeller t);

	public abstract void msgRobberAlert(BankCustomer c);

	public abstract boolean pickAndExecuteAnAction();

	public abstract void msgOffWork(BankTeller t);

}