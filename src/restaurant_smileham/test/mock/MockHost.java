package restaurant_smileham.test.mock;

import java.util.Collection;
import java.util.List;

import restaurant_smileham.Table;
import restaurant_smileham.agents.CashierAgent;
import restaurant_smileham.gui.HostGui;
import restaurant_smileham.interfaces.Cook;
import restaurant_smileham.interfaces.Customer;
import restaurant_smileham.interfaces.Host;
import restaurant_smileham.interfaces.Waiter;

public class MockHost extends Mock implements Host{

	public MockHost(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgAddWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantFood(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingRestaurant(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWantToGoOnBreak(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Customer> getWaitingCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Table> getTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGui(HostGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HostGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Waiter> getWaiters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumWorkingWaiters() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isRestaurantFull() {
		// TODO Auto-generated method stub
		return false;
	}

}
