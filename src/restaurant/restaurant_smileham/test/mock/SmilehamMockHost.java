package restaurant.restaurant_smileham.test.mock;

import java.util.Collection;
import java.util.List;

import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.gui.HostGui;
import restaurant.restaurant_smileham.interfaces.SmilehamCustomer;
import restaurant.restaurant_smileham.interfaces.SmilehamHost;
import restaurant.restaurant_smileham.interfaces.SmilehamWaiter;

public class SmilehamMockHost extends SmilehamMock implements SmilehamHost{

	public SmilehamMockHost(String name) {
		super(name);
	}

	@Override
	public void msgAddWaiter(SmilehamWaiter waiter) {
		
	}

	@Override
	public void msgIWantFood(SmilehamCustomer customer) {
		
	}

	@Override
	public void msgLeavingTable(SmilehamCustomer cust) {

	}

	@Override
	public void msgLeavingRestaurant(SmilehamCustomer customer) {
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	@Override
	public List<SmilehamCustomer> getWaitingCustomers() {
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
	public List<SmilehamWaiter> getWaiters() {
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
