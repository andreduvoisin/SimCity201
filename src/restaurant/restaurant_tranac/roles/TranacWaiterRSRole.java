package restaurant.restaurant_tranac.roles;

import base.interfaces.Person;

public class TranacWaiterRSRole extends TranacWaiterBase {

	public TranacWaiterRSRole(Person p) {
		super(p);
	}
	
	protected void sendOrder(MyCustomer c) {
		print("Putting order on revolving stand.");
		DoGoToRevolvingStand();
		c.s = CustomerState.WaitingForFood;
		mCook.addOrderToStand(this, c.choice, c.table);
	}
	
	private void DoGoToRevolvingStand() {
		waiterGui.DoGoToRevolvingStand();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
