package restaurant.restaurant_maggiyan.test.mock;


import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.interfaces.Cashier;
import restaurant.restaurant_maggiyan.interfaces.Customer;
import restaurant.restaurant_maggiyan.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCashier extends Mock implements Waiter {
	
	public Cashier cashier;

	public MockCashier(String name) {
		super(name);

	}

@Override
public void msgPleaseSeatCustomer(Customer cust, int table) {
	log.add(new LoggedEvent("Received PleaseSeatCustomer from host. Customer = "+ cust.getName() + "Table = " + table));
	
}

@Override
public void msgCantGoOnBreak() {
	log.add(new LoggedEvent("Received msgCantGoOnBreak")); 
	
}

@Override
public void msgGoOnBreak() {
	log.add(new LoggedEvent("Received msgGoOnBreak")); 
	
}

@Override
public void msgOutOfChoice(String choice, int tableNum) {
	log.add(new LoggedEvent("Received msgOutOfChoice. Choice = " + choice + "Table = " + tableNum)); 
	
}

@Override
public void msgOrderDone(String choice, int tableNum) {
	log.add(new LoggedEvent("Received msgOrderDone. Choice = " + choice + ". Table = " + tableNum)); 
	
}

@Override
public void msgHereIsBill(Check check) {
	log.add(new LoggedEvent("Received msgHereIsBill")); 
	
}

@Override
public void msgReadyToOrder(Customer cust) {
	log.add(new LoggedEvent("Received msgReadyToOrder. Customer = " + cust.getName())); 
	
}

@Override
public void msgHereIsMyOrder(String choice, Customer c) {
	log.add(new LoggedEvent("Received msgHereIsMyOrder. Choice = " + choice + ". Customer = " + c.getName())); 
	
}

@Override
public void msgLeavingTable(Customer cust) {
	log.add(new LoggedEvent("Received msgLeavingTable. Customer = " +cust.getName() )); 
	
}

}
