package restaurant.restaurant_maggiyan.interfaces;

import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.gui.MaggiyanWaiterGui;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface MaggiyanWaiter {
	MaggiyanWaiterGui waiterGui = null;

	public void msgPleaseSeatCustomer(MaggiyanCustomer cust, int table);
	
	public void msgCantGoOnBreak();
	
	public void msgGoOnBreak();
	
	//From Cook 
	
	public void msgOutOfChoice(String choice, int tableNum);
	
	//Lets the waiter know food is done
	public void msgOrderDone(String choice, int tableNum, int cookingPos);
	
	//From Cashier
	public void msgHereIsBill(Check check);
	
	//From Customer
	
	public void msgReadyToOrder(MaggiyanCustomer cust);
	
	public void msgHereIsMyOrder(String choice, MaggiyanCustomer c);
	
	public void msgLeavingTable(MaggiyanCustomer cust);

	public Object getName();

	public void msgReadyToBeSeated();

	public void msgAskToGoOnBreak();

	public void msgAnimationReady();

	public void msgReachedKitchen();

	public void msgAtTable();

	public void atWork();

	public void msgBackFromBreak();

	public void msgWaiterFree();

	public MaggiyanWaiterGui getGui();


}