package restaurant.restaurant_smileham.test.mock;

import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.gui.CookGui;
import restaurant.restaurant_smileham.interfaces.Cook;

public class MockCook extends Mock implements Cook{

	public MockCook(String name) {
		super(name);
	}

	@Override
	public void msgMakeFood(Order order) {
		
	}

	@Override
	public void msgOrderResponse(EnumFoodOptions food, int newIncomingAmount) {
		
	}

	@Override
	public void msgOrderFulfillment(EnumFoodOptions food, int amountArrived) {
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	@Override
	public void addMarket() {
		
	}

	@Override
	public void setGui(CookGui gui) {
		
	}

	@Override
	public CookGui getGui() {
		return null;
	}

	@Override
	public void refreshLabels() {
		
	}

}
