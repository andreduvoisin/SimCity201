package restaurant.restaurant_cwagoner.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import restaurant.restaurant_cwagoner.interfaces.CwagonerCashier;
import restaurant.restaurant_cwagoner.interfaces.CwagonerCook;
import restaurant.restaurant_cwagoner.interfaces.CwagonerCustomer;
import restaurant.restaurant_cwagoner.interfaces.CwagonerMarket;
import restaurant.restaurant_cwagoner.interfaces.CwagonerWaiter;
import restaurant.restaurant_cwagoner.test.mock.EventLog;
import restaurant.restaurant_cwagoner.test.mock.LoggedEvent;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;

/**
 * Restaurant cashier agent.
 */
public class CwagonerCashierRole extends BaseRole implements CwagonerCashier {
	
	public CwagonerCashierRole(Person person) {
		super(person);

		PriceList.put("Steak",		8.0);
		PriceList.put("Chicken",	6.0);
		PriceList.put("Salad",		2.0);
		PriceList.put("Pizza",		4.0);
	}
	
	public CwagonerCashierRole() {
		super(null);
		// CHASE: placeholder cashier
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
	
	// SCHEDULER
	
	public boolean pickAndExecuteAnAction() {
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
		return "CwagonerCashier " + mPerson.getName();
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
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(1);
	}
}
