package restaurant.restaurant_maggiyan.interfaces;

import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanHostRole.WaiterState;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface MaggiyanHost {
	//From Customer 
		public void msgIWantFood(MaggiyanCustomer cust);
		
		public void msgLeaving(MaggiyanCustomer customer);
		
		//From Waiter
		public void msgIAmHere(MaggiyanWaiter waiter);
		
		public void msgWaiterFree(MaggiyanWaiter w);
		public void msgWaiterBusy(MaggiyanWaiter w);
		
		
		public void msgCanIGoOnBreak(MaggiyanWaiter w);
		
		public void msgDoneWithBreak(MaggiyanWaiter w);
		
		public void msgTableFree(int tableNum, MaggiyanWaiter w);

}