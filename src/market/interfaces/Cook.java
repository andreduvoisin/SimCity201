package market.interfaces;

import restaurant_smileham.Order;
import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.gui.CookGui;
import restaurant_smileham.test.mock.EventLog;

public interface Cook {
	
	public EventLog log = new EventLog();
	
	//Messages
	public abstract void msgMakeFood(Order order);
	public abstract void msgOrderResponse(EnumFoodOptions food, int newIncomingAmount);
	public abstract void msgOrderFulfillment(EnumFoodOptions food, int amountArrived);
	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
	public abstract void addMarket();
//	private void cookFood(final Food food);
//	private void orderFood(EnumFoodOptions food);
	public abstract void refreshLabels();
	
	//Accessors
	public abstract String getName();
	public abstract void setGui(CookGui gui);
	public abstract CookGui getGui();
	public abstract String toString();

}
