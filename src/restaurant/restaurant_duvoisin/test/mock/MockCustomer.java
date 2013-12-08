package restaurant.restaurant_duvoisin.test.mock;

import restaurant.restaurant_duvoisin.Menu;
import restaurant.restaurant_duvoisin.interfaces.Cashier;
import restaurant.restaurant_duvoisin.interfaces.Customer;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import restaurant.restaurant_duvoisin.roles.AndreSharedWaiterRole;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {
	public Cashier cashier;
	public EventLog log = new EventLog();
	public AndreSharedWaiterRole waiter;

	public MockCustomer(String name) {
		super(name);
	}

	@Override
	public void msgGotHungry() {
		
	}

	@Override
	public void msgFollowMeToTable(Waiter w, Menu m, int t) {
		
	}

	@Override
	public void msgAtSeat() {
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		
	}

	@Override
	public void msgOutOfChoice(Menu m) {
		
	}

	@Override
	public void msgHereIsYourFood(String myChoice) {
		
	}

	@Override
	public void msgHereIsChange(double change) {
		log.add(new LoggedEvent("msgHereIsChange received. Change = " + change));
	}

	@Override
	public void msgDoneLeaving() {
		
	}

	@Override
	public void msgRestaurantFull() {
		
	}

	@Override
	public void msgHereIsCheck(double amount) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	/*
	public Cashier cashier;

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void HereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
	}

	@Override
	public void HereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}
	*/
}
