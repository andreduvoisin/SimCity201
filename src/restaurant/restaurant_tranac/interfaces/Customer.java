package restaurant.restaurant_tranac.interfaces;

import restaurant.restaurant_tranac.Check;
import restaurant.restaurant_tranac.Menu;
import restaurant.restaurant_tranac.gui.CustomerGui_at;

/**
 * Customer interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface Customer {
	public abstract String getName();				//necessary for hostAgent
	
	public abstract CustomerGui_at getGui();			//necessary for waiterAgent
	
	public abstract void msgGotHungry();
	
	public abstract void msgPleaseWaitHere(int n);
	
	public abstract void msgRestaurantFull();
	
	public abstract void msgFollowMe(Menu m, Waiter w);
	
	public abstract void msgWhatDoYouWant();
	
	public abstract void msgOutOfChoice();
	
	public abstract void msgHereIsFood();
	
	public abstract void msgDoneEating();
	
	public abstract void msgHereIsCheck(Check c);
	
	public abstract void msgHereIsChange(Check c);
	
	public abstract void msgPayNextTime();
}