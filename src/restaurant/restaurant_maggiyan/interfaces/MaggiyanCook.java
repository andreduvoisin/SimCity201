package restaurant.restaurant_maggiyan.interfaces;

import java.util.Map;

import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface MaggiyanCook {
	//From Waiter
		public void msgHereIsOrder(MaggiyanWaiter w, String choice, int table);
		
		//From Market
		public void msgFulfillingOrder();
		
		public void msgFulfillingPartialOrder();
		
		public void msgCannotFulfillOrder();
		
		public void msgDeliverOrder(Map<String, Integer> order);
		
		public void msgOutOfAllInventory(MaggiyanMarket m);

		public void msgPickedUpOrder(int orderPos);

		public void addRStandOrder(MaggiyanWaiter w,
				String choice, int table);
	

}