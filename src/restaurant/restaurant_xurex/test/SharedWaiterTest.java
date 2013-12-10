package restaurant.restaurant_xurex.test;

import junit.framework.TestCase;
import restaurant.restaurant_xurex.agents.WaiterAgent2;
import restaurant.restaurant_xurex.test.mock.MockCashier;
import restaurant.restaurant_xurex.test.mock.MockCook;
import restaurant.restaurant_xurex.test.mock.MockCustomer;
import restaurant.restaurant_xurex.test.mock.MockHost;
import restaurant.restaurant_xurex.test.mock.MockWaiterGui;
import restaurant.restaurant_xurex.utilities.CustomerState;
import restaurant.restaurant_xurex.utilities.OrderState;
import restaurant.restaurant_xurex.utilities.WaiterState;

/**
 * 
 * This class is a JUnit test class to unit test the WaiterAgent's basic interaction
 * with cashiers, customers, and a market.
 *
 *
 * @author Rex Xu
 */
public class SharedWaiterTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	WaiterAgent2 waiter;
	
	MockHost host;
	MockCook cook;
	MockCashier cashier;
	MockCustomer customer1;
	MockCustomer customer2;
	
	MockWaiterGui waiterGui;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		waiter = new WaiterAgent2("waiter");
		host = new MockHost("host");
		cook = new MockCook("cook");
		cashier = new MockCashier("cashier");
		customer1 = new MockCustomer("customer1");
		customer2 = new MockCustomer("customer2");
		waiterGui = new MockWaiterGui("waiterGui");
		
		waiter.setHost(host);
		waiter.setCook(cook);
		waiter.setCashier(cashier);
		waiter.setGui(waiterGui);
		
		//Prevents Test Stall
		for(int i = 0; i<99; i++){
			waiter.releaseSem();
		}
	}	
	
	public void testOne_OneCustomer(){
	//  setUp()
		
	//	Preconditions
		assertTrue("Waiter has a gui", waiter.getGui() != null);
		assertTrue("Waiter gui is correct", waiter.getGui() == waiterGui);
		assertTrue("Waiter has no customers", waiter.customers.isEmpty());
		assertTrue("Waiter has no ordeers", waiter.orders.isEmpty());
		assertTrue("Waiter state is good", waiter.state == WaiterState.good);
		assertTrue("Host log is empty", host.log.size()==0);
		assertTrue("Cook log is empty", cook.log.size()==0);
		assertTrue("Cashier log is empty", cashier.log.size()==0);
		assertTrue("Waiter gui log is empty", waiterGui.log.size()==0);

	//	1: add one customer
		waiter.PleaseSeatCustomer(customer1, 2);
		
	//	Check
		assertTrue("Waiter has one customer", waiter.customers.size() == 1);
		assertTrue("Waiter has proper customer", waiter.customers.get(0).c == customer1);
		assertTrue("Customer state is waiting", waiter.customers.get(0).s == CustomerState.waiting);
		assertTrue("Waiter state is working", waiter.state == WaiterState.working);
		
	//	2: p.a.e.a. (SeatCustomer(c))
		assertTrue("p.a.e.a. seatCustomer(c)", waiter.pickAndExecuteAnAction()); 
		
	//	Check
		assertTrue("Customer 1 received FollowMe", customer1.log.containsString("FollowMe: 2"));
		assertTrue("Waiter Gui received DoGoToTable. Instead: "+
					waiterGui.log.getLastLoggedEvent().toString(), waiterGui.log.containsString("DoGoToTable: 2"));
		assertTrue("Customer 1 state is ignore", waiter.customers.get(0).s == CustomerState.ignore);
		
	//	3: msgReadyToOrder
		waiter.ReadyToOrder(customer1);
		
	//	Check
		assertTrue("Customer 1 state is readytoOrder", waiter.customers.get(0).s == CustomerState.readyToOrder);
		
	//	4: p.a.e.a. (TakeOrder(customer))
		assertTrue("p.a.e.a. TakeOrder", waiter.pickAndExecuteAnAction());
		
	//	Check
		assertTrue("Customer 1 state is askedToOrder", waiter.customers.get(0).s == CustomerState.askedToOrder);
		assertTrue("Customer 1 received WhatWouldYouLike", customer1.log.containsString("WhatWouldYouLike"));
		
	//	5: msgHereIsChoice(customer, choice)
		waiter.HereIsChoice(customer1, "Salad");
		
	//	Check
		assertTrue("Customer 1 state is ordered", waiter.customers.get(0).s == CustomerState.ordered);
		assertTrue("Customer 1 choice is Salad", waiter.customers.get(0).choice.equals("Salad"));
		
	// 6: p.a.e.a. (sendOrder(customer))
		assertTrue("p.a.e.a. sendOrder", waiter.pickAndExecuteAnAction());
		
	//	Check
		assertTrue("Customer 1 state is ignore", waiter.customers.get(0).s == CustomerState.ignore);
		assertTrue("Waiter now has an order", waiter.orders.size() == 1);
		assertTrue("Waiter order has choice of salad", waiter.orders.get(0).choice.equals("Salad"));
		assertTrue("Waiter order has table of 2", waiter.orders.get(0).table == 2);
		assertTrue("WaiterGui received DoDisplayOrder", waiterGui.log.containsString("DoDisplayOrder: Salad 2"));
		assertTrue("WaiterGui received DoGoToTable", waiterGui.log.containsString("DoGoToTable: 5"));
		assertTrue("Cook received addToStand", cook.log.containsString("addToStand: Salad 2"));
		//getbill called in sendOrder
		assertTrue("WaiterGui received DoGoToTable", waiterGui.log.containsString("DoGoToTable: 11"));
		assertTrue("Cashier received ComputeBill", cashier.log.containsString("ComputeBill: customer1"));
		
	//	7: msgHereIsBill(customer, bill)
		waiter.HereIsBill(customer1, 10);
		
	//	Check
		assertTrue("Customer 1 has bill of 10", waiter.customers.get(0).bill == 10);
		
	//	8: msgOrderIsReady (choice, table, kitchen)
		waiter.OrderIsReady("Salad", 2, 6);
		
	//	Check
		assertTrue("Order has kitchen of 6", waiter.orders.get(0).kitchen == 6);
		assertTrue("Order state is readyToServe", waiter.orders.get(0).s == OrderState.readyToServe);
		
	//	9: p.a.e.a. (serveFood(order))
		assertTrue("paea: serveFood", waiter.pickAndExecuteAnAction());
		
	//	Check
		assertTrue("Order state is served", waiter.orders.get(0).s == OrderState.served);
		assertTrue("WaiterGui received DoGoToTable", waiterGui.log.containsString("DoGoToTable: 6"));
		assertTrue("Cook received PickedUp", cook.log.containsString("PickedUp: 6"));
		assertTrue("Customer received HereIsFoodAndBill", customer1.log.containsString("HereIsFoodAndBill: 10"));
		assertTrue("Customer state is ignore", waiter.customers.get(0).s == CustomerState.ignore);
		assertTrue("WaiterGui received DoServeFood", waiterGui.log.containsString("DoServeFood: Salad"));
		
	//	10: msgLeaving (customer)
		waiter.Leaving(customer1);
		
	//	Check
		assertTrue("Customer state is done", waiter.customers.get(0).s == CustomerState.done);
		
	//	11: p.a.e.a. (cleanTable())
		assertTrue("paea: cleanTable()", waiter.pickAndExecuteAnAction());
	
	//	Check
		assertTrue("WaiterGui received DoCleanFood", waiterGui.log.containsString("DoCleanFood"));
		assertTrue("Host received TableIsFree", host.log.containsString("TableIsFree: 2"));
		assertTrue("Waiter has no customers now", waiter.customers.isEmpty());
		
	//	12: p.a.e.a. (IAmFree())
		assertTrue("paea: IAmFree", waiter.pickAndExecuteAnAction());
		
	//	Check
		assertTrue("Waiter state is now good", waiter.state == WaiterState.good);
		assertTrue("Host received IAmFree", host.log.containsString("IAmFree"));
	}
	
}
