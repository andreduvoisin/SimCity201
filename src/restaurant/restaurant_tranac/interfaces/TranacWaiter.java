package restaurant.restaurant_tranac.interfaces;

import restaurant.restaurant_tranac.TranacCheck;
/**
 * Waiter interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface TranacWaiter {
	public abstract void msgPleaseSeatCustomer(TranacCustomer c, int n, int table);
	
	public abstract void msgReadyToOrder(TranacCustomer c);
	
	public abstract void msgReceivedOrder(TranacCustomer c, String choice);
	
	public abstract void msgOrderDone(String choice, int table, int n);
	
	public abstract void msgDoneEating(TranacCustomer c);
	
	public abstract void msgOutOfFood(String choice, int table);
	
/*	public abstract void msgWantToGoOnBreak();
	
	public abstract void msgGoOnBreak();
	
	public abstract void msgNoBreak();
	*/
	public abstract void msgAskingForCheck(TranacCustomer c);

	public abstract void msgHereIsCheck(TranacCheck check);
}