package restaurant.restaurant_cwagoner.interfaces;

//import restaurant.CashierAgent.Bill;


public interface CwagonerCashier {
	
	public abstract void msgCustomerOrdered(CwagonerWaiter w, CwagonerCustomer c, String food);
	public abstract void msgReadyToPay(CwagonerCustomer c);
	public abstract void msgPayment(CwagonerCustomer c, double cashTendered);
	
}