package restaurant.restaurant_davidmca.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.Timer;

import restaurant.restaurant_davidmca.roles.DavidCashierRole;
import restaurant.restaurant_davidmca.roles.DavidCookRole;
import restaurant.restaurant_davidmca.roles.DavidCustomerRole;
import restaurant.restaurant_davidmca.roles.DavidHostRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;
import base.Gui;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings("serial")
public class DavidAnimationPanel extends CityCard implements ActionListener {

	static int customerCount = 0;
	static int waiterCount = 0;
	
	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private static List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	/*
	 * Data
	 */

	public static DavidHostRole host;
	public static DavidCookRole cook;
	public static DavidCashierRole cashier;
	public static Vector<DavidCustomerRole> customers;

	/*
	 * Constructor
	 */

	public DavidAnimationPanel(SimCityGui city) {
		super(city);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		customers = new Vector<DavidCustomerRole>();
		Timer timer = new Timer(Time.cSYSCLK / 40, this);
		timer.start();
	}

	public static void addCustomer(DavidCustomerRole cust) {
		CustomerGui g = new CustomerGui(cust, customerCount);
		customerCount++;
		addGui(g);
		cust.setHost(host);
		cust.setCashier(cashier);
		cust.setGui(g);
		g.setHungry();
		customers.add(cust);
	}

	public static void addWaiter(DavidWaiterRole waiter) {
		WaiterGui g = new WaiterGui(waiter, waiterCount);
		waiterCount++;
		addGui(g);
		waiter.setHost(host);
		waiter.setGui(g);
		host.addWaiter(waiter);
		waiter.setCashier(cashier);
	}

	public static void addSharedWaiter(DavidWaiterRoleShared waiter) {
		WaiterGui g = new WaiterGui(waiter, waiterCount);
		waiterCount++;
		addGui(g);
		waiter.setHost(host);
		waiter.setCook(cook);
		waiter.setGui(g);
		host.addWaiter(waiter);
		waiter.setCashier(cashier);
	}

	public void actionPerformed(ActionEvent e) {
		repaint(); // Will have paintComponent called
		synchronized (guis) {
			for (Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

	public static void addGui(WaiterGui g) {
		guis.add(g);
	}

	public static void addGui(CustomerGui g) {
		guis.add(g);
	}

	public static void addGui(HostGui gui) {
		guis.add(gui);
	}

	public void addGui(CookGui gui) {
		guis.add(gui);
	}
}
