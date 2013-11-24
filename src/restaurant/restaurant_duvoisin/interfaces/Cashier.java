package restaurant.restaurant_duvoisin.interfaces;

public interface Cashier {
	public abstract void msgComputeBill(Waiter w, Customer c, String choice);
	public abstract void msgPayment(Customer c, double amount);
	public abstract void msgComputeMarketBill(Market m, String type, int amount);
}