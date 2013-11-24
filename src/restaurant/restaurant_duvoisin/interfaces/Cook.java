package restaurant.restaurant_duvoisin.interfaces;

public interface Cook {
	public abstract void msgHereIsOrder(Waiter w, String choice, int table);
	public abstract void msgFailedToFulfillRequest(Market ma, String item, int amount);
	public abstract void msgReplenishFood(String item, int amount);
	public abstract void msgGotFood(int position);
}