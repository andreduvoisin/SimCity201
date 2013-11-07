package restaurant.test.mock;

import java.util.List;

import agent.Check;
import restaurant.Food.EnumFoodOptions;
import restaurant.Order;
import restaurant.Table;
import restaurant.agents.CookAgent;
import restaurant.agents.CustomerAgent;
import restaurant.agents.HostAgent;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

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
