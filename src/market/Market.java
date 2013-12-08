package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.gui.MarketBaseGui;
import market.gui.MarketItemsGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketDeliveryTruck;
import market.interfaces.MarketWorker;

public class Market {
	public int mMarketID;
	
	//people
	public MarketCashier mCashier;
	public List<MarketWorker> mWorkers = Collections.synchronizedList(new ArrayList<MarketWorker>());
	//public List<MarketCustomer> mCustomers = Collections.synchronizedList(new ArrayList<MarketCustomer>());
	public MarketDeliveryTruck mDeliveryTruck;

	//guis
	public List<MarketBaseGui> mGuis = Collections.synchronizedList(new ArrayList<MarketBaseGui>());
	public MarketItemsGui mItemsGui;
	//delivery truck gui in cityView
	
	public Market(int n) {
		mMarketID = n;
	}
}
