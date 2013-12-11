package restaurant.restaurant_cwagoner.interfaces;

public interface CwagonerHost {

	abstract public void msgIWantFood(CwagonerCustomer cust);

	abstract public void msgCustomerGoneTableEmpty(CwagonerCustomer c, int tableNum);
}