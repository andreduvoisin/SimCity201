package restaurant_davidmca.test.mock;

import java.util.Map;

import restaurant_davidmca.Menu;
import restaurant_davidmca.agents.CashierAgent;
import restaurant_davidmca.interfaces.Cook;
import restaurant_davidmca.interfaces.Market;

public class MockMarket implements Market {
	
	String name;
	CashierAgent cashier;
	public double totalRevenue = 0;
	
	public double orderTotal = 0;
	
	public MockMarket(String mktname) {
		this.name = mktname;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setCashier(CashierAgent cash) {
		this.cashier = cash;

	}

	@Override
	public void msgWantToBuy(Cook c, Map<String, Integer> stuffToBuy) {
		Menu prices = new Menu();
		for (Map.Entry<String, Integer> food: stuffToBuy.entrySet()) {
			orderTotal += (prices.getPrice(food.getKey())*food.getValue());
		}
		cashier.msgHereIsInvoice(this, orderTotal);
	}

	@Override
	public void msgPayInvoice(double payment) {
		totalRevenue += payment;
	}

}
