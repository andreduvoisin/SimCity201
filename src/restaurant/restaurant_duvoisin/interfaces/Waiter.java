package restaurant.restaurant_duvoisin.interfaces;

public interface Waiter {
	public abstract void msgSitAtTable(Customer c, int table, int waitingPosition);
	public abstract void msgImReadyToOrder(Customer c);
	public abstract void msgHereIsMyChoice(Customer c, String choice);
	public abstract void msgOrderIsReady(String choice, int table, int position);
	public abstract void msgDoneEatingAndLeaving(Customer c);
	public abstract void msgOutOfFood(int table, String choice);
	public abstract void msgRequestBreak();
	public abstract void msgRespondToBreakRequest(Boolean answer);
	public abstract void msgRequestCheck(Customer c);
	public abstract void msgHereIsCheck(Customer c, double amount);
}