package restaurant_maggiyan.interfaces;

import restaurant_maggiyan.Check;
import restaurant_maggiyan.Menu;
import restaurant_maggiyan.WaiterAgent;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	
	public void msgRestaurantFull();
	
	//From waiter 
	public void msgFollowMe(WaiterAgent w, Menu m, int tableNumber);
	
	public void msgWhatDoYouWant();
	
	public void msgHereIsYourFood();
	
	public void msgHereIsCheck(Check c);

	public void msgOutOfYourChoice();

	public String getName();
	

}