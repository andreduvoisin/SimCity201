package restaurant.restaurant_smileham.test.mock;

import java.util.List;

import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.gui.WaiterGui;
import restaurant.restaurant_smileham.interfaces.SmilehamCook;
import restaurant.restaurant_smileham.interfaces.SmilehamCustomer;
import restaurant.restaurant_smileham.interfaces.SmilehamHost;
import restaurant.restaurant_smileham.interfaces.SmilehamWaiter;

public class SmilehamMockWaiter extends SmilehamMock implements SmilehamWaiter {

	public SmilehamMockWaiter(String name) {
		super(name);
	}

	@Override
	public void msgSeatCustomer(Table table, SmilehamCustomer customer) {
		log.add(new LoggedEvent("msgSeatCustomer(" + table + ", " + customer + ")"));
		
	}

	@Override
	public void msgReadyToOrder(SmilehamCustomer customer) {
		
	}

	@Override
	public void msgHereIsMyChoice(SmilehamCustomer customer, EnumFoodOptions choice) {
		
	}

	@Override
	public void msgNotGettingFood(SmilehamCustomer customer) {
		
	}

	@Override
	public void msgOrderIsReady(Order order, List<EnumFoodOptions> foods) {
		
	}

	@Override
	public void msgOutOfFood(Order order, List<EnumFoodOptions> foods) {
		
	}

	@Override
	public void msgDoneEating(SmilehamCustomer customer) {
		
	}

	@Override
	public void msgBreakReply(boolean reply) {
		
	}

	@Override
	public void msgNewMenu(List<EnumFoodOptions> foods) {
		
	}

	@Override
	public void msgReadyForCheck(EnumFoodOptions choice, SmilehamCustomer customer) {
		
	}

	@Override
	public void msgCustomerLeaving(SmilehamCustomer customer) {
		
	}

	@Override
	public void msgHereIsCheck(Order order, Check check) {
		
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
	public SmilehamHost getHost() {
		return null;
	}

	@Override
	public SmilehamCook getCook() {
		return null;
	}

	@Override
	public boolean isWorking() {
		return false;
	}


}
