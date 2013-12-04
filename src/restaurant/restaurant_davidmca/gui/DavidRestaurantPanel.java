package restaurant.restaurant_davidmca.gui;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant.intermediate.interfaces.RestaurantInterface;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.agents.MarketAgent;
import restaurant.restaurant_davidmca.interfaces.Customer;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import restaurant.restaurant_davidmca.roles.DavidCashierRole;
import restaurant.restaurant_davidmca.roles.DavidCookRole;
import restaurant.restaurant_davidmca.roles.DavidCustomerRole;
import restaurant.restaurant_davidmca.roles.DavidHostRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;

/**
 * Panel in frame that contains all the restaurant_davidmca information,
 * including host, cook, waiters, and customers.
 */
@SuppressWarnings("serial")
public class DavidRestaurantPanel extends JPanel  {
	static int customerCount = 0;
	static int waiterCount = 0;
	// animation grid
	static int gridX = 25;
	static int gridY = 35;

	// Host, cook, waiters and customers
	public DavidHostRole host = new DavidHostRole("Host");
	public static DavidCookRole cook;
	public DavidCashierRole cash = new DavidCashierRole("Cashier");
	private HostGui hostGui = new HostGui(host);
	MarketAgent mkt1, mkt2, mkt3;

	public Vector<DavidCustomerRole> customers = new Vector<DavidCustomerRole>();

	private JPanel restLabel = new JPanel();

	private JPanel group = new JPanel();

	private DavidRestaurantGui gui; // reference to main gui

	public DavidRestaurantPanel(DavidRestaurantGui gui) {
		this.gui = gui;
		gui.animationPanel.addGui(hostGui);
		// host.startThread();
		host.setCook(cook);
		mkt1 = new MarketAgent("Ralphs", 0);
		mkt2 = new MarketAgent("Smart and Final", 50);
		mkt3 = new MarketAgent("Whole Foods", 50);
//		cook.addMarket(mkt1);
//		cook.addMarket(mkt2);
//		cook.addMarket(mkt3);
		/*
		 * mkt1.startThread(); mkt2.startThread(); mkt3.startThread();
		 */
		mkt1.setCashier(cash);
		mkt2.setCashier(cash);
		mkt3.setCashier(cash);
		CookGui cg = new CookGui(cook);
		cook.setGui(cg);
		gui.animationPanel.addGui(cg);
		// cook.startThread();
		// cash.startThread();
		initRestLabel();
		add(restLabel);
		add(group);
	}

	/**
	 * Sets up the restaurant_davidmca label that includes the menu, and host
	 * and cook information
	 */
	private void initRestLabel() {
		JLabel label = new JLabel();
		restLabel.setLayout(new BorderLayout());
		label.setText("<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>"
				+ host.getName()
				+ "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");
		restLabel.add(label, BorderLayout.CENTER);
	}

	/**
	 * When a customer or waiter is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information will be
	 * shown
	 * 
	 * @param type
	 *            indicates whether the person is a customer or waiter
	 * @param name
	 *            name of person
	 */
	public void showInfo(String type, String name) {
		if (type.equals("Waiters")) {
			Collection<Waiter> waitersList = host.getWaitersList();
			for (Waiter temp : waitersList) {
				if (temp.getName() == name) {
					gui.updateInfoPanel(temp);
				}
			}
		}
		if (type.equals("Customers")) {

			for (int i = 0; i < customers.size(); i++) {
				Customer temp = customers.get(i);
				if (temp.getName() == name) {
					gui.updateInfoPanel(temp);
				}
			}
		}
	}

	/**
	 * Adds a customer or waiter to the appropriate list
	 * 
	 * @param type
	 *            indicates whether the person is a customer or waiter (later)
	 * @param name
	 *            name of person
	 */

	public void addCustomer(DavidCustomerRole cust) {
			CustomerGui g = new CustomerGui(cust, gui, customerCount);
			customerCount++;
			gui.animationPanel.addGui(g);
			cust.setHost(host);
			cust.setCashier(cash);
			cust.setGui(g);
			g.setHungry();
			customers.add(cust);
	}

	public void addWaiter(DavidWaiterRole waiter) {
			WaiterGui g = new WaiterGui(waiter, waiterCount);
			waiterCount++;
			gui.animationPanel.addGui(g);
			waiter.setHost(host);
			waiter.setGui(g);
			host.addWaiter(waiter);
			waiter.setCashier(cash);
	}

	public void addSharedWaiter(DavidWaiterRoleShared waiter) {
			WaiterGui g = new WaiterGui(waiter, waiterCount);
			waiterCount++;
			gui.animationPanel.addGui(g);
			waiter.setHost(host);
			waiter.setCook(cook);
			waiter.setGui(g);
			host.addWaiter(waiter);
			waiter.setCashier(cash);
	}

	public void addTable(int x, int y, int seats) {
		Table newTable = new Table(host.tables.size() + 1, x, y, seats);
		host.tables.add(newTable);
	}

	public boolean areWaiters() {
		return host.AreWaiters();
	}

}
