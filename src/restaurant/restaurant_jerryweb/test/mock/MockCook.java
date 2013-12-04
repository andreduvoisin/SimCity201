package restaurant.restaurant_jerryweb.test.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import restaurant.restaurant_jerryweb.JerrywebCookRole.Food;
import restaurant.restaurant_jerryweb.JerrywebCookRole.Order;
import restaurant.restaurant_jerryweb.JerrywebCookRole.OrderState;
import restaurant.restaurant_jerryweb.JerrywebMarketRole;
import restaurant.restaurant_jerryweb.interfaces.Cashier;
import restaurant.restaurant_jerryweb.interfaces.Cook;
import restaurant.restaurant_jerryweb.interfaces.Waiter;

public class MockCook extends Mock implements Cook {
	
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public Waiter waiter;
	public Cashier cashier;
	
	public MockCook(String name) {
		super(name);
	}
	
	public void msgOutOfStock(JerrywebMarketRole market, String choice){
		log.add(new LoggedEvent("Market: " + market.getName() + " has sent the outOfStock message for " + choice));
	}
	
	public void addToRevolvingStand(Waiter w,String custOrder, int t, OrderState orderS){
		log.add(new LoggedEvent("Checking the revolving stand for orders from waiters"));
	}

	
	@Override
	public void msgHereIsOrder(String choice, Map<String, Food> fm) {
		log.add(new LoggedEvent("The market has sent the order for " + choice + ", plus anything else needed"));
		
	}

	@Override
	public void msgCookThis(Waiter w, String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgfoodDone(Order order) {
		log.add(new LoggedEvent("Message foodDone was called"));
	}

	@Override
	public void msgGiveMeOrder(int t) {
		log.add(new LoggedEvent("A waiter has sent a messege GiveMeOrder for the order belonging to table: " + t));
	}
	
	
	
}