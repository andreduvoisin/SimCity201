package restaurant.restaurant_davidmca.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import restaurant.restaurant_davidmca.Menu;
import restaurant.restaurant_davidmca.Stock;
import restaurant.restaurant_davidmca.interfaces.Cashier;
import restaurant.restaurant_davidmca.interfaces.Cook;
import restaurant.restaurant_davidmca.interfaces.Market;
import restaurant.restaurant_davidmca.roles.CashierRole;
import base.Agent;

/**
 * Restaurant customer restaurant_davidmca.agent.
 */
public class MarketAgent extends Agent implements Market {
	String[] foodChoices = { "Steak", "Chicken", "Salad", "Pizza" };
	List<Stock> inventory = Collections
			.synchronizedList(new ArrayList<Stock>());
	List<MyOrder> pendingOrders = Collections
			.synchronizedList(new ArrayList<MyOrder>());
	Cashier cashier;
	double totalRevenue;

	private Semaphore processingOrder = new Semaphore(1, true);

	private class MyOrder {
		Cook cook;
		List<Stock> stock;

		public MyOrder(Cook c) {
			this.cook = c;
			stock = Collections.synchronizedList(new ArrayList<Stock>());
		}
	}

	// Map for cook times

	private String name;

	Timer timer = new Timer();

	// restaurant_davidmca.agent correspondents
	/**
	 * Constructor for CookAgent class
	 * 
	 * @param name
	 *            name of the customer
	 */
	public MarketAgent(String name, int qty) {
		super();
		this.name = name;
		for (int i = 0; i < 4; i++) {
			inventory.add(new Stock(foodChoices[i], qty));
		}
		totalRevenue = 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setCashier(CashierRole cash) {
		this.cashier = cash;
	}

	// Messages

	@Override
	public void msgWantToBuy(Cook c, Map<String, Integer> stuffToBuy) {
		MyOrder newOrder = new MyOrder(c);
		synchronized (stuffToBuy) {
			for (Map.Entry<String, Integer> foodToBuy : stuffToBuy.entrySet()) {
				newOrder.stock.add(new Stock(foodToBuy.getKey(), foodToBuy
						.getValue()));
			}
		}
		pendingOrders.add(newOrder);
		stateChanged();
	}

	@Override
	public void msgPayInvoice(double total) {
		totalRevenue += total;
		print("Invoice payment recieved from Cashier, total revenue is now: "
				+ totalRevenue);
		stateChanged();
	}

	// Scheduler

	protected boolean pickAndExecuteAnAction() {
		if (!pendingOrders.isEmpty()) {
			processOrder(pendingOrders.get(0));
			return true;
		}
		return false;
	}

	// Actions

	private void processOrder(MyOrder thisOrder) {
		try {
			processingOrder.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				processingOrder.release();
			}
		}, 5000);
		try {
			processingOrder.tryAcquire(6000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		double total = 0;
		Menu menu = new Menu();
		synchronized (inventory) {
			for (Stock i : inventory) {
				synchronized (thisOrder.stock) {
					for (Stock o : thisOrder.stock) {
						if (o.getQuantity() <= i.getQuantity()) {
							i.setQuantity(i.getQuantity() - o.getQuantity());
							total += o.getQuantity()
									* menu.getPrice(o.getChoice());
						} else {
							o.setQuantity(0);
						}
					}
				}
			}
		}
		if (total > 0) {
			cashier.msgHereIsInvoice(this, total);
		}
		thisOrder.cook.msgOrderFullFillment(this, thisOrder.stock);
		pendingOrders.remove(thisOrder);
	}

}
