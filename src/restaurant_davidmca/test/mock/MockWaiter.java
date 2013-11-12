package restaurant_davidmca.test.mock;


import restaurant_davidmca.Check;
import restaurant_davidmca.HostAgent;
import restaurant_davidmca.Order;
import restaurant_davidmca.Table;
import restaurant_davidmca.gui.HostGui;
import restaurant_davidmca.gui.WaiterGui;
import restaurant_davidmca.interfaces.Cashier;
import restaurant_davidmca.interfaces.Customer;
import restaurant_davidmca.interfaces.Waiter;

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
	public void setHost(HostAgent host) {
		
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
	public void msgSeatAtTable(Customer c, Table t) {
		
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

	

}
