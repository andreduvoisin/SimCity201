package restaurant.restaurant_maggiyan.test.mock;

import java.util.List;

import restaurant.restaurant_maggiyan.interfaces.MaggiyanCashier;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanMarket;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;

public class MockCashier extends Mock implements MaggiyanCashier {

	public MockCashier(String name) {
		super(name);
	}

	@Override
	public void msgPleaseCalculateBill(MaggiyanWaiter w, MaggiyanCustomer c,
			String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(MaggiyanCustomer c, double cash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeliverBill(MaggiyanMarket me, List<String> deliveryBill) {
		// TODO Auto-generated method stub
		
	}

}
