package restaurant.restaurant_maggiyan.test.mock;

import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanHost;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;

public class MockHost extends Mock implements MaggiyanHost {
	
	public MockHost(String name){
		super(name); 
	}

	@Override
	public void msgIWantFood(MaggiyanCustomer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaving(MaggiyanCustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmHere(MaggiyanWaiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWaiterFree(MaggiyanWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWaiterBusy(MaggiyanWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCanIGoOnBreak(MaggiyanWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneWithBreak(MaggiyanWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTableFree(int tableNum, MaggiyanWaiter w) {
		// TODO Auto-generated method stub
		
	}
}
