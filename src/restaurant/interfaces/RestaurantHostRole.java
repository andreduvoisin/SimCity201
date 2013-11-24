package restaurant.interfaces;

import restaurant.restaurant_davidmca.roles.CustomerRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantHostRole extends BaseRole {
	
	Role subRole = null;
	
	public RestaurantHostRole(Person person){
		mPerson = person;
	}
	
	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			subRole = new CustomerRole(mPerson.getName());
		}
		//TODO DAVID add if statements for all the other restaurants
	}
	
	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}

}
