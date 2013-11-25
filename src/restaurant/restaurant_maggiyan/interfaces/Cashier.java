package restaurant.restaurant_maggiyan.interfaces;

import java.util.List;

import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {
	public void msgPleaseCalculateBill(Waiter w, Customer c, String choice);

	
	//From Customer
	public void msgHereIsPayment(Customer c, double cash);


	public void msgDeliverBill(Market me, List<String> deliveryBill);
	

}