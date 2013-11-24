package restaurant.restaurant_xurex.test.mock;


import restaurant.restaurant_xurex.interfaces.Cashier;
import restaurant.restaurant_xurex.interfaces.Customer;
import restaurant.restaurant_xurex.interfaces.Market;
import restaurant.restaurant_xurex.interfaces.Waiter;


/**
 * MockCashier built to test waiter
 *
 * @author Rex Xu
 *
 */
public class MockCashier extends Mock implements Cashier {

	public MockCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void ComputeBill(Waiter waiter, Customer customer) {
		log.add(new LoggedEvent("ComputeBill: "+customer.getName()));
	}

	@Override
	public void IWantToPay(Customer customer, float cash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HereIsBill(Market market, float payment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getAssets() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
