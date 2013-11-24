package restaurant_all.interfaces.Host;

import java.util.Collection;
import java.util.List;

import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.gui.HostGui;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import restaurant.restaurant_davidmca.roles.CookRole;
import restaurant.restaurant_davidmca.roles.CustomerRole;
import restaurant_all.interfaces.Customer.Customer_dc;
import restaurant_all.roles.Host.HostRole_dc.MyWaiter;

public interface Host_dc {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract void addWaiter(Waiter newWaiter);

	public abstract List<CustomerRole> getWaitingCustomers();

	public abstract Collection<Waiter> getWaitersList();

	public abstract Collection<Table> getTables();

	public abstract Table getAvailableTable();

	public abstract MyWaiter getWaiter();

	public abstract void msgCheckAvailability(Customer_dc cust);

	public abstract void msgIWantFood(Customer_dc cust);

	public abstract void msgTableIsEmpty(Table t);

	public abstract void msgGoOnBreak(Waiter waiter);

	public abstract void msgGoOffBreak(Waiter waiter);

	public abstract void setGui(HostGui gui);

	public abstract HostGui getGui();

	public abstract void setCook(CookRole cook);

	public abstract boolean AreWaiters();

	public abstract int getCustomerIndex();

}