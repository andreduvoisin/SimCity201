package restaurant.restaurant_davidmca.interfaces;

import java.util.Collection;
import java.util.List;

import restaurant.restaurant_davidmca.MyWaiter;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.agents.CookAgent;
import restaurant.restaurant_davidmca.agents.CustomerAgent;
import restaurant.restaurant_davidmca.gui.HostGui;

public interface Host {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract void addWaiter(Waiter newWaiter);

	public abstract List<CustomerAgent> getWaitingCustomers();

	public abstract Collection<Waiter> getWaitersList();

	public abstract Collection<Table> getTables();

	public abstract Table getAvailableTable();

	public abstract MyWaiter getWaiter();

	public abstract void msgCheckAvailability(CustomerAgent cust);

	public abstract void msgIWantFood(CustomerAgent cust);

	public abstract void msgTableIsEmpty(Table t);

	public abstract void msgGoOnBreak(Waiter waiter);

	public abstract void msgGoOffBreak(Waiter waiter);

	public abstract void setGui(HostGui gui);

	public abstract HostGui getGui();

	public abstract void setCook(CookAgent cook);

	public abstract boolean AreWaiters();

	public abstract int getCustomerIndex();

}