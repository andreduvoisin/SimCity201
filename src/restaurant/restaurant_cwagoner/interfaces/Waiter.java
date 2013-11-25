package restaurant.restaurant_cwagoner.interfaces;

public interface Waiter {

	public abstract void msgSeatCustomer(Customer c, int table);
	public abstract void msgReadyToOrder(Customer c);
	public abstract void msgHeresMyOrder(Customer c, String choice);
	public abstract void msgOrderReady(int table);
	public abstract void msgOutOfFood(int table);
	public abstract void msgLeavingTable(Customer c);
	public abstract int numCustomers();
	public abstract String getName();

}