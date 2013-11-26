package restaurant.restaurant_tranac.test.mock;

import restaurant.restaurant_tranac.TranacCheck;
import restaurant.restaurant_tranac.interfaces.*;
import test.mock.*;

/**
 * MockWaiter built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public class TranacMockWaiter extends Mock implements TranacWaiter {

	public TranacMockWaiter() {
		super();
	}
	
	public void msgHereIsCheck(TranacCheck c) {
		log.add(new LoggedEvent("Received msgHereIsCheck from cashier. Check = " + c.getAmount()));
	}
	
//empty messages; not necessary to test cashier
	public void msgPleaseSeatCustomer(TranacCustomer c, int n, int table) {	}
	
	public void msgReadyToOrder(TranacCustomer c) {	}
	
	public void msgReceivedOrder(TranacCustomer c, String choice) {	}
	
	public void msgOrderDone(String choie, int table, int n) {	}
	
	public void msgDoneEating(TranacCustomer c) {	}
	
	public void msgOutOfFood(String choice, int table) {	}
	
	public void msgWantToGoOnBreak() {	}
	
	public void msgGoOnBreak() {	}
	
	public void msgNoBreak() {	}
	
	public void msgAskingForCheck(TranacCustomer c) {	}
}
