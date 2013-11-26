package restaurant.restaurant_tranac.test.mock;

import restaurant.restaurant_tranac.*;
import restaurant.restaurant_tranac.gui.TranacCustomerGui;
import restaurant.restaurant_tranac.interfaces.*;
import test.mock.*;

/**
 * MockCustomer built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public class TranacMockCustomer extends Mock implements TranacCustomer {
	public TranacMockCustomer() {
		super();
	}
	
	public void msgHereIsChange(TranacCheck c) {
		log.add(new LoggedEvent("Received msgHereIsChange from cashier. Change = " + c.getChange()));
	}
	
	public void msgPayNextTime() {
		log.add(new LoggedEvent("Received msgPayNextTime from cashier."));
	}

//empty messages; not necessary to test cashier
	public String getName() { return getName(); }
	
	public TranacCustomerGui getGui() { return null; }
	
	public void msgGotHungry() {	}
	
	public void msgPleaseWaitHere(int n) {	}
	
	public void msgRestaurantFull() {	}
	
	public void msgFollowMe(TranacMenu m, TranacWaiter w) {	}
	
	public void msgWhatDoYouWant() {	}
	
	public void msgOutOfChoice() {	}
	
	public void msgHereIsFood() {	}
	
	public void msgDoneEating() {	}
	
	public void msgHereIsCheck(TranacCheck c) {	}
}
