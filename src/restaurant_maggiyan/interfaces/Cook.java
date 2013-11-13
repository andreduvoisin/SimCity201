package restaurant_maggiyan.interfaces;

import java.util.Map;

import restaurant_maggiyan.Check;
import restaurant_maggiyan.CustomerAgent;
import restaurant_maggiyan.WaiterAgent;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cook {
	//From Waiter
		public void msgHereIsOrder(Waiter w, String choice, int table);
		
		//From Market
		public void msgFulfillingOrder();
		
		public void msgFulfillingPartialOrder();
		
		public void msgCannotFulfillOrder();
		
		public void msgDeliverOrder(Map<String, Integer> order);
		
		public void msgOutOfAllInventory(Market m);
	

}