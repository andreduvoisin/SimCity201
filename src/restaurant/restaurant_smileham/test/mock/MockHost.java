package restaurant.restaurant_smileham.test.mock;

import java.util.Collection;
import java.util.List;

import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.agents.CashierAgent;
import restaurant.restaurant_smileham.gui.HostGui;
import restaurant.restaurant_smileham.interfaces.Cook;
import restaurant.restaurant_smileham.interfaces.Customer;
import restaurant.restaurant_smileham.interfaces.Host;
import restaurant.restaurant_smileham.interfaces.Waiter;

public class MockHost extends Mock implements Host{

	public MockHost(String name) {
		super(name);
	}

	@Override
	public void msgAddWaiter(Waiter waiter) {
		
	}

	@Override
	public void msgIWantFood(Customer customer) {
		
	}

	@Override
	public void msgLeavingTable(Customer cust) {

	}

	@Override
	public void msgLeavingRestaurant(Customer customer) {
		
	}

	@Override
	public void msgWantToGoOnBreak(Waiter waiter) {
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	@Override
	public List<Customer> getWaitingCustomers() {
		return null;
	}

	@Override
	public Collection<Table> getTables() {
		return null;
	}

	@Override
	public void setGui(HostGui gui) {
		
	}

	@Override
	public HostGui getGui() {
		return null;
	}


	@Override
	public List<Waiter> getWaiters() {
		return null;
	}

	@Override
	public int getNumWorkingWaiters() {
		return 0;
	}

	@Override
	public boolean isRestaurantFull() {
		return false;
	}

}
