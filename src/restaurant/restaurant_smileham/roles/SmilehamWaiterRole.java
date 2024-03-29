package restaurant.restaurant_smileham.roles;

import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.Order.EnumOrderStatus;
import base.interfaces.Person;

public class SmilehamWaiterRole extends SmilehamWaiterBase{

	public SmilehamWaiterRole(Person person) {
		super(person);
	}

	@Override
	protected void deliverOrder(Order order) {
		print("Action: deliverOrder()");
		
		mWaiterGui.DoGoToCook();
		acquireSemaphore(semAtCook);
		
		order.mOrderStatus = EnumOrderStatus.PENDING;
		mCook.msgMakeFood(order);
			
	}

}
