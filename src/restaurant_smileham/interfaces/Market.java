package restaurant_smileham.interfaces;

import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.test.mock.EventLog;

public interface Market {
	
	public EventLog log = new EventLog();
	
	//Messages
	public abstract void msgOrderFood(EnumFoodOptions food, int amount);
	public abstract void msgPayingMarket(int amount);
	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
//	private void fulfillOrder(final EnumFoodOptions food);
	
	//Accessors
	public abstract String getName();
	public abstract boolean isOut();
	public abstract String toString();

}
