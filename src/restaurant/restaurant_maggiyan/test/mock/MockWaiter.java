package restaurant_maggiyan.test.mock;


import restaurant_maggiyan.Check;
import restaurant_maggiyan.gui.WaiterGui;
import restaurant_maggiyan.interfaces.Cashier;
import restaurant_maggiyan.interfaces.Customer;
import restaurant_maggiyan.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter {
	
	public Cashier cashier;

	public MockWaiter(String name) {
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
public void msgHereIsBill(Check check) {
	log.add(new LoggedEvent("Received msgHereIsBill of: " + check.getCheckTotal())); 
	
}

private String getCheckTotal(Check check) {
	// TODO Auto-generated method stub
	return null;
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

@Override
public void msgOrderDone(String choice, int tableNum, int cookingPos) {
	log.add(new LoggedEvent("Received msgOrderDone. Choice = " + choice + ". Table = " + tableNum)); 
	
}

@Override
public void msgReadyToBeSeated() {
	// TODO Auto-generated method stub
	
}

@Override
public void msgAskToGoOnBreak() {
	// TODO Auto-generated method stub
	
}

@Override
public void msgAnimationReady() {
	// TODO Auto-generated method stub
	
}

@Override
public void msgReachedKitchen() {
	// TODO Auto-generated method stub
	
}

@Override
public void msgAtTable() {
	// TODO Auto-generated method stub
	
}

@Override
public void atWork() {
	// TODO Auto-generated method stub
	
}

@Override
public void msgBackFromBreak() {
	// TODO Auto-generated method stub
	
}

@Override
public void msgWaiterFree() {
	// TODO Auto-generated method stub
	
}

@Override
public void restart() {
	// TODO Auto-generated method stub
	
}

@Override
public void pause() {
	// TODO Auto-generated method stub
	
}

@Override
public WaiterGui getGui() {
	// TODO Auto-generated method stub
	return null;
}

}
