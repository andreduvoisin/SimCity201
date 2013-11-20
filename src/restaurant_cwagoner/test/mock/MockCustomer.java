package restaurant_cwagoner.test.mock;

import restaurant_cwagoner.interfaces.*;

import java.awt.Dimension;
import java.util.HashMap;


public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public EventLog log;

	public MockCustomer(String name) {
		super(name);
		log = new EventLog();
	}

	public void msgGuiGotHungry() {
		
	}

	public void msgRestaurantFull() {
		
	}

	public void msgSitAtTable(Waiter w, int table, HashMap<String, Integer> menuOptions) {
		
	}

	public void msgGuiAtSeat() {
		
	}

	public void msgWhatDoYouWant() {
		
	}

	public void msgPickSomethingElse(HashMap<String, Integer> newMenu) {
		
	}

	public void msgHeresYourFood() {
		
	}
	
	public void msgAcknowledgeLeaving() {
		
	}

	public void msgGuiAtCashier() {
		
	}

	public void msgYourTotalIs(double total) {
        log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

        // Non-normative: customer doesn't have enough money
        if (name.toLowerCase().contains("thief")) {
        	cashier.msgPayment(this, Math.floor(total - 1));

        }
        // Non-normative: customer overpays
        else if (name.toLowerCase().contains("rich")) {
    		cashier.msgPayment(this, Math.ceil(total + 1));

        }
        // Normative: pays correct amount
        else {
        	cashier.msgPayment(this, total);
        }
	}

	public void msgThankYou() {
		log.add(new LoggedEvent("Received msgThankYou from cashier"));
	}
	
	public void msgHeresYourChange(double changeDue) {
		log.add(new LoggedEvent("Received msgHeresYourChange from cashier. Change = "+ changeDue));
	}

	public void msgYouOwe(double remainingTotal) {
	        log.add(new LoggedEvent("Received msgYouOwe($" + remainingTotal + ")"));
	}

	public void msgGuiLeftRestaurant() {
		
	}

	@Override
	public Dimension getPosition() {
		return new Dimension(0, 0);
	}
}
