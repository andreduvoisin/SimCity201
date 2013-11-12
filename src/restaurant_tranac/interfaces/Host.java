package restaurant_tranac.interfaces;

/**
 * Host interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface Host {
	public abstract void msgIWantFood(Customer c);
	
	public abstract void msgWillWait(Customer c);
	
	public abstract void msgLeavingEarly(Customer c);
	
	public abstract void msgAtWaitingArea(Customer c);
	
	public abstract void msgCustomerSeated(Customer c);
	
	public abstract void msgTableIsFree(int t);
	
	public abstract void msgWantToGoOnBreak(Waiter w);
	
	public abstract void msgBackFromBreak(Waiter w);
}