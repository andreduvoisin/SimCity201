package restaurant_davidmca.interfaces;

import java.util.Collection;
import java.util.List;

import restaurant_davidmca.Order;
import restaurant_davidmca.Stock;
import restaurant_davidmca.Table;
import restaurant_davidmca.agents.MarketAgent;
import restaurant_davidmca.gui.CookGui;

public interface Cook {

	public abstract String getName();

	public abstract void setGui(CookGui g);

	public abstract void addMarket(MarketAgent mkt);

	public abstract void msgHereIsAnOrder(Waiter w, String choice, Table t);

	public abstract void msgDoneAnimating();

	public abstract void msgOrderFullFillment(Market mkt, List<Stock> recieved);

	public abstract Collection<MarketAgent> getMarketList();

	public abstract List<Order> getRevolvingStand();

}