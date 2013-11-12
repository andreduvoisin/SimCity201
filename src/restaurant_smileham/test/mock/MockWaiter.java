package restaurant_smileham.test.mock;

import java.util.List;

import restaurant_smileham.Order;
import restaurant_smileham.Table;
import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.agent.Check;
import restaurant_smileham.agents.CookAgent;
import restaurant_smileham.agents.CustomerAgent;
import restaurant_smileham.agents.HostAgent;
import restaurant_smileham.gui.WaiterGui;
import restaurant_smileham.interfaces.Cook;
import restaurant_smileham.interfaces.Customer;
import restaurant_smileham.interfaces.Host;
import restaurant_smileham.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgSeatCustomer(Table table, Customer customer) {
		log.add(new LoggedEvent("msgSeatCustomer(" + table + ", " + customer + ")"));
		
	}

	@Override
	public void msgReadyToOrder(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(Customer customer, EnumFoodOptions choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNotGettingFood(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(Order order, List<EnumFoodOptions> foods) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(Order order, List<EnumFoodOptions> foods) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakReply(boolean reply) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNewMenu(List<EnumFoodOptions> foods) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWantBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyForCheck(EnumFoodOptions choice, Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCustomerLeaving(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Order order, Check check) {
		log.add(new LoggedEvent("msgHereIsCheck(" + order + ", " + check + ")"));
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setGui(WaiterGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> getOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Host getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cook getCook() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWorking() {
		// TODO Auto-generated method stub
		return false;
	}

}
