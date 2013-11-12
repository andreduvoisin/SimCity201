package restaurant_smileham.test.mock;

import restaurant_smileham.Order;
import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.gui.CookGui;
import restaurant_smileham.interfaces.Cook;

public class MockCook extends Mock implements Cook{

	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgMakeFood(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderResponse(EnumFoodOptions food, int newIncomingAmount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderFulfillment(EnumFoodOptions food, int amountArrived) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addMarket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CookGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CookGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshLabels() {
		// TODO Auto-generated method stub
		
	}

}
