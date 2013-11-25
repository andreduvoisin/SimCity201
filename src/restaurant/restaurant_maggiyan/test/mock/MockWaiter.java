package restaurant.restaurant_maggiyan.test.mock;


import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.gui.MaggyanWaiterGui;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCashier;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements MaggiyanWaiter {
	
	public MaggiyanCashier cashier;

	public MockWaiter(String name) {
		super(name);

	}

@Override
public void msgPleaseSeatCustomer(MaggiyanCustomer cust, int table) {
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
public void msgReadyToOrder(MaggiyanCustomer cust) {
	log.add(new LoggedEvent("Received msgReadyToOrder. Customer = " + cust.getName())); 
	
}

@Override
public void msgHereIsMyOrder(String choice, MaggiyanCustomer c) {
	log.add(new LoggedEvent("Received msgHereIsMyOrder. Choice = " + choice + ". Customer = " + c.getName())); 
	
}

@Override
public void msgLeavingTable(MaggiyanCustomer cust) {
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
public MaggyanWaiterGui getGui() {
	// TODO Auto-generated method stub
	return null;
}

}
