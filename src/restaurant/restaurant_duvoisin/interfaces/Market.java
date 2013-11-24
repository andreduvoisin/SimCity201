package restaurant.restaurant_duvoisin.interfaces;

import java.util.Map;

public interface Market {
	public abstract void msgOrderFood(Map<String, Integer> orders);
	public abstract void msgFoodPayment(String type, double payment);
	public abstract void msgNotEnoughMoney(String type, double payment);
}