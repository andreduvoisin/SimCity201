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
		public void msgIWantFood(CustomerAgent cust);
		
		public void msgLeaving(CustomerAgent customer);
		
		//From Waiter
		public void msgIAmHere(WaiterAgent waiter);
		
		public void msgWaiterFree(WaiterAgent w);
		public void msgWaiterBusy(WaiterAgent w);
		
		
		public void msgCanIGoOnBreak(WaiterAgent w);
		
		public void msgDoneWithBreak(WaiterAgent w);
		
		public void msgTableFree(int tableNum, WaiterAgent w);

}