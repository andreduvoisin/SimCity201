package restaurant.restaurant_smileham.test.mock;

import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.gui.CookGui;
import restaurant.restaurant_smileham.interfaces.SmilehamCook;

public class SmilehamMockCook extends SmilehamMock implements SmilehamCook{

	public SmilehamMockCook(String name) {
		super(name);
	}

	@Override
	public void msgMakeFood(Order order) {
		log.add(new LoggedEvent("msgMakeFood"));
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		return false;
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

	@Override
	public void addOrderToStand(Order order) {
		
	}

	@Override
	public void checkStand() {
		
	}

}
