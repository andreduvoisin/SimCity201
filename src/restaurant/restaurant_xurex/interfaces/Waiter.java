package restaurant.restaurant_xurex.interfaces;

import restaurant.restaurant_xurex.gui.WaiterGui;
import restaurant.restaurant_xurex.utilities.*;


public interface Waiter {

	WaiterState state = null;

	public abstract String getName();

	//CUSTOMER MESSAGES
	public abstract void ReadyToOrder(Customer c);

	public abstract void HereIsChoice(Customer c, String choice);

	public abstract void Leaving(Customer c);

	//HOST MESSAGES
	public abstract void PleaseSeatCustomer(Customer c, int table);

	public abstract void okForBreak();

	public abstract void noBreak();

	//COOK MESSAGES
	public abstract void OrderIsReady(String choice, int table, int kitchen);

	public abstract void OutOfFood(int table, String choice);

	//CASHIER MESSAGES
	public abstract void HereIsBill(Customer customer, float bill);

	// ACTIONS
	public abstract void StayStill();

	public abstract void ServeFood(Order o);

	public abstract void TakeOrder(MyCustomer c);

	public abstract void SendOrder(MyCustomer c);

	public abstract void CleanTable(MyCustomer c);

	public abstract void SeatCustomer(MyCustomer c);

	public abstract void goOnBreak();

	public abstract void AskToReorder(Order o);


	//UTILITIES
	public abstract void setHost(Host host);

	public abstract void setCook(Cook cook);

	public abstract void setCashier(Cashier cashier);
	
	public abstract boolean isAvailable();
	
	public abstract boolean isGood();

	void msgAtLocation();

	void wantBreak();

	void backToWork();

	void DoGoToTable(int table);

	void DoServeCustomer(String choice, int table);

	void setGui(WaiterGui gui);

	WaiterGui getGui();

	void DoDisplayOrder(String choice, int table);

	void DoCleanFood();

	void DoGoHome();
	
	public abstract void setNumber(int number);
	
	public abstract int getNumber();
}