package restaurant.restaurant_duvoisin.test.mock;

import java.util.Map;

import restaurant.restaurant_duvoisin.interfaces.Cashier;
import restaurant.restaurant_duvoisin.interfaces.Market;

public class MockMarket extends Mock implements Market {
	public Cashier cashier;
	public EventLog log = new EventLog();
	public String type;
	public double payment;
	
	public MockMarket(String name) {
		super(name);
	}

	@Override
	public void msgOrderFood(Map<String, Integer> orders) {
		
	}

	@Override
	public void msgFoodPayment(String type, double payment) {
		log.add(new LoggedEvent("msgFoodPayment received"));
		this.type = type;
		this.payment = payment;
	}

	@Override
	public void msgNotEnoughMoney(String type, double payment) {
		
	}
}
