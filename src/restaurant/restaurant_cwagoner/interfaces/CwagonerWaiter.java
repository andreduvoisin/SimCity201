package restaurant.restaurant_cwagoner.interfaces;

public interface CwagonerWaiter {

	public abstract void msgGoOnBreak(boolean allowed);
	public abstract void msgSeatCustomer(CwagonerCustomer c, int table);
	public abstract void msgReadyToOrder(CwagonerCustomer c);
	public abstract void msgHeresMyOrder(CwagonerCustomer c, String choice);
	public abstract void msgOrderReady(int table);
	public abstract void msgOutOfFood(int table);
	public abstract void msgLeavingTable(CwagonerCustomer c);
	public abstract int numCustomers();
	public abstract String getName();

}