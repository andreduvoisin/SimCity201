package restaurant.restaurant_maggiyan.interfaces;

import java.util.List;

import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Maggi Yang 
 *
 */
public interface MaggiyanCashier {
	public void msgPleaseCalculateBill(MaggiyanWaiter w, MaggiyanCustomer c, String choice);

	
	//From Customer
	public void msgHereIsPayment(MaggiyanCustomer c, double cash);


	public void msgDeliverBill(MaggiyanMarket me, List<String> deliveryBill);
	

}