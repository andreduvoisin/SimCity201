package restaurant_davidmca.interfaces;

import java.util.Map;

import restaurant_davidmca.agents.CashierAgent;

public interface Market {

	public abstract String getName();

	public abstract void setCashier(CashierAgent cash);

	public abstract void msgWantToBuy(Cook c,
			Map<String, Integer> stuffToBuy);

	public abstract void msgPayInvoice(double total);

}