package restaurant_all.interfaces;

import restaurant_maggiyan.Check;
import restaurant_maggiyan.Menu;
import restaurant_maggiyan.interfaces.Waiter;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer_my {
	
	public void msgRestaurantFull();
	
	//From waiter 
	public void msgFollowMe(Waiter w, Menu m, int tableNumber);
	
	public void msgWhatDoYouWant();
	
	public void msgHereIsYourFood();
	
	public void msgHereIsCheck(Check c);

	public void msgOutOfYourChoice();

	public String getName();
	

}