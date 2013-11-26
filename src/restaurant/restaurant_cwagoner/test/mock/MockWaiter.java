package restaurant.restaurant_cwagoner.test.mock;

import restaurant.restaurant_cwagoner.interfaces.*;

public class MockWaiter extends Mock implements CwagonerWaiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public CwagonerCashier cwagonerCashier;

	public EventLog log = new EventLog();

	public MockWaiter(String name) {
		super(name);
	}
	
	
	public void msgGoOnBreak(boolean allowed) {
		
	}
	
	// From host
	public void msgSeatCustomer(CwagonerCustomer c, int table) {
		//print("Received msgSeatCustomer(" + c.getName() + ", table " + table + ")");
	}
	
	// From customer
	public void msgReadyToOrder(CwagonerCustomer c) {
		//print("Received msgReadyToOrder(" + c.getName() + ")");
	}
	
	// From customer
	public void msgHeresMyOrder(CwagonerCustomer c, String choice) {
		//print("Received msgHeresMyOrder(" + c.getName() + ", " + choice + ")");
	}
	
	// From cook
	public void msgOrderReady(int table) {
		//print("Received msgOrderReady(table " + table + ")");
	}
	
	// From cook
	public void msgOutOfFood(int table) {
		//print("Received msgOutOfFood(table " + table + ")");
	}
	
	// From customer
	public void msgCheckPlease(CwagonerCustomer c) {
		//print("Received msgCheckPlease(" + c.getName() + ")");
	}
	
	// From cashier
	public void msgCheckReady(CwagonerCustomer c) {
		//print("Received msgCheckReady(" + c.getName() + ")");
	}
	
	// From customer
	public void msgLeavingTable(CwagonerCustomer c) {
		//print("Received msgLeavingTable(" + c.getName() + ")");
	}
	
	public int numCustomers() {
		return 1;
	}
}
