package restaurant.restaurant_cwagoner.interfaces;

import java.util.HashMap;

public interface Market {

	public abstract void msgNeedFood(Cook c, Cashier ca, HashMap<String, Integer> orderedStock);
	public abstract void msgPayment(Cashier ca, double payment);
	public abstract String getName();
	
}