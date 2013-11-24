package restaurant.restaurant_davidmca.test.mock;

import java.util.Map;

import restaurant.restaurant_davidmca.Menu;
import restaurant.restaurant_davidmca.interfaces.Cook;
import restaurant.restaurant_davidmca.interfaces.Market;
import restaurant.restaurant_davidmca.roles.CashierRole;

public class MockMarket implements Market {
	
	String name;
	CashierRole cashier;
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
	public void setCashier(CashierRole cash) {
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
