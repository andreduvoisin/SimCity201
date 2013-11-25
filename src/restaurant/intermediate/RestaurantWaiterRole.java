package restaurant.intermediate;

import java.util.Random;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import restaurant.restaurant_davidmca.roles.WaiterRole;
import restaurant.restaurant_davidmca.roles.WaiterRoleShared;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantWaiterRole extends BaseRole implements RestaurantBaseInterface {

	Role subRole = null;
	int restaurantID;

	public RestaurantWaiterRole(Person person) {
		super(person);
	}

	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			int rn = new Random().nextInt();
			if (rn % 2 == 0) {
				subRole = new WaiterRole(super.mPerson);
				RestaurantPanel.getInstance().addWaiter((WaiterRole) subRole);
			} else {
				subRole = new WaiterRoleShared(super.mPerson);
				RestaurantPanel.getInstance().addSharedWaiter(
						(WaiterRoleShared) subRole);
			}
		}
		// TODO DAVID add if statements for all the other restaurants
	}
	
	public void setPerson(Person person){
		super.mPerson = person;	
	}

	public boolean pickAndExecuteAnAction() {
		//System.out.println("RestaurantWaiterRole pAEA run");
		return subRole.pickAndExecuteAnAction();
	}
}
