package restaurant.restaurant_jerryweb.interfaces;

import restaurant.restaurant_jerryweb.gui.Menu;


public interface Customer {
	
	public int QuePosX = 0;
	public int QuePosY = 0;
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	/*public abstract void HereIsYourTotal(double total);

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	public abstract void msgHereIsChange(double total);
	
	public abstract void msgHereIsCheck();
	
	public abstract void msgHereIsYourFood();
	
	public abstract void msgOrderNotAvailable(String choice);
	
	public abstract void msgWhatDoYouWant();
	
	public abstract void gotHungry();
	
	public abstract void msgSitAtTable(int tableNumber, Menu m, Waiter w);
	
	public abstract void  msgWaitInQue(int x);
	
/*
	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 
	public abstract void YouOweUs(double remaining_cost);
*/
	//public abstract void CustomerAgent(String name);
	public abstract double getCash();
	
	public abstract String getName();
}