package restaurant.restaurant_cwagoner.roles;

import base.Agent;
import restaurant.restaurant_cwagoner.interfaces.*;
import restaurant.restaurant_cwagoner.test.mock.*;

import java.util.*;

/**
 * Restaurant cashier agent.
 */
public class CwagonerCashierRole extends Agent implements CwagonerCashier {
	
	public CwagonerCashierRole() {
		PriceList.put("Steak",		8.0);
		PriceList.put("Chicken",	6.0);
		PriceList.put("Salad",		2.0);
		PriceList.put("Pizza",		4.0);
	}
	
	CwagonerCook cwagonerCook;
	
	public EventLog log = new EventLog();
	
	// DATA

	// Cashier starts with $15
	public double money = 15.0;
	HashMap<String, Double> PriceList = new HashMap<String, Double>();
	public List<Bill> Bills =
			Collections.synchronizedList(new ArrayList<Bill>());
	public HashMap<CwagonerMarket, Double> MarketInteractions = new HashMap<CwagonerMarket, Double>();
	// Values: 0 - don't order again; otherwise - pay that amount
	
	
	// MESSAGES

	// From waiter - telling cashier to prepare bill for leaving customer
	public void msgCustomerOrdered(CwagonerWaiter w, CwagonerCustomer c, String food) {
		log.add(new LoggedEvent("Received msgCustomerOrdered(" + w.getName() + ", " + c.getName() + ", " + food + ")"));

		Bills.add(new Bill(w, c, PriceList.get(food)));
		stateChanged();
	}
	
	// From customer at cashier's desk
	public void msgReadyToPay(CwagonerCustomer c) {
		print("Received msgReadyToPay(" + c.getName() + ")");
		log.add(new LoggedEvent("Received msgReadyToPay(" + c.getName() + ")"));
		
		// makes hang synchronized(Bills) {
			for (Bill b : Bills) {
				if (c.equals(b.customer)) {
					b.state = Bill.State.customerApproached;
					break;
				}
			}
		//}

		stateChanged();
	}
	
	// From customer
	public void msgPayment(CwagonerCustomer c, double cashTendered) {
		log.add(new LoggedEvent("Received msgPayment(" + c.getName() + ", $" + cashTendered + ")"));
		print("Received msgPayment(" + c.getName() + ", $" + cashTendered + ")");
		
		// Makes hang synchronized(Bills) {
			for (Bill b : Bills) {
				if (b.customer.equals(c)) {
					money += cashTendered;
					
					// Non-normative: underpaid
					if (cashTendered < b.netCost) {
						b.netCost -= cashTendered;
						b.state = Bill.State.underpaid;
					}
					// Non-normative: overpaid
					else if (cashTendered > b.netCost) {
						b.state = Bill.State.overpaid;
						b.changeDue = cashTendered - b.netCost;
					}
					// Normative: pays exact amount
					else {
						b.state = Bill.State.paid;
					}
					break;
				}
			}
		//}

		stateChanged();
	}
	
	// From market - pay for delivered order
	public void msgPayForOrder(CwagonerMarket m, double total) {
		log.add(new LoggedEvent("Received msgPayForOrder, total = " + total));
		print("Received msgPayForOrder(" + m.getName() + ", $" + total + ")");
		
		MarketInteractions.put(m, total);
		stateChanged();
	}
	
	// From market - couldn't pay
	public void msgDontOrderAgain(CwagonerMarket m) {
		log.add(new LoggedEvent("Received msgDontOrderAgain"));
		print("Received msgDontOrderAgain(" + m.getName() + ")");
		
		MarketInteractions.put(m, 0.0);
		stateChanged();
	}
	
	// SCHEDULER
	
	public boolean pickAndExecuteAnAction() {

		// If market is owed money
		if (MarketInteractions.size() > 0) {
			HandleMarkets();
			return true;
		}
		
		// If customer underpaid
		synchronized(Bills) {
			for (Bill b : Bills) {
				if (b.state.equals(Bill.State.underpaid)) {
					TellCustomerTheyOwe(b);
					return true;
				}
			}
		}
		
		// If customer overpaid
		synchronized(Bills) {
			for (Bill b : Bills) {
				if (b.state.equals(Bill.State.overpaid)) {
					GiveChange(b);
					return true;
				}
			}
		}
		
		// If customer paid exact change
		synchronized(Bills) {
			for (Bill b : Bills) {
				if (b.state.equals(Bill.State.paid)) {
					ThankForComing(b);
					return true;
				}
			}
		}
		
		// Give customers their total
		synchronized(Bills) {
			for (Bill b : Bills) {
				if (b.state.equals(Bill.State.customerApproached)) {
					GiveBill(b);
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	// ACTIONS
	
	private void HandleMarkets() {
		print("HandleMarkets()");
		
		for (CwagonerMarket m : MarketInteractions.keySet()) {
			double total = MarketInteractions.get(m);
			if (total == 0.0) {
				cwagonerCook.msgDontOrderFrom(m);
			}
			else {
				if (money >= total) {
					m.msgPayment(this, total);
					money -= total;
				}
				else {
					m.msgPayment(this, money);
					money = 0;
				}
			}
		}
		
		MarketInteractions.clear();
		stateChanged();
	}
	
	private void TellCustomerTheyOwe(Bill b) {
		b.customer.msgYouOwe(b.netCost);
		Bills.remove(b);
		stateChanged();
	}

	private void GiveChange(Bill b) {
		b.customer.msgHeresYourChange(b.changeDue);
		Bills.remove(b);
		stateChanged();
	}
	
	private void ThankForComing(Bill b) {
		b.customer.msgThankYou();
		Bills.remove(b);
		stateChanged();
	}

	private void GiveBill(Bill b) {
		b.customer.msgYourTotalIs(b.netCost);
		b.state = Bill.State.awaitingPayment;
		// NO stateChanged() - cashier only helps this customer until they're done
	}
	
	
	// Accessors
	
	public String getName() {
		return "Cashier";
	}
	
	// Classes
	
	public static class Bill {
		public CwagonerCustomer customer;
		public CwagonerWaiter waiter;
		
		public enum State { received, customerApproached, awaitingPayment,
							paid, overpaid, underpaid }
		public State state;
		public double changeDue;
		public double netCost;

		public Bill(CwagonerWaiter w, CwagonerCustomer c, double total) {
			waiter = w;
			customer = c;
			netCost = total;
			changeDue = 0.0;
			state = State.received;
		}
	}

	public void HereIsBill(Bill bill) {
		Bills.add(bill);
	}
	
	public void setCook(CwagonerCook c) {
		cwagonerCook = c;
	}
}
