package restaurant_maggiyan.interfaces;

import restaurant_maggiyan.Check;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Waiter {
public void msgPleaseSeatCustomer(Customer cust, int table);
	
	public void msgCantGoOnBreak();
	
	public void msgGoOnBreak();
	
	//From Cook 
	
	public void msgOutOfChoice(String choice, int tableNum);
	
	//Lets the waiter know food is done
	public void msgOrderDone(String choice, int tableNum);
	
	//From Cashier
	public void msgHereIsBill(Check check);
	
	//From Customer
	
	public void msgReadyToOrder(Customer cust);
	
	public void msgHereIsMyOrder(String choice, Customer c);
	
	public void msgLeavingTable(Customer cust);

	public void msgReadyToBeSeated();
}