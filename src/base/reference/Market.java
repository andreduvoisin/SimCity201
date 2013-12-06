package base.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.gui.MarketBaseGui;
import market.gui.MarketCustomerGui;
import market.gui.MarketWorkerGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketDeliveryTruck;
import market.interfaces.MarketWorker;
import market.roles.MarketCashierRole;
import market.roles.MarketDeliveryTruckRole;
import market.roles.MarketWorkerRole;

public class Market {
	
	//people
	public MarketCashier mCashier;
	public List<MarketWorker> mWorkers = Collections.synchronizedList(new ArrayList<MarketWorker>());
	public MarketDeliveryTruck mDeliveryTruck;

	//guis
	public List<MarketBaseGui> mGuis = Collections.synchronizedList(new ArrayList<MarketBaseGui>());
	public List<MarketWorkerGui> mWorkerGuis = new ArrayList<MarketWorkerGui>();
	public List<MarketCustomerGui> mCustomerGuis = new ArrayList<MarketCustomerGui>();
	//delivery truck gui in cityView
}
