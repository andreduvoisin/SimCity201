package restaurant.restaurant_xurex.interfaces;


public interface Host {

	public abstract String getName();

	public abstract void IWantFood(Customer c);

	public abstract void IWillWait(Customer c);

	public abstract void IWillNotWait(Customer c);

	public abstract void IWantBreak(Waiter w);

	public abstract void IAmFree();

	public abstract void TableIsFree(int t);

	public abstract void addWaiter(Waiter w);
	
	public abstract int getWaiterNumber();

}