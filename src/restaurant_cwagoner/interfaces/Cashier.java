package restaurant_cwagoner.interfaces;

//import restaurant.CashierAgent.Bill;


public interface Cashier {
	
	public abstract void msgCustomerOrdered(Waiter w, Customer c, String food);
	public abstract void msgReadyToPay(Customer c);
	public abstract void msgPayment(Customer c, double cashTendered);
	public abstract void msgPayForOrder(Market m, double total);
	public abstract void msgDontOrderAgain(Market m);
	
}