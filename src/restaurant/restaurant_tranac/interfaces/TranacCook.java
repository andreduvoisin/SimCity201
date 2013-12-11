package restaurant.restaurant_tranac.interfaces;

/**
 * Cook interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface TranacCook {
	public abstract void addOrderToStand(TranacWaiter w, String c, int t);
	
	public abstract void msgHereIsOrder(TranacWaiter w, String c, int t);
	
	public abstract void msgOrderPickedUp(TranacWaiter w, String c);
}