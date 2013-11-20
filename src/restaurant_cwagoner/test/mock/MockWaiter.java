package restaurant_cwagoner.test.mock;

import restaurant_cwagoner.interfaces.*;

public class MockWaiter extends Mock implements Waiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public EventLog log = new EventLog();

	public MockWaiter(String name) {
		super(name);
	}
	
	
	public void msgGoOnBreak(boolean allowed) {
		
	}
	
	// From host
	public void msgSeatCustomer(Customer c, int table) {
		//print("Received msgSeatCustomer(" + c.getName() + ", table " + table + ")");
	}
	
	// From customer
	public void msgReadyToOrder(Customer c) {
		//print("Received msgReadyToOrder(" + c.getName() + ")");
	}
	
	// From customer
	public void msgHeresMyOrder(Customer c, String choice) {
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
	public void msgCheckPlease(Customer c) {
		//print("Received msgCheckPlease(" + c.getName() + ")");
	}
	
	// From cashier
	public void msgCheckReady(Customer c) {
		//print("Received msgCheckReady(" + c.getName() + ")");
	}
	
	// From customer
	public void msgLeavingTable(Customer c) {
		//print("Received msgLeavingTable(" + c.getName() + ")");
	}
	
	public int numCustomers() {
		// TODO
		return 1;
	}
}
