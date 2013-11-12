package restaurant_smileham.test.mock;

import java.util.List;

import restaurant_smileham.Order;
import restaurant_smileham.agent.Check;
import restaurant_smileham.interfaces.Cashier;
import restaurant_smileham.interfaces.Market;

public class MockCashier extends Mock implements Cashier{

	public MockCashier(String name) {
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
	public void msgMarketBill(Market market, int amount) {
		
	}

	@Override
	public void giveChange(Check check) {
		
	}

	@Override
	public void payMarket(Market market, int amount) {
		
	}
}
