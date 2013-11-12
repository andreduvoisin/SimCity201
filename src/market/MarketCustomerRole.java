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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(Waiter waiter, int tableNum, Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood(EnumFoodOptions choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckDelivered(Check check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoodToGo(int change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationPickedUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCash() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isHungry() {
		// TODO Auto-generated method stub
		return false;
	}

}
