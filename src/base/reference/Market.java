package base.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.gui.MarketBaseGui;
import market.gui.MarketCustomerGui;
import market.gui.MarketWorkerGui;

public class Market {

	private List<MarketBaseGui> guis = Collections.synchronizedList(new ArrayList<MarketBaseGui>());
	private List<MarketWorkerGui> mWorkerGuis = new ArrayList<MarketWorkerGui>();
	private List<MarketCustomerGui> mCustomerGuis = new ArrayList<MarketCustomerGui>();
	
}
