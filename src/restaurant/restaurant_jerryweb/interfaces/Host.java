package restaurant.restaurant_jerryweb.interfaces;

import restaurant.restaurant_jerryweb.CustomerRole;
import restaurant.restaurant_jerryweb.WaiterRole;

public interface Host {

	public abstract void msgIWantFood(CustomerRole customer); 
	
	public abstract void msgWantToGoOnBreak(WaiterRole waiter);
	
	public abstract void msgCustomerSeated(WaiterRole waiter);
	
	public abstract void msgCustLeavingTable(CustomerRole customer);
	
	public abstract void msgBackFromBreak(WaiterRole waiter);
}
