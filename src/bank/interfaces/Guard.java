package bank.interfaces;

public interface Guard {

	public abstract void msgNeedService(Customer c);

	public abstract void msgReadyToWork(Teller t);

	public abstract void msgReadyForNext(Teller t);

	public abstract void msgRobberAlert(Customer c);

	public abstract boolean pickAndExecuteAnAction();

	public abstract void msgOffWork(Teller t);

}