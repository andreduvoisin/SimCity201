package restaurant.restaurant_tranac.interfaces;

/**
 * Cook interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface Market {
	public abstract void msgOrderFood(Cook c, String f, int n);
	
	public abstract void msgHereIsPayment(String i, double p);
	
	public abstract void msgWillPaySoon(String i, double p);
}