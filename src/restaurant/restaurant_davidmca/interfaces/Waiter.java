package restaurant.restaurant_davidmca.interfaces;

import restaurant.restaurant_davidmca.Check;
import restaurant.restaurant_davidmca.Order;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.gui.HostGui;
import restaurant.restaurant_davidmca.gui.WaiterGui;
import restaurant.restaurant_davidmca.roles.DavidHostRole;

public interface Waiter {

	public abstract boolean isOnBreak();

	public abstract void setHost(DavidHostRole host);

	public abstract void setCashier(Cashier cash);

	public abstract void setGui(WaiterGui gui);

	public abstract String getMaitreDName();

	public abstract String getName();

	/**
	 * Message
	 */

	public abstract void msgReadyForCheck(Customer c, String choice);

	public abstract void msgDoneAndPaying(Customer c);

	public abstract void msgHereIsCheck(Check chk);

	public abstract void msgSeatAtTable(Customer c, Table t, int home);

	public abstract void msgReadyToOrder(Customer c);

	public abstract void msgHereIsMyOrder(Customer c, String choice);

	public abstract void msgOutOfFood(String choice);

	public abstract void msgOrderIsReady(Order order);

	public abstract void msgDoneEating(Customer c);

	public abstract void msgBreakReply(Boolean breakResponse);

	public abstract void msgDoneAnimating();

	public abstract void RequestBreak();

	public abstract void setGui(HostGui gui);

	public abstract WaiterGui getGui();

	public abstract void startThread();

}