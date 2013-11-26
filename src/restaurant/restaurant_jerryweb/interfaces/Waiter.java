package restaurant.restaurant_jerryweb.interfaces;

import restaurant.restaurant_jerryweb.CustomerRole;
import restaurant.restaurant_jerryweb.WaiterRole;

public interface Waiter {

	public abstract void msgHereIsBill(Customer c, double check);

	public abstract void msgRecievedFood ();
	
	public abstract void msgReadyToOrder(Customer customer);
	
	public abstract void msgOutOfFood(String choice, int t);
	
	public abstract void msgLeavingTable(Customer c); 
	
	public abstract void msgCanGoOnBreak();
	
	public abstract void msgServedFood();
	
	public abstract void msgHereIsMyOrder(Customer customer, String order);
	
	public abstract void msgOrderReady(String meal, int t);
	
	public abstract void msgAskForBreak();
	
	public abstract String getName();

	public abstract void msgAtTable();

	public abstract void msgAtCook();

	public abstract void msgAtCashier();

	public abstract void msgPleaseSitCustomer(Customer c, int tableNumber);

	public abstract void msgTakeFood(String choice, int table);
	

	
}
