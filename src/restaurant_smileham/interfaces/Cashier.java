package restaurant.interfaces;

import java.util.List;

import restaurant.Order;
import restaurant.test.mock.EventLog;
import agent.Check;

public interface Cashier {
	
	public EventLog log = new EventLog();
	
	//Messages
	public abstract void msgMakeCheck(Order order);
	public abstract void msgPayingCheck(Check check);
	public abstract void msgMarketBill(Market market, int amount);
	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
	public abstract void makeCheck(Order order);
	public abstract void giveChange(Check check);
	public abstract void payMarket(Market market, int amount);
	
	//Accessors
	public abstract String getName();
	public abstract List<Order> getOrders();
	public abstract String toString();

}
