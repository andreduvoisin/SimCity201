package restaurant.intermediate;

import java.util.Random;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;


import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;

import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;

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

		if (restaurantID == 3) {
			int rn = new Random().nextInt();
			
			if (rn % 2 == 0) {
				subRole = new MaggiyanWaiterRole(super.mPerson);
				MaggiyanRestaurantPanel.getRestPanel().addWaiter((MaggiyanWaiterRole) subRole);
			} else {
				subRole = new MaggiyanSharedWaiterRole(super.mPerson);
				MaggiyanRestaurantPanel.getRestPanel().addSharedWaiter((MaggiyanSharedWaiterRole) subRole);
			}
		}
		else if (restaurantID == 5){
			subRole = new SmilehamWaiterRole(mPerson);
		}
		else{ //just for now to remove null pointer errors
			subRole = new DavidWaiterRole(super.mPerson);
			DavidRestaurantPanel.getInstance().addWaiter((DavidWaiterRole) subRole);

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
