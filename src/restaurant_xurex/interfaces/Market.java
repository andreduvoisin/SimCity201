package restaurant_xurex.interfaces;

import java.util.Map;

public interface Market {

	// MESSAGES
	public abstract void HereIsOrder(Map<String, Integer> order);
	public abstract void HereIsPayment(float payment);

	//UTILITIES
	public abstract String getName();
	public abstract float getAssets();
	public abstract int getQuantity(String food);

}