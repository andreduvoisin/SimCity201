package base.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.gui.MarketBaseGui;
import market.gui.MarketCashierGui;
import market.gui.MarketCustomerGui;
import market.gui.MarketWorkerGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketDeliveryTruck;
import market.interfaces.MarketWorker;

public class Market {
	public int mMarketID;
	
	//people
	public MarketCashier mCashier;
	public List<MarketWorker> mWorkers = Collections.synchronizedList(new ArrayList<MarketWorker>());
	public MarketDeliveryTruck mDeliveryTruck;

	//guis
	public List<MarketBaseGui> mGuis = Collections.synchronizedList(new ArrayList<MarketBaseGui>());
	public List<MarketWorkerGui> mWorkerGuis = new ArrayList<MarketWorkerGui>();
	public List<MarketCustomerGui> mCustomerGuis = new ArrayList<MarketCustomerGui>();
	public List<MarketCashierGui> mCashierGuis = new ArrayList<MarketCashierGui>();
	//delivery truck gui in cityView
	
	public Market(int n) {
		mMarketID = n;
	}
}
