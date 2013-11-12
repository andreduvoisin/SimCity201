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
	}

	@Override
	public void msgSeatCustomer(Table table, Customer customer) {
		log.add(new LoggedEvent("msgSeatCustomer(" + table + ", " + customer + ")"));
		
	}

	@Override
	public void msgReadyToOrder(Customer customer) {
		
	}

	@Override
	public void msgHereIsMyChoice(Customer customer, EnumFoodOptions choice) {
		
	}

	@Override
	public void msgNotGettingFood(Customer customer) {
		
	}

	@Override
	public void msgOrderIsReady(Order order, List<EnumFoodOptions> foods) {
		
	}

	@Override
	public void msgOutOfFood(Order order, List<EnumFoodOptions> foods) {
		
	}

	@Override
	public void msgDoneEating(Customer customer) {
		
	}

	@Override
	public void msgBreakReply(boolean reply) {
		
	}

	@Override
	public void msgNewMenu(List<EnumFoodOptions> foods) {
		
	}

	@Override
	public void msgWantBreak() {
		
	}

	@Override
	public void msgReadyForCheck(EnumFoodOptions choice, Customer customer) {
		
	}

	@Override
	public void msgCustomerLeaving(Customer customer) {
		
	}

	@Override
	public void msgHereIsCheck(Order order, Check check) {
		log.add(new LoggedEvent("msgHereIsCheck(" + order + ", " + check + ")"));
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	@Override
	public void setGui(WaiterGui gui) {
		
	}

	@Override
	public WaiterGui getGui() {
		return null;
	}

	@Override
	public List<Order> getOrders() {
		return null;
	}

	@Override
	public Host getHost() {
		return null;
	}

	@Override
	public Cook getCook() {
		return null;
	}

	@Override
	public boolean isWorking() {
		return false;
	}

}
