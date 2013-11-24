package restaurant.restaurant_smileham.interfaces;

import java.util.List;

import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.gui.WaiterGui;
import restaurant.restaurant_smileham.test.mock.EventLog;

public interface Waiter {
	
	public EventLog log = new EventLog(); //SHANE: 3 is this allowed?
	
	//Messages
	public abstract void msgSeatCustomer(Table table, Customer customer);
	public abstract void msgReadyToOrder(Customer customer);
	public abstract void msgHereIsMyChoice(Customer customer, EnumFoodOptions choice);
	public abstract void msgNotGettingFood(Customer customer);
	public abstract void msgOrderIsReady(Order order, List<EnumFoodOptions> foods);
	public abstract void msgOutOfFood(Order order, List<EnumFoodOptions> foods);
	public abstract void msgDoneEating(Customer customer);
	public abstract void msgBreakReply(boolean reply);
	public abstract void msgNewMenu(List<EnumFoodOptions> foods);
	public abstract void msgWantBreak();
	public abstract void msgReadyForCheck(EnumFoodOptions choice, Customer customer);
	public abstract void msgHereIsCheck(Order order, Check check);
	public abstract void msgCustomerLeaving(Customer customer);
	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
//	private void acquireSemaphore(Semaphore semaphore);
//	private void seatCustomer(Order order, Table table); 
//	private void getOrderFromCustomer(Order order);
//	private void deliverOrder(Order order);
//	private void getOrderFromCook(Order order);
//	private void deliverFoodToCustomer(Order order);
//	private void paymentToCashier(Order order);
//	private void changeToCustomer(Order order);
//	private void cleanUp(Order order);
//	private void askForBreak();
//	private void updateMenu();
	
	//Accessors
	public abstract String getName(); 
	public abstract void setGui(WaiterGui gui); 
	public abstract WaiterGui getGui();
	public abstract String toString(); 
	public abstract List<Order> getOrders();
	public abstract Host getHost();
	public abstract Cook getCook();
	public abstract boolean isWorking();
}
