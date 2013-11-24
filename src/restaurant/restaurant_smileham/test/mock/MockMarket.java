package restaurant.restaurant_smileham.test.mock;

import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.interfaces.Market;

public class MockMarket extends Mock implements Market{

	public MockMarket(String name) {
		super(name);
	}

	@Override
	public void msgOrderFood(EnumFoodOptions food, int amount) {
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {

		return false;
	}

	@Override
	public boolean isOut() {
		return false;
	}

	@Override
	public void msgPayingMarket(int amount) {
		log.add(new LoggedEvent("msgPayingMarket(" + amount + ")"));
	}

}
