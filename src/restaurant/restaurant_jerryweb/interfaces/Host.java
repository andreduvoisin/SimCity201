package restaurant.restaurant_jerryweb.interfaces;

import restaurant.restaurant_jerryweb.JerrywebCustomerRole;
import restaurant.restaurant_jerryweb.JerrywebWaiterRole;

public interface Host {

	public abstract void msgIWantFood(JerrywebCustomerRole customer); 
	
	public abstract void msgWantToGoOnBreak(JerrywebWaiterRole waiter);
	
	public abstract void msgCustomerSeated(JerrywebWaiterRole waiter);
	
	public abstract void msgCustLeavingTable(JerrywebCustomerRole customer);
	
	public abstract void msgBackFromBreak(JerrywebWaiterRole waiter);
}
