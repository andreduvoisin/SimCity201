package restaurant.restaurant_duvoisin.interfaces;

import restaurant.restaurant_duvoisin.Menu;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	//public abstract void HereIsYourTotal(double total);

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	//public abstract void HereIsYourChange(double total);


	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
	//public abstract void YouOweUs(double remaining_cost);

	public abstract void msgGotHungry();
	public abstract void msgFollowMeToTable(Waiter w, Menu m, int t);
	public abstract void msgAtSeat();
	public abstract void msgWhatWouldYouLike();
	public abstract void msgOutOfChoice(Menu m);
	public abstract void msgHereIsYourFood(String myChoice);
	public abstract void msgHereIsCheck(double amount);
	public abstract void msgHereIsChange(double change);
	public abstract void msgDoneLeaving();
	public abstract void msgRestaurantFull();
}