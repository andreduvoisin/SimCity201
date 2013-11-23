package restaurant.restaurant_tranac.interfaces;

/**
 * Cook interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface Cashier {
	public abstract void msgComputeCheck(Waiter w, Customer c, String item);
	
	public abstract void msgHereIsPayment(Customer c, double p);
	
	public abstract void msgHereIsBill(Market m, String i, double c);
}