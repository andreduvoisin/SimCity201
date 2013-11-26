package restaurant.restaurant_davidmca.test;

import java.util.HashMap;
import java.util.Map;

import base.PersonAgent;
import junit.framework.TestCase;
import restaurant.restaurant_davidmca.Check;
import restaurant.restaurant_davidmca.roles.DavidCashierRole;
import restaurant.restaurant_davidmca.test.mock.MockCustomer;
import restaurant.restaurant_davidmca.test.mock.MockMarket;
import restaurant.restaurant_davidmca.test.mock.MockWaiter;

public class CashierTest extends TestCase {
	DavidCashierRole cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockCustomer customer2;
	MockCustomer brokecustomer;
	MockMarket market1;
	MockMarket market2;

	public void setUp() throws Exception {
		super.setUp();
		waiter = new MockWaiter("mockwaiter");
		market1 = new MockMarket("Fresh 'n Easy");
		market2 = new MockMarket("Superior");
		PersonAgent testPerson = new PersonAgent();
		cashier = new DavidCashierRole("testCashier");
		cashier.setPerson(testPerson);
		cashier.totalCash = 10000;
		customer = new MockCustomer("mockcustomer");
		customer2 = new MockCustomer("mockcustomer2");
		customer.setCashier(cashier);
		customer2.setCashier(cashier);
	}

	public void testSingleMarketOrder() {
		market1.setCashier(cashier);
		assertEquals("Start off ensuring cashier has no invoices to pay",
				cashier.invoicesToPay.size(), 0);
		double startingCash = cashier.totalCash;
		assertEquals("Market has not gotten any payments",
				market1.totalRevenue, 0.0);
		// Create sample list of foods to buy
		Map<String, Integer> stuffToBuy = new HashMap<String, Integer>();
		stuffToBuy.put("Pizza", 5);
		stuffToBuy.put("Steak", 3);
		// Sent a request to our Mock Waiter. Mock waiter will process and send
		// an invoice to cashier
		market1.msgWantToBuy(null, stuffToBuy);
		assertEquals("Cashier should now have one pending invoice",
				cashier.invoicesToPay.size(), 1);
		double orderTotal = market1.orderTotal;
		assertEquals("Make sure the invoice total is correct",
				cashier.invoicesToPay.get(0).total, orderTotal);
		cashier.pickAndExecuteAnAction();
		// Wait a bit for the cashier thread to start and process the invoice
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(
				"Market should have gotten a payment and should now have proper revenue",
				market1.totalRevenue, orderTotal);
		assertEquals("Check if cashier cash reserve is accurate",
				cashier.totalCash, startingCash - orderTotal);
	}

	public void testMarketOrderMultipleFullfillment() {
		market1.setCashier(cashier);
		market2.setCashier(cashier);
		assertEquals("Start off ensuring cashier has no invoices to pay",
				cashier.invoicesToPay.size(), 0);
		double startingCash = cashier.totalCash;
		assertEquals("Market has not gotten any payments",
				market1.totalRevenue, 0.0);
		// Create sample list of foods to buy
		Map<String, Integer> firstList = new HashMap<String, Integer>();
		firstList.put("Pizza", 5);
		firstList.put("Steak", 3);
		// Sent a request to our Mock Waiter. Mock waiter will process and send
		// an invoice to cashier
		market1.msgWantToBuy(null, firstList);
		// Create second list of foods to buy. This mirrors logic in the Cook
		// such that is one market is unable to fulfill all or part of an order,
		// another market will be asked.
		Map<String, Integer> secondList = new HashMap<String, Integer>();
		secondList.put("Steak", 4);
		secondList.put("Salad", 6);
		market2.msgWantToBuy(null, secondList);
		assertEquals("Cashier should now have two pending invoices",
				cashier.invoicesToPay.size(), 2);
		double firstOrderTotal = market1.orderTotal;
		double secondOrderTotal = market2.orderTotal;
		assertEquals("Make sure the first invoice total is correct",
				cashier.invoicesToPay.get(0).total, firstOrderTotal);
		assertEquals("Make sure the second invoice total is correct",
				cashier.invoicesToPay.get(1).total, secondOrderTotal);
		cashier.pickAndExecuteAnAction();
		assertEquals(
				"First market should have gotten a payment and should now have proper revenue",
				market1.totalRevenue, firstOrderTotal);
		cashier.pickAndExecuteAnAction();
		assertEquals(
				"Second market should have gotten a payment and should now have proper revenue",
				market2.totalRevenue, secondOrderTotal);
		assertEquals("Check if cashier cash reserve is accurate",
				cashier.totalCash, startingCash - firstOrderTotal
						- secondOrderTotal);
	}

	public void testCustomerPayment() {
		assertEquals("Start off ensuring cashier has no checks to compute",
				cashier.pendingChecks.size(), 0);
		double startingCash = cashier.totalCash;
		cashier.msgComputeBill(waiter, customer, "Pizza");
		assertEquals("Cashier should now have one check to compute",
				cashier.pendingChecks.size(), 1);
		Check newCheck = new Check(waiter, customer, "Pizza");
		customer.msgHereIsCheck(newCheck);
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer should have received msgHereIsCheck"
				+ customer.log.toString(),
				customer.log.containsString("Received msgHereIsCheck"));
		assertEquals("Ensure paidChecks has a check",
				cashier.paidChecks.size(), 1);
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer should have received msgChange"
				+ customer.log.toString(),
				customer.log.containsString("Received msgChange"));
		assertEquals("Check that cashier cash has been increased correctly",
				cashier.totalCash, startingCash + newCheck.total);
	}

	public void testMultipleCustomerPayment() {
		assertEquals("Start off ensuring cashier has no checks to compute",
				cashier.pendingChecks.size(), 0);
		double startingCash = cashier.totalCash;
		cashier.msgComputeBill(waiter, customer, "Pizza");
		cashier.msgComputeBill(waiter, customer2, "Steak");
		assertEquals("Cashier should now have two checks to compute",
				cashier.pendingChecks.size(), 2);
		Check newCheck = new Check(waiter, customer, "Pizza");
		Check newCheck2 = new Check(waiter, customer2, "Steak");
		customer.msgHereIsCheck(newCheck);
		customer2.msgHereIsCheck(newCheck2);
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer should have received msgHereIsCheck"
				+ customer.log.toString(),
				customer.log.containsString("Received msgHereIsCheck"));
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer 2 should have received msgHereIsCheck"
				+ customer2.log.toString(),
				customer2.log.containsString("Received msgHereIsCheck"));
		assertEquals("Ensure paidChecks has two checks",
				cashier.paidChecks.size(), 2);
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer should have received msgChange"
				+ customer.log.toString(),
				customer.log.containsString("Received msgChange"));
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer 2 should have received msgChange"
				+ customer2.log.toString(),
				customer2.log.containsString("Received msgChange"));
		assertEquals("Check that cashier cash has been increased correctly",
				cashier.totalCash, startingCash + newCheck.total
						+ newCheck2.total);
	}

	public void testMultipleRepeatCustomers() {
		// Continue using same customers from previous tests, as if they were
		// "marked hungry" again.
		// Clear logs so we can accurately check message receipt
		customer.log.clear();
		customer2.log.clear();
		assertEquals("Start off ensuring cashier has no checks to compute",
				cashier.pendingChecks.size(), 0);
		double startingCash = cashier.totalCash;
		cashier.msgComputeBill(waiter, customer, "Salad");
		cashier.msgComputeBill(waiter, customer2, "Chicken");
		assertEquals("Cashier should now have two checks to compute",
				cashier.pendingChecks.size(), 2);
		Check newCheck = new Check(waiter, customer, "Salad");
		Check newCheck2 = new Check(waiter, customer2, "Chicken");
		customer.msgHereIsCheck(newCheck);
		customer2.msgHereIsCheck(newCheck2);
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer should have received msgHereIsCheck"
				+ customer.log.toString(),
				customer.log.containsString("Received msgHereIsCheck"));
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer 2 should have received msgHereIsCheck"
				+ customer2.log.toString(),
				customer2.log.containsString("Received msgHereIsCheck"));
		assertEquals("Ensure paidChecks has two checks",
				cashier.paidChecks.size(), 2);
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer should have received msgChange"
				+ customer.log.toString(),
				customer.log.containsString("Received msgChange"));
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer 2 should have received msgChange"
				+ customer2.log.toString(),
				customer2.log.containsString("Received msgChange"));
		assertEquals("Check that cashier cash has been increased correctly",
				cashier.totalCash, startingCash + newCheck.total
						+ newCheck2.total);
	}

	public void testNotEnoughMoneyDebtPayment() {
		cashier.log.clear();
		brokecustomer = new MockCustomer("brokecustomer");
		brokecustomer.setCashier(cashier);
		assertEquals("Start off ensuring cashier has no checks to compute",
				cashier.pendingChecks.size(), 0);
		double startingCash = cashier.totalCash;
		cashier.msgComputeBill(waiter, brokecustomer, "Pizza");
		assertEquals("Cashier should now have one check to compute",
				cashier.pendingChecks.size(), 1);
		Check newCheck = new Check(waiter, brokecustomer, "Pizza");
		brokecustomer.msgHereIsCheck(newCheck);
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer should have received msgHereIsCheck"
				+ brokecustomer.log.toString(),
				brokecustomer.log.containsString("Received msgHereIsCheck"));
		assertEquals("Ensure paidChecks has a check",
				cashier.paidChecks.size(), 1);
		cashier.pickAndExecuteAnAction();
		assertTrue("Mock customer should have received msgChange"
				+ brokecustomer.log.toString(),
				brokecustomer.log.containsString("Received msgChange"));
		assertNotSame("Cashier Cash should be lower than expected",
				cashier.totalCash, startingCash + newCheck.total);
		double currentCash = cashier.totalCash;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(
				"Cashier should record a debt payment" + cashier.log.toString(),
				cashier.log.containsString("msgDebtPayment occurred"));
		assertEquals("Cashier cash should now match", cashier.totalCash,
				currentCash + newCheck.total);
	}

}
