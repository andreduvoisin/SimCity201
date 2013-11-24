package restaurant.interfaces;

import java.util.Random;

import restaurant.restaurant_davidmca.roles.WaiterRole;
import restaurant.restaurant_davidmca.roles.WaiterRoleShared;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantWaiterRole extends BaseRole {

	Role subRole = null;

	public RestaurantWaiterRole(Person person) {
		mPerson = person;
	}

	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			int rn = new Random().nextInt();
			if (rn % 2 == 0) {
				subRole = new WaiterRole(mPerson.getName());
			} else {
				subRole = new WaiterRoleShared(mPerson.getName());
			}
		}
		// TODO DAVID add if statements for all the other restaurants
	}

	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}
}
