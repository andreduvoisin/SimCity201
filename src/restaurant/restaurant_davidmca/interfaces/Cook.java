package restaurant.restaurant_davidmca.interfaces;

import java.util.List;

import restaurant.restaurant_davidmca.Order;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.gui.CookGui;

public interface Cook {

	public abstract String getName();

	public abstract void setGui(CookGui g);

//	public abstract void addMarket(MarketAgent mkt);

	public abstract void msgHereIsAnOrder(Waiter w, String choice, Table t);

	public abstract void msgDoneAnimating();

//	public abstract void msgOrderFullFillment(Market mkt, List<Stock> recieved);

//	public abstract Collection<MarketAgent> getMarketList();

	public abstract List<Order> getRevolvingStand();

}