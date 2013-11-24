package restaurant.interfaces;

import restaurant.restaurant_davidmca.roles.CashierRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCashierRole extends BaseRole {
	
	Role subRole = null;

	public RestaurantCashierRole(Person person){
		mPerson = person;
	}
	
	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			subRole = new CashierRole(mPerson.getName());
		}
		//TODO DAVID add if statements for all the other restaurants
	}
	
	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}

}
