package restaurant.restaurant_tranac.interfaces;

/**
 * Cook interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface Cook {
	public abstract void msgHereIsOrder(Waiter w, String c, int t);
	
	public abstract void msgOrderPickedUp(Waiter w, String c);
	
/*	public abstract void msgCanFulfillInventory(String f, int n);
	
	public abstract void msgHereIsInventory(String f, int n);
	
	public abstract void msgOutOfInventory(Market m, String f);
*/
}