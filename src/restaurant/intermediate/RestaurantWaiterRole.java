package restaurant.intermediate;

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
	int restaurantID;

	public RestaurantWaiterRole(Person person) {
		super(person);
	}

	public void setRestaurant(int restaurantID) throws IOException {
		if (restaurantID == 1) {
			int rn = new Random().nextInt();
			if (rn % 2 == 0) {
				subRole = new WaiterRole(mPerson);
				RestaurantPanel.getInstance().addWaiter((WaiterRole) subRole);
			} else {
				subRole = new WaiterRoleShared(mPerson);
				RestaurantPanel.getInstance().addSharedWaiter(
						(WaiterRoleShared) subRole);
			}
		}
		// TODO DAVID add if statements for all the other restaurants
	}
	
	public void setPerson(Person person){
		mPerson = person;		
	}

	public boolean pickAndExecuteAnAction() {
		print("paea in generic waiter run");
		return subRole.pickAndExecuteAnAction();
	}
}
