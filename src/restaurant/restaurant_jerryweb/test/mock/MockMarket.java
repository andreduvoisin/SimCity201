package restaurant.restaurant_jerryweb.test.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import restaurant.restaurant_jerryweb.JerrywebCookRole;
import restaurant.restaurant_jerryweb.JerrywebMarketRole.Order;
import restaurant.restaurant_jerryweb.interfaces.Cashier;
import restaurant.restaurant_jerryweb.interfaces.Cook;
import restaurant.restaurant_jerryweb.interfaces.Market;

public class MockMarket extends Mock implements Market {

	public Cashier cashier;
	public Cook cook;
	
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	/*public List<Order> getRestockList(){
		log.add(new LoggedEvent("Returning the restocking list for the market"));
		return restockOrder;
	}*/
	
	public void CompleteTransaction(Order order){
		log.add(new LoggedEvent("Completing the transaction with the cashier"));
	}
			
	public void processRequest(Order order){
		log.add(new LoggedEvent("We are processing the cook's order"));
	}
	
	@Override
	public void msgGiveMeOrder(String choice, Map<String, JerrywebCookRole.Food> foodMap){
		log.add(new LoggedEvent("Cook is ordering more " + choice));
	}
	
	@Override
	public void msgPayment(double income, int BN){
		log.add(new LoggedEvent("The cashier has paid us " + income));
	}

	public boolean pickAndExecuteAnAction() {
		log.add(new LoggedEvent("Market Scheduler has run."));
		return true;
	}
}
