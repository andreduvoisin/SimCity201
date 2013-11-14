package restaurant_davidmca.gui;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant_davidmca.CashierAgent;
import restaurant_davidmca.CookAgent;
import restaurant_davidmca.CustomerAgent;
import restaurant_davidmca.HostAgent;
import restaurant_davidmca.MarketAgent;
import restaurant_davidmca.Table;
import restaurant_davidmca.WaiterAgent;
import restaurant_davidmca.interfaces.Customer;
import restaurant_davidmca.interfaces.Waiter;

/**
 * Panel in frame that contains all the restaurant_davidmca information, including host,
 * cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
	
	//animation grid
	static int gridX = 25;
	static int gridY = 35;

	// Host, cook, waiters and customers
	private HostAgent host = new HostAgent("Host");
	private CookAgent cook = new CookAgent("Cook", 1);
	private CashierAgent cash = new CashierAgent("Cashier");
	private HostGui hostGui = new HostGui(host);
	MarketAgent mkt1, mkt2, mkt3;

	private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();

	private JPanel restLabel = new JPanel();

	private JPanel group = new JPanel();

	private RestaurantGui gui; // reference to main gui

	public RestaurantPanel(RestaurantGui gui) {
		this.gui = gui;
		gui.animationPanel.addGui(hostGui);
		host.startThread();
		host.setCook(cook);
		mkt1 = new MarketAgent("Ralphs", 0);
		mkt2 = new MarketAgent("Smart and Final", 50);
		mkt3 = new MarketAgent("Whole Foods", 50);
		cook.addMarket(mkt1);
		cook.addMarket(mkt2);
		cook.addMarket(mkt3);
		mkt1.startThread();
		mkt2.startThread();
		mkt3.startThread();
		mkt1.setCashier(cash);
		mkt2.setCashier(cash);
		mkt3.setCashier(cash);
		CookGui cg = new CookGui(cook);
		cook.setGui(cg);
		gui.animationPanel.addGui(cg);
		cook.startThread();
		cash.startThread();
		initRestLabel();
		add(restLabel);
		add(group);
	}

	/**
	 * Sets up the restaurant_davidmca label that includes the menu, and host and cook
	 * information
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
			Collection<WaiterAgent> waitersList = host.getWaitersList();
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

	public void addPerson(String type, String name, boolean isHungry) {

		if (type.equals("Customers")) {
			CustomerAgent c = new CustomerAgent(name);
			CustomerGui g = new CustomerGui(c, gui, host.getCustomerIndex());

			gui.animationPanel.addGui(g);
			c.setHost(host);
			c.setCashier(cash);
			c.setGui(g);
			customers.add(c);
			if (isHungry) {
				g.setHungry();
			}
			c.startThread();
		}

		if (type.equals("Waiters")) {
			WaiterAgent w = new WaiterAgent(name);
			WaiterGui g = new WaiterGui(w, host.getWaitersList().size());

			gui.animationPanel.addGui(g);
			w.setHost(host);
			w.setGui(g);
			host.addWaiter(w);
			w.setCashier(cash);
			w.startThread();
		}
	}

	public void addTable(int x, int y, int seats) {
		Table newTable = new Table(host.tables.size() + 1, x, y, seats);
		host.tables.add(newTable);
	}

	public boolean areWaiters() {
		return host.AreWaiters();
	}

}
