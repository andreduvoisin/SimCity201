package restaurant.restaurant_smileham.test.mock;

import java.util.List;

import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.interfaces.SmilehamCashier;
import restaurant.restaurant_smileham.interfaces.SmilehamMarket;

public class SmilehamMockCashier extends SmilehamMock implements SmilehamCashier{

	public SmilehamMockCashier(String name) {
		super(name);
		
	}

	@Override
	public void msgMakeCheck(Order order) {
		
		
	}

	@Override
	public void msgPayingCheck(Check check) {
		
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		
		return false;
	}

	@Override
	public void makeCheck(Order order) {
		
		
	}

	@Override
	public List<Order> getOrders() {
		return null;
	}

	@Override
	public void msgMarketBill(SmilehamMarket market, int amount) {
		
	}

	@Override
	public void giveChange(Check check) {
		
	}

	@Override
	public void payMarket(SmilehamMarket market, int amount) {
		
	}
}
