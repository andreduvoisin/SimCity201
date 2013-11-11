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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHost(HostAgent host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(WaiterGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgReadyForCheck(Customer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAndPaying(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Check chk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSeatAtTable(Customer c, Table t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyOrder(Customer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakReply(Boolean breakResponse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAnimating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RequestBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(HostGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
