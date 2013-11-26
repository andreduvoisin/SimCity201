package restaurant.restaurant_tranac.interfaces;

/**
 * Host interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface TranacHost {
	public abstract void msgIWantFood(TranacCustomer c);
	
	public abstract void msgWillWait(TranacCustomer c);
	
	public abstract void msgLeavingEarly(TranacCustomer c);
	
	public abstract void msgAtWaitingArea(TranacCustomer c);
	
	public abstract void msgCustomerSeated(TranacCustomer c);
	
	public abstract void msgTableIsFree(int t);
	
	public abstract void msgWantToGoOnBreak(TranacWaiter w);
	
	public abstract void msgBackFromBreak(TranacWaiter w);
}