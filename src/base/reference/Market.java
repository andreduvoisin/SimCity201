package base.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.gui.MarketBaseGui;
import market.gui.MarketCustomerGui;
import market.gui.MarketWorkerGui;
import market.roles.MarketCashierRole;
import market.roles.MarketWorkerRole;

public class Market {
	
	public MarketCashierRole mCashier;
	public MarketWorkerRole mWorker;

	public List<MarketBaseGui> mGuis = Collections.synchronizedList(new ArrayList<MarketBaseGui>());
	public List<MarketWorkerGui> mWorkerGuis = new ArrayList<MarketWorkerGui>();
	public List<MarketCustomerGui> mCustomerGuis = new ArrayList<MarketCustomerGui>();
	
}
