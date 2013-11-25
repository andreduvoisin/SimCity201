package restaurant.restaurant_maggiyan.interfaces;

import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.Menu;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	
	public void msgRestaurantFull();
	
	//From waiter 
	public void msgFollowMe(Waiter w, Menu m, int tableNumber);
	
	public void msgWhatDoYouWant();
	
	public void msgHereIsYourFood();
	
	public void msgHereIsCheck(Check c);

	public void msgOutOfYourChoice();

	public String getName();
	

}