package restaurant.restaurant_davidmca.interfaces;

import java.util.Map;

import restaurant.restaurant_davidmca.roles.DavidCashierRole;

public interface Market {

	public abstract String getName();

	public abstract void setCashier(DavidCashierRole cash);

	public abstract void msgWantToBuy(Cook c,
			Map<String, Integer> stuffToBuy);

	public abstract void msgPayInvoice(double total);

}