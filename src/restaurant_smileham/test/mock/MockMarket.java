package restaurant_smileham.test.mock;

import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.interfaces.Market;

public class MockMarket extends Mock implements Market{

	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgOrderFood(EnumFoodOptions food, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgPayingMarket(int amount) {
		log.add(new LoggedEvent("msgPayingMarket(" + amount + ")"));
	}

}
