package restaurant_xurex.interfaces;


public interface Cashier {

	// MESSAGES //
	public abstract void ComputeBill(Waiter waiter, Customer customer);
	public abstract void IWantToPay(Customer customer, float cash);
	public abstract void HereIsBill(Market market, float payment);

	//UTILITIES
	public abstract String getName();
	public abstract float getAssets();

}