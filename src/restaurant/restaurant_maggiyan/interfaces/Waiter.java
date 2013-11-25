package restaurant_maggiyan.interfaces;

import restaurant_maggiyan.Check;
import restaurant_maggiyan.gui.WaiterGui;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Waiter {
	WaiterGui waiterGui = null;

	public void msgPleaseSeatCustomer(Customer cust, int table);
	
	public void msgCantGoOnBreak();
	
	public void msgGoOnBreak();
	
	//From Cook 
	
	public void msgOutOfChoice(String choice, int tableNum);
	
	//Lets the waiter know food is done
	public void msgOrderDone(String choice, int tableNum, int cookingPos);
	
	//From Cashier
	public void msgHereIsBill(Check check);
	
	//From Customer
	
	public void msgReadyToOrder(Customer cust);
	
	public void msgHereIsMyOrder(String choice, Customer c);
	
	public void msgLeavingTable(Customer cust);

	public Object getName();

	public void msgReadyToBeSeated();

	public void msgAskToGoOnBreak();

	public void msgAnimationReady();

	public void msgReachedKitchen();

	public void msgAtTable();

	public void atWork();

	public void msgBackFromBreak();

	public void msgWaiterFree();

	public void restart();

	public void pause();

	public WaiterGui getGui();


}