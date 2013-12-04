package restaurant.restaurant_smileham.interfaces;

import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.Menu;
import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.gui.CustomerGui;
import restaurant.restaurant_smileham.test.mock.EventLog;

public interface SmilehamCustomer{
	
	public EventLog log = new EventLog();
	
	//Messages
	public abstract void msgGotHungry();
	public abstract void msgRestaurantFull();
	public abstract void msgSitAtTable(SmilehamWaiter waiter, int tableNum, Menu menu);
	public abstract void msgAnimationFinishedGoToSeat();
	public abstract void msgWhatWouldYouLike(Menu menu);
	public abstract void msgHereIsYourFood(EnumFoodOptions choice);
	public abstract void msgCheckDelivered(Check check);
	public abstract void msgGoodToGo(int change);
	
	//Animation
	public abstract void msgAnimationFinishedLeaveRestaurant();
	public void msgAnimationPickedUp();
	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
//	private void acquireSemaphore(Semaphore semaphore);
//	private void goToRestaurant();
//	private void sitDown();
//	private void getWaitersAttention();
//	private void orderFood();
//	private void eatFood();
//	private void askForCheck();
//	private void payCheck();
//	private void leaveTable();
//	private void leaveRestaurant();
	
	//Actions
	
	//Accessors
	public abstract void setHost(SmilehamHost host);
	public abstract String getCustomerName();
	public abstract String getName();
	public abstract int getHungerLevel();
	public abstract int getCash();
	public abstract void setHungerLevel(int hungerLevel);
	public abstract void setGui(CustomerGui g);
	public abstract CustomerGui getGui();
	public abstract boolean isHungry();
	public abstract String toString();

}