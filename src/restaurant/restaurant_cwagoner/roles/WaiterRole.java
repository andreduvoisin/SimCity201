package restaurant.restaurant_cwagoner.roles;

import base.Agent;
import restaurant.restaurant_cwagoner.gui.WaiterGui;
import restaurant.restaurant_cwagoner.interfaces.*;
import restaurant.restaurant_cwagoner.roles.CookRole.Order;

import java.util.*;
import java.util.concurrent.*;

public class WaiterRole extends Agent implements Waiter {

	private static boolean DirectCookAccess = false;
	private final boolean accessCook;

	public WaiterRole(String waiterName) {
		name = waiterName;
		// Initialize menu
		menu.put("Steak", 8);
		menu.put("Chicken", 6);
		menu.put("Salad", 2);
		menu.put("Pizza", 4);
		
		accessCook = DirectCookAccess;
		DirectCookAccess = !(DirectCookAccess);
	}
	
	
	// DATA

	private String name;
	Host host;
	Cook cook;
	Cashier cashier;
	
	private HashMap<String, Integer> menu = new HashMap<String, Integer>();
	
	private List<AssignedCustomer> Customers =
			Collections.synchronizedList(new ArrayList<AssignedCustomer>());
	
	// Pauses agent's thread while GUI is doing an animation
	private Semaphore animationFinished = new Semaphore(0, true);
	public WaiterGui gui = null;
	private enum State { working, askForBreak, asked, onBreak }
	State state = State.working;
	Timer breakTimer = new Timer();
	
	
	// MESSAGES
	
	// From GUI
	public void msgAnimationFinished() {
		animationFinished.release();
	}
	
	// From animation when "Ask" (for break) button clicked
	public void msgGuiAskedForBreak() {		
		state = State.askForBreak;
		stateChanged();
	}
	
	// From host
	public void msgGoOnBreak(boolean allowed) {
		
		if (allowed) {
			state = State.onBreak;
			print("Received msgGoOnBreak(allowed)");
		}
		else {
			state = State.working;
			print("Received msgGoOnBreak(not allowed)");
		}
		
		stateChanged();
	}
	
	// From host
	public void msgSeatCustomer(Customer c, int table) {
		print("Received msgSeatCustomer(" + c.getName() + ", table " + table + ")");
		
		Customers.add(new AssignedCustomer(c, table));
				
		stateChanged();
	}
	
	// From customer
	public void msgReadyToOrder(Customer c) {
		print("Received msgReadyToOrder(" + c.getName() + ")");
		
		synchronized(Customers) {
			for (AssignedCustomer cust : Customers) {
				if (cust.customer.equals(c)) {
					cust.state = AssignedCustomer.State.readyToOrder;
					
					break;
				}
			}
		}

		stateChanged();
	}
	
	// From customer
	public void msgHeresMyOrder(Customer c, String choice) {
		print("Received msgHeresMyOrder(" + c.getName() + ", " + choice + ")");
		
		for (AssignedCustomer cust : Customers) {
			if (cust.customer.equals(c)) {
				cust.state = AssignedCustomer.State.ordered;
				cust.food = choice;
				animationFinished.release();
				break;
			}
		}

		stateChanged();
	}
	
	// From cook
	public void msgOrderReady(int table) {
		print("Received msgOrderReady(table " + table + ")");
		
		synchronized(Customers) {
			for (AssignedCustomer cust : Customers) {
				if (cust.tableNum == table) {				
					cust.state = AssignedCustomer.State.foodReady;
					
					break;
				}
			}
		}

		stateChanged();
	}
	
	// From cook
	public void msgOutOfFood(int table) {
		print("Received msgOutOfFood(table " + table + ")");
		
		synchronized(Customers) {
			for (AssignedCustomer c : Customers) {
				if (c.tableNum == table) {
					c.state = AssignedCustomer.State.orderDifferent;
					break;
				}
			}
		}

		stateChanged();
	}
	
	// From customer
	public void msgLeavingTable(Customer c) {
		print("Received msgLeavingTable(" + c.getName() + ")");
		
		synchronized(Customers) {
			for (AssignedCustomer ac : Customers) {
				if (ac.customer.equals(c)) {
					ac.state = AssignedCustomer.State.waitingForCheck;
					break;
				}
			}
		}

		stateChanged();
	}

	
	// SCHEDULER

	protected boolean pickAndExecuteAnAction() {
		
		if (state.equals(State.askForBreak)) {
			AskForBreak();
			return true;
		}

		synchronized(Customers) {
			// Tell cashier to prepare check, and tell host table empty
			for (AssignedCustomer c : Customers) {
				if (c.state.equals(AssignedCustomer.State.waitingForCheck)) {
					CustomerLeaving(c);
					return true;
				}
			}
		}

		synchronized(Customers) {
			// Take order from any customer ready to order
			for (AssignedCustomer c : Customers) {
				if (c.state.equals(AssignedCustomer.State.readyToOrder)) {
					TakeOrder(c);
					return true;
				}
			}
		}

		synchronized(Customers) {
			// Deliver all taken orders to the cook
			for (AssignedCustomer c : Customers) {
				if (c.state.equals(AssignedCustomer.State.ordered)) {
					DeliverOrderToCook(c);
					return true;
				}
			}
		}

		synchronized(Customers) {
			// If cook is out of ordered food
			for (AssignedCustomer c : Customers) {
				if (c.state.equals(AssignedCustomer.State.orderDifferent)) {
					TellToOrderAgain(c);
					return true;
				}
			}
		}

		synchronized(Customers) {
			// Deliver any prepared food to the customer who ordered it
			for (AssignedCustomer c : Customers) {
				if (c.state.equals(AssignedCustomer.State.foodReady)) {
					GetFood(c);
					return true;
				}
			}
		}

		synchronized(Customers) {
			// Seat any customer in waiting area
			for (AssignedCustomer c : Customers) {
				if (c.state.equals(AssignedCustomer.State.waitingToBeSeated)) {
					Seat(c);
					return true;
				}
			}
		}
		
		// If break has been allowed, take it once all tasks are completed
		if (state.equals(State.onBreak)) {
			TakeBreak();
		}
		else {
			gui.onBreak(false);
		}
		
		return false;
	}

	
	// ACTIONS

	
	private void CustomerLeaving(AssignedCustomer c) {
		print("DeliverOrderToCashier(" + c.customer.getName() + ")");
		
		gui.DoGoToCashier();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		cashier.msgCustomerOrdered(this, c.customer, c.food);
		
		gui.DoGoToTable(c.tableNum);
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		c.customer.msgAcknowledgeLeaving();
		host.msgCustomerGoneTableEmpty(c.customer, c.tableNum);
		
		Customers.remove(c);
		gui.DoGoToHomePosition();
		stateChanged();
	}
	
	private void Seat(AssignedCustomer c) {
		print("Seat(" + c.customer.getName() + ")");
		
		gui.DoGoGetCustomer(c.customer.getPosition());
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		c.customer.msgSitAtTable(this, c.tableNum, menu);
		
		gui.DoGoToTable(c.tableNum);
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		c.state = AssignedCustomer.State.readingMenu;
		
		gui.DoGoToHomePosition();
		
		stateChanged();
	}
	
	private void GetFood(AssignedCustomer c) {
		print("GetFood(" + c.customer.getName() + ")");
		
		gui.DoGoToCook();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		gui.DoDrawFood(c.food.substring(0, 2));
		
		gui.DoDeliverFood(c.tableNum);
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		c.customer.msgHeresYourFood();
		c.state = AssignedCustomer.State.eating;
		
		gui.DoClearFood();
		
		gui.DoGoToHomePosition();
		
		stateChanged();
	}
	
	private void TakeOrder(AssignedCustomer c) {
		print("TakeOrder(" + c.customer.getName() + ")");
		
		gui.DoGoToTable(c.tableNum);
		try { animationFinished.acquire(); } catch (InterruptedException e) {}

		c.customer.msgWhatDoYouWant();
		
		// Not real "animation": just staying at table while customer orders
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		// Don't need stateChanged() because it's called in msgHeresMyOrder
	}
	
	private void DeliverOrderToCook(AssignedCustomer c) {
		print("DeliverOrderToCook(" + c.customer.getName() + ")");

		gui.DoGoToCook();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}

		if (accessCook) {
			print("Adding order to cook's revolving stand");
			Cook.Orders.add(new Order(this, c.tableNum, c.food));
		}
		else {
			print("Messaging cook with order");
			cook.msgHeresAnOrder(this, c.tableNum, c.food);
		}

		c.state = AssignedCustomer.State.orderDeliveredToCook;

		gui.DoGoToHomePosition();

		stateChanged();
	}

	private void TellToOrderAgain(AssignedCustomer c) {
		print("TellToOrderAgain(" + c.customer.getName() + ")");
		
		gui.DoGoToTable(c.tableNum);
		
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		// Create new menu to give to this customer
		HashMap<String, Integer> newMenu = new HashMap<String, Integer>();
		newMenu = menu;
		newMenu.remove(c.food);
		
		// At table now; tell customer to look over menu again
		c.customer.msgPickSomethingElse(newMenu);

		// New state seems weird, but look at scheduler. Avoids an extra rule
		c.state = AssignedCustomer.State.readingMenu;
		
		gui.DoGoToHomePosition();
		
		stateChanged();
	}
	
	private void AskForBreak() {
		print("AskForBreak()");
		
		host.msgCanIGoOnBreak(this);
		state = State.asked;
		
		stateChanged();
	}
	
	private void TakeBreak() {
		print("TakeBreak()");
		
		gui.onBreak(true);
		
		breakTimer.schedule(new TimerTask() {
			public void run() {
				state = State.working;
				GoOffBreak();

				stateChanged();
			}
		}, 10000);
	}
	
	// Necessary because host.msgOffBreak can't be put in run() above
	private void GoOffBreak() {
		print("GoOffBreak()");
		host.msgOffBreak(this);
		gui.onBreak(false);
	}
	
	// ACCESSORS
	
	public String getName() {
		return name;
	}
	
	public void setGui(WaiterGui waiterGui) {
		gui = waiterGui;
	}

	public void setHost(Host h) {
		host = h;
	}

	public void setCook(Cook c) {
		cook = c;
	}
	
	public void setCashier(Cashier c) {
		cashier = c;
	}

	// For host to determine which waiter has fewest customers
	public int numCustomers() {
		return Customers.size();
	}
	
	// Gets i-th customer's name
	public String getCustomerName(int i) {
		return Customers.get(i).customer.getName();
	}


	// CLASSES
	
	private static class AssignedCustomer {
		Customer customer;
		int tableNum;
		public enum State { waitingToBeSeated, readingMenu, readyToOrder, ordered,
							orderDeliveredToCook, orderDifferent, foodReady,
							eating, waitingForCheck }
		private State state;
		private String food;
		
		AssignedCustomer(Customer c, int table) {
			customer = c;
			tableNum = table;
			state = State.waitingToBeSeated;
			food = "";
		}	
	}
}
	