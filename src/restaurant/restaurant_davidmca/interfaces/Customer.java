package restaurant.restaurant_davidmca.interfaces;

import restaurant.restaurant_davidmca.Check;
import restaurant.restaurant_davidmca.Menu;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.gui.CustomerGui;

public interface Customer {

	public abstract void setHost(Host host);

	public abstract double getMoney();

	public abstract String getCustomerName();

	public abstract void msgChange(Double change);

	public abstract void msgHereIsCheck(Check chk);

	public abstract void gotHungry();

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

	public abstract void msgDoneAnimating();


}