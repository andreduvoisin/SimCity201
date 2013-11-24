package restaurant.interfaces;

import java.io.IOException;
import java.util.Random;

import restaurant.restaurant_davidmca.gui.RestaurantPanel;
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

	public void setRestaurant(int restaurantID) throws IOException {
		if (restaurantID == 1) {
			int rn = new Random().nextInt();
			if (rn % 2 == 0) {
				subRole = new WaiterRole(mPerson.getName());
				RestaurantPanel.getInstance().addWaiter((WaiterRole) subRole);
			} else {
				subRole = new WaiterRoleShared(mPerson.getName());
				RestaurantPanel.getInstance().addSharedWaiter(
						(WaiterRoleShared) subRole);
			}
		}
		// TODO DAVID add if statements for all the other restaurants
	}

	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}
}
