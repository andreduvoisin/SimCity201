package restaurant.restaurant_cwagoner.interfaces;

public interface CwagonerHost {

	abstract public void msgIWantFood(CwagonerCustomer cust);

	abstract public void msgCustomerGoneTableEmpty(CwagonerCustomer c, int tableNum);

	abstract public void msgCanIGoOnBreak(CwagonerWaiter w);

	abstract public void msgOffBreak(CwagonerWaiter w);
	
	abstract public boolean pickAndExecuteAnAction();
}