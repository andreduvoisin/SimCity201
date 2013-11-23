package restaurant_all.interfaces.Customer;

import restaurant_davidmca.Check;
import restaurant_davidmca.Menu;
import restaurant_davidmca.Table;
import restaurant_davidmca.gui.CustomerGui;
import restaurant_davidmca.interfaces.Cashier;
import restaurant_davidmca.interfaces.Host;
import restaurant_davidmca.interfaces.Waiter;

public interface Customer_dc {

	public abstract void setHost(Host host);

	public abstract double getMoney();

	public abstract String getCustomerName();

	public abstract void msgChange(Double change);

	public abstract void msgHereIsCheck(Check chk);

	public abstract void gotHungry();

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void msgFollowMe(Waiter w, Table t);

	public abstract void msgWhatWouldYouLike(Menu m);

	public abstract void msgHereIsYourOrder();

	public abstract String getName();

	public abstract int getHungerLevel();

	public abstract void setHungerLevel(int hungerLevel);

	public abstract String toString();

	public abstract void setGui(CustomerGui g);

	public abstract CustomerGui getGui();

	public abstract void setCashier(Cashier ca);

	public abstract void msgAnimationFinishedGoToWaitingArea();

	public abstract void msgAvailability(boolean availability);

}