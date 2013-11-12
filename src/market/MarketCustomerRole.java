package market;

import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.Menu;
import restaurant_smileham.agent.Check;
import restaurant_smileham.gui.CustomerGui;
import market.interfaces.Customer;
import market.interfaces.Host;
import market.interfaces.Waiter;
import base.Role;

public class MarketCustomerRole extends Role implements Customer{

	
	
	
	
	
	
	
	
	
	@Override
	public void msgGotHungry() {
		
	}

	@Override
	public void msgRestaurantFull() {
		
	}

	@Override
	public void msgSitAtTable(Waiter waiter, int tableNum, Menu menu) {
		
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
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		
	}

	@Override
	public void msgAnimationPickedUp() {
		
	}

	@Override
	public void setHost(Host host) {
		
	}

	@Override
	public String getCustomerName() {
		return null;
	}

	@Override
	public String getName() {
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

}
