package restaurant_all.interfaces.Host;

import restaurant.restaurant_cwagoner.interfaces.Waiter;
import restaurant_all.interfaces.Customer.Customer_cw;

public interface Host_cw {

	public void msgIWantFood(Customer_cw cust);

	public void msgCustomerGoneTableEmpty(Customer_cw c, int tableNum);

	public void msgCanIGoOnBreak(Waiter w);

	public void msgOffBreak(Waiter w);
}