package restaurant.restaurant_smileham.roles;

import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.Order.EnumOrderStatus;
import base.interfaces.Person;

public class SmilehamWaiterRoleShared extends SmilehamWaiterBase{

	public SmilehamWaiterRoleShared(Person person) {
		super(person);
	}

	@Override
	protected void deliverOrder(Order order) {
		print("Action: Putting order on revolving stand.");
		mWaiterGui.DoGoToCook();
		acquireSemaphore(semAtCook);
		order.mOrderStatus = EnumOrderStatus.PENDING;
		mCook.addOrderToStand(order);
	}
}
