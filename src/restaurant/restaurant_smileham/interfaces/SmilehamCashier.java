package restaurant.restaurant_smileham.interfaces;

import java.util.List;

import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.test.mock.EventLog;

public interface SmilehamCashier {
	
	public EventLog log = new EventLog();
	
	//Messages
	public abstract void msgMakeCheck(Order order);
	public abstract void msgPayingCheck(Check check);
	public abstract void msgMarketBill(SmilehamMarket market, int amount);
	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
	public abstract void makeCheck(Order order);
	public abstract void giveChange(Check check);
	public abstract void payMarket(SmilehamMarket market, int amount);
	
	//Accessors
	public abstract String getName();
	public abstract List<Order> getOrders();
	public abstract String toString();

}
