package restaurant.restaurant_maggiyan.interfaces;

import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.Menu;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface MaggiyanCustomer {
	
	public void msgRestaurantFull();
	
	//From waiter 
	public void msgFollowMe(MaggiyanWaiter w, Menu m, int tableNumber);
	
	public void msgWhatDoYouWant();
	
	public void msgHereIsYourFood();
	
	public void msgHereIsCheck(Check c);

	public void msgOutOfYourChoice();

	public String getName();
	

}