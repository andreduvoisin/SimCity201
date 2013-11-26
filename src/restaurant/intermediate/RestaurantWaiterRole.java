package restaurant.intermediate;

import java.util.Random;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;
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
		if (restaurantID == 4) {
			int rn = new Random().nextInt();
			if (rn % 2 == 0) {
				subRole = new DavidWaiterRole(super.mPerson);
				DavidRestaurantPanel.getInstance().addWaiter((DavidWaiterRole) subRole);
			} else {
				subRole = new DavidWaiterRoleShared(super.mPerson);
				DavidRestaurantPanel.getInstance().addSharedWaiter(
						(DavidWaiterRoleShared) subRole);
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
