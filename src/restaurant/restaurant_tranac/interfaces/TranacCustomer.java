package restaurant.restaurant_tranac.interfaces;

import restaurant.restaurant_tranac.TranacCheck;
import restaurant.restaurant_tranac.TranacMenu;
import restaurant.restaurant_tranac.gui.TranacCustomerGui;

/**
 * Customer interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface TranacCustomer {
	public abstract String getName();				//necessary for hostAgent
	
	public abstract TranacCustomerGui getGui();			//necessary for waiterAgent
	
	public abstract void msgGotHungry();
	
	public abstract void msgPleaseWaitHere(int n);
	
	public abstract void msgRestaurantFull();
	
	public abstract void msgFollowMe(TranacMenu m, TranacWaiter w);
	
	public abstract void msgWhatDoYouWant();
	
	public abstract void msgOutOfChoice();
	
	public abstract void msgHereIsFood();
	
	public abstract void msgDoneEating();
	
	public abstract void msgHereIsCheck(TranacCheck c);
	
	public abstract void msgHereIsChange(TranacCheck c);
	
	public abstract void msgPayNextTime();
}