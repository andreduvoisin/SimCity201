package market.test.mock;

import java.util.Map;

import market.MarketInvoice;
import market.MarketOrder;
import market.interfaces.MarketCashier;
import base.Item.EnumItemType;
import restaurant.intermediate.interfaces.RestaurantCashierInterface;
import test.mock.LoggedEvent;
import test.mock.Mock;

public class MockRestaurantCashier extends Mock implements RestaurantCashierInterface {

	@Override
	public void msgPlacedMarketOrder(MarketOrder o, MarketCashier c) {
		log.add(new LoggedEvent("Receieved msgPlacedMarketOrder."));
	}

	@Override
	public void msgInvoiceToPerson(Map<EnumItemType, Integer> cannotFulfill,
			MarketInvoice invoice) {
		log.add(new LoggedEvent("Received msgInvoiceToPerson."));
	}

}
