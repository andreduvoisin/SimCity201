package restaurant.restaurant_xurex.test.mock;

import restaurant.restaurant_xurex.interfaces.Customer;
import restaurant.restaurant_xurex.interfaces.Host;
import restaurant.restaurant_xurex.interfaces.Waiter;


/**
 * MockHost built to test waiter
 *
 * @author Rex Xu
 *
 */
public class MockHost extends Mock implements Host {

	public MockHost(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void IWantFood(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void IWillWait(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void IWillNotWait(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void IWantBreak(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void IAmFree() {
		log.add(new LoggedEvent("IAmFree"));
	}

	@Override
	public void TableIsFree(int t) {
		log.add(new LoggedEvent("TableIsFree: "+t));
	}

	@Override
	public void addWaiter(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWaiterNumber() {
		// TODO Auto-generated method stub
		return 0;
	}



}
