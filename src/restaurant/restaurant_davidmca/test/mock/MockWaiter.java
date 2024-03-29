package restaurant.restaurant_davidmca.test.mock;


import restaurant.restaurant_davidmca.Check;
import restaurant.restaurant_davidmca.Order;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.gui.HostGui;
import restaurant.restaurant_davidmca.gui.WaiterGui;
import restaurant.restaurant_davidmca.interfaces.Cashier;
import restaurant.restaurant_davidmca.interfaces.Customer;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import restaurant.restaurant_davidmca.roles.DavidHostRole;

public class MockWaiter extends Mock implements Waiter {
	
	public EventLog log = new EventLog();

	public MockWaiter(String name) {
		super(name);
	}

	@Override
	public boolean isOnBreak() {
		return false;
	}

	@Override
	public void setHost(DavidHostRole host) {
		
	}

	@Override
	public void setCashier(Cashier cash) {
		
	}

	@Override
	public void setGui(WaiterGui gui) {
		
	}

	@Override
	public String getMaitreDName() {
		return null;
	}

	@Override
	public void msgReadyForCheck(Customer c, String choice) {
		
	}

	@Override
	public void msgDoneAndPaying(Customer c) {
		
	}

	@Override
	public void msgHereIsCheck(Check chk) {
		
	}

	@Override
	public void msgSeatAtTable(Customer c, Table t, int home) {
		
	}

	@Override
	public void msgReadyToOrder(Customer c) {
		
	}

	@Override
	public void msgHereIsMyOrder(Customer c, String choice) {
		
	}

	@Override
	public void msgOutOfFood(String choice) {
		
	}

	@Override
	public void msgOrderIsReady(Order order) {
		
	}

	@Override
	public void msgDoneEating(Customer c) {
		
	}

	@Override
	public void msgBreakReply(Boolean breakResponse) {
		
	}

	@Override
	public void msgDoneAnimating() {
		
	}

	@Override
	public void RequestBreak() {
		
	}

	@Override
	public void setGui(HostGui gui) {
		
	}

	@Override
	public WaiterGui getGui() {
		return null;
	}

	@Override
	public void startThread() {
	}

	

}
