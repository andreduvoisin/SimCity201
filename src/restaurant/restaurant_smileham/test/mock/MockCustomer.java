package restaurant.restaurant_smileham.test.mock;


import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.Menu;
import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.gui.CustomerGui;
import restaurant.restaurant_smileham.interfaces.SmilehamCashier;
import restaurant.restaurant_smileham.interfaces.SmilehamCustomer;
import restaurant.restaurant_smileham.interfaces.SmilehamHost;
import restaurant.restaurant_smileham.interfaces.SmilehamWaiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements SmilehamCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public SmilehamCashier mCashier;

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgGotHungry() {
		
	}

	@Override
	public void msgRestaurantFull() {
		
	}

	@Override
	public void msgSitAtTable(SmilehamWaiter waiter, int tableNum, Menu menu) {
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		
	}

	@Override
	public void msgWhatWouldYouLike(Menu menu) {
		
	}

	@Override
	public void msgHereIsYourFood(EnumFoodOptions choice) {
		
	}

	@Override
	public void msgCheckDelivered(Check check) {
		
	}

	@Override
	public void msgGoodToGo(int change) {
		log.add(new LoggedEvent("msgGoodToGo(" + change + ")"));
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	@Override
	public void setHost(SmilehamHost host) {
		
	}

	@Override
	public String getCustomerName() {
		return null;
	}

	@Override
	public int getHungerLevel() {
		return 0;
	}

	@Override
	public int getCash() {
		return 0;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		
	}

	@Override
	public void setGui(CustomerGui g) {
		
	}

	@Override
	public CustomerGui getGui() {
		return null;
	}

	@Override
	public boolean isHungry() {
		return false;
	}

	@Override
	public void msgAnimationPickedUp() {
		
	}

}
