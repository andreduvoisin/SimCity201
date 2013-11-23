package restaurant.restaurant_tranac.test.mock;

import restaurant.restaurant_tranac.Check;
import restaurant.restaurant_tranac.interfaces.*;
import test.mock.*;

/**
 * MockWaiter built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public class MockWaiter extends Mock implements Waiter {

	public MockWaiter() {
		super();
	}
	
	public void msgHereIsCheck(Check c) {
		log.add(new LoggedEvent("Received msgHereIsCheck from cashier. Check = " + c.getAmount()));
	}
	
//empty messages; not necessary to test cashier
	public void msgPleaseSeatCustomer(Customer c, int n, int table) {	}
	
	public void msgReadyToOrder(Customer c) {	}
	
	public void msgReceivedOrder(Customer c, String choice) {	}
	
	public void msgOrderDone(String choie, int table, int n) {	}
	
	public void msgDoneEating(Customer c) {	}
	
	public void msgOutOfFood(String choice, int table) {	}
	
	public void msgWantToGoOnBreak() {	}
	
	public void msgGoOnBreak() {	}
	
	public void msgNoBreak() {	}
	
	public void msgAskingForCheck(Customer c) {	}
}
