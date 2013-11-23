package restaurant_all.interfaces.Customer;

import java.awt.Dimension;
import java.util.HashMap;

import restaurant_cwagoner.interfaces.Waiter;

public interface Customer_cw {

	public abstract void msgGuiGotHungry();
	public abstract void msgRestaurantFull();
	public abstract void msgSitAtTable(Waiter w, int table, HashMap<String, Integer> menuOptions);
	public abstract void msgWhatDoYouWant();
	public abstract void msgPickSomethingElse(HashMap<String, Integer> newMenu);
	public abstract void msgHeresYourFood();
	public abstract void msgAcknowledgeLeaving();
	public abstract void msgGuiAtCashier();
	public abstract void msgYourTotalIs(double total);
	public abstract void msgThankYou();
	public abstract void msgHeresYourChange(double changeDue);
	public abstract void msgYouOwe(double remainingTotal);
	public abstract void msgGuiLeftRestaurant();
	public abstract Dimension getPosition();
	public abstract String getName();

}