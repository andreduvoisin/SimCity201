package restaurant.restaurant_cwagoner.interfaces;

public interface CwagonerHost {

	public void msgIWantFood(CwagonerCustomer cust);

	public void msgCustomerGoneTableEmpty(CwagonerCustomer c, int tableNum);

	public void msgCanIGoOnBreak(CwagonerWaiter w);

	public void msgOffBreak(CwagonerWaiter w);
}