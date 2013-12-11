package restaurant.restaurant_tranac.roles;

import base.interfaces.Person;

public class TranacWaiterRole extends TranacWaiterBase {

	public TranacWaiterRole(Person p) {
		super(p);
	}
	
	protected void sendOrder(MyCustomer c) {
		print("Sending order to cook.");
		DoGoToCook();
		c.s = CustomerState.WaitingForFood;
		mCook.msgHereIsOrder(this, c.choice, c.table);
	}
}
