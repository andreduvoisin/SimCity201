package restaurant_cwagoner.interfaces;

public interface Host {

	public void msgIWantFood(Customer cust);

	public void msgCustomerGoneTableEmpty(Customer c, int tableNum);

	public void msgCanIGoOnBreak(Waiter w);

	public void msgOffBreak(Waiter w);
}