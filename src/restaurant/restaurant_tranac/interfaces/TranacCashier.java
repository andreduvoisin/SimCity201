package restaurant.restaurant_tranac.interfaces;

/**
 * Cook interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface TranacCashier {
	public abstract void msgComputeCheck(TranacWaiter w, TranacCustomer c, String item);
	
	public abstract void msgHereIsPayment(TranacCustomer c, double p);
}