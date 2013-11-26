package restaurant.restaurant_jerryweb.interfaces;

import restaurant.restaurant_jerryweb.JerrywebCustomerRole;
import restaurant.restaurant_jerryweb.JerrywebWaiterRole;

/**
 * A sample Cashier interface 
 *
 * @author Jerry Webb
 *
 */
public interface Cashier {

	public abstract void msgPayment(Customer customer, double cash);

	public abstract void msgComputeBill(Waiter waiter, Customer customer, String choice);

}