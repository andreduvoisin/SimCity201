package restaurant.restaurant_xurex.test.mock;


import restaurant.restaurant_xurex.interfaces.Cashier;
import restaurant.restaurant_xurex.interfaces.Cook;
import restaurant.restaurant_xurex.interfaces.Customer;
import restaurant.restaurant_xurex.interfaces.Host;
import restaurant.restaurant_xurex.interfaces.Waiter;
import restaurant.restaurant_xurex.interfaces.WaiterGui_;
import restaurant.restaurant_xurex.utilities.MyCustomer;
import restaurant.restaurant_xurex.utilities.Order;
import restaurant.restaurant_xurex.utilities.WaiterState;

/**
 * A MockCustomer built to unit test a CashierAgent.
 *
 * @author Rex Xu
 *
 */
public class MockWaiter extends Mock implements Waiter {

	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	WaiterState state = null;

	public String getName() {
		return null;
	}

	//CUSTOMER MESSAGES
	public void ReadyToOrder(Customer c) {
	}

	public void HereIsChoice(Customer c, String choice) {
	}

	public void Leaving(Customer c) {
	}

	//HOST MESSAGES
	public void PleaseSeatCustomer(Customer c, int table) {
	}

	public void okForBreak() {
	}

	public void noBreak() {
	}

	//COOK MESSAGES
	public void OrderIsReady(String choice, int table, int kitchen) {
		log.add(new LoggedEvent("OrderIsReady: "+choice+" "+table));
	}

	public void OutOfFood(int table, String choice) {
	}

	//CASHIER MESSAGES
	public void HereIsBill(Customer customer, float bill) {
		log.add(new LoggedEvent("HereIsBill: "+bill));
	}

	// ACTIONS
	public void StayStill() {
	}

	public void ServeFood(Order o) {
	}

	public void TakeOrder(MyCustomer c) {
	}

	public void SendOrder(MyCustomer c) {
	}

	public void CleanTable(MyCustomer c) {
	}

	public void SeatCustomer(MyCustomer c) {
	}

	public void goOnBreak() {
	}

	public void AskToReorder(Order o) {
	}


	//UTILITIES
	public void setHost(Host host) {
	}

	public void setCook(Cook cook) {
	}

	public void setCashier(Cashier cashier) {
	}

	public void msgAtLocation() {
	}

	public void wantBreak() {
	}

	public void backToWork() {
	}

	public void DoGoToTable(int table) {
	}

	public void DoServeCustomer(String choice, int table) {
	}

	public void setGui(WaiterGui_ gui) {
	}

	public WaiterGui_ getGui() {
		return null;
	}

	public void DoDisplayOrder(String choice, int table) {
	}

	public void DoCleanFood() {
	}

	public void DoGoHome() {
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setNumber(int number) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

}
