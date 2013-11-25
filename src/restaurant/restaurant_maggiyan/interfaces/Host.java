package restaurant_maggiyan.interfaces;

import restaurant_maggiyan.Check;
import restaurant_maggiyan.CustomerAgent;
import restaurant_maggiyan.WaiterAgent;
import restaurant_maggiyan.HostAgent.WaiterState;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Host {
	//From Customer 
		public void msgIWantFood(Customer cust);
		
		public void msgLeaving(Customer customer);
		
		//From Waiter
		public void msgIAmHere(Waiter waiter);
		
		public void msgWaiterFree(Waiter w);
		public void msgWaiterBusy(Waiter w);
		
		
		public void msgCanIGoOnBreak(Waiter w);
		
		public void msgDoneWithBreak(Waiter w);
		
		public void msgTableFree(int tableNum, Waiter w);

}