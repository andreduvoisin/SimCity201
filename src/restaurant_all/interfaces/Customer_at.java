package restaurant_all.interfaces;

import restaurant_tranac.Check;
import restaurant_tranac.Menu;
import restaurant_tranac.gui.CustomerGui;
import restaurant_tranac.interfaces.Waiter;

/**
 * Customer interface built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public interface Customer_at {
	public abstract String getName();				//necessary for hostAgent
	
	public abstract CustomerGui getGui();			//necessary for waiterAgent
	
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