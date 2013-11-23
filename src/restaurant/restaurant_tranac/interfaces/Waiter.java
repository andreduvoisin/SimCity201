package restaurant.restaurant_tranac.interfaces;

import restaurant.restaurant_tranac.Check;
/**
 * Waiter interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface Waiter {
	public abstract void msgPleaseSeatCustomer(Customer c, int n, int table);
	
	public abstract void msgReadyToOrder(Customer c);
	
	public abstract void msgReceivedOrder(Customer c, String choice);
	
	public abstract void msgOrderDone(String choice, int table, int n);
	
	public abstract void msgDoneEating(Customer c);
	
	public abstract void msgOutOfFood(String choice, int table);
	
	public abstract void msgWantToGoOnBreak();
	
	public abstract void msgGoOnBreak();
	
	public abstract void msgNoBreak();
	
	public abstract void msgAskingForCheck(Customer c);

	public abstract void msgHereIsCheck(Check check);
}