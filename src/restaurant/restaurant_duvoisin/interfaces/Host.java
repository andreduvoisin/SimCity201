package restaurant.restaurant_duvoisin.interfaces;

public interface Host {
	public abstract void msgIWantToEat(Customer cust, int waitingPosition);
	public abstract void msgLeavingBecauseRestaurantFull(Customer cust);
	public abstract void msgTableIsFree(Waiter w, int table);
	public abstract void msgRequestGoOnBreak(Waiter w);
	public abstract void msgOffBreak(Waiter w);
}