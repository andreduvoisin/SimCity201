package restaurant_davidmca.test.mock;

import java.util.Collection;
import java.util.List;

import restaurant_davidmca.Stock;
import restaurant_davidmca.Table;
import restaurant_davidmca.agents.MarketAgent;
import restaurant_davidmca.gui.CookGui;
import restaurant_davidmca.interfaces.Cook;
import restaurant_davidmca.interfaces.Market;
import restaurant_davidmca.interfaces.Waiter;

public class MockCook extends Mock implements Cook {

	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setGui(CookGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMarket(MarketAgent mkt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsAnOrder(Waiter w, String choice, Table t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAnimating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderFullFillment(Market mkt, List<Stock> recieved) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<MarketAgent> getMarketList() {
		// TODO Auto-generated method stub
		return null;
	}

}
