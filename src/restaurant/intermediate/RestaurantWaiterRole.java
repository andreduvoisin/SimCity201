package restaurant.intermediate;

import java.util.Random;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;


import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_duvoisin.roles.AndreSharedWaiterRole;
import restaurant.restaurant_duvoisin.roles.AndreWaiterRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
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
		switch(restaurantID){
			case 0: //andre
				//int rn0 = new Random().nextInt();
				//if (rn0 % 2 == 0) {
					subRole = new AndreWaiterRole(super.mPerson);
					AndreRestaurantPanel.getInstance().addPerson((AndreWaiterRole) subRole);
//				} else {
//					subRole = new AndreSharedWaiterRole(super.mPerson);
//					AndreRestaurantPanel.getInstance().addPerson((AndreSharedWaiterRole) subRole);
//				}
				break;
			case 1: 
				break;
			case 2:
				break;
			case 3:
				int rn1 = new Random().nextInt();
				
				if (rn1 % 2 == 0) {
					subRole = new MaggiyanWaiterRole(super.mPerson);
					MaggiyanRestaurantPanel.getRestPanel().addWaiter((MaggiyanWaiterRole) subRole);
				}else {
					subRole = new MaggiyanSharedWaiterRole(super.mPerson);
					MaggiyanRestaurantPanel.getRestPanel().addSharedWaiter((MaggiyanSharedWaiterRole) subRole);
				}
				break;
			case 4: //david
				int rn = new Random().nextInt();
				if (rn % 2 == 0) {
					subRole = new DavidWaiterRole(super.mPerson);
					DavidRestaurantPanel.getInstance().addWaiter((DavidWaiterRole) subRole);
				} else {
					subRole = new DavidWaiterRoleShared(super.mPerson);
					DavidRestaurantPanel.getInstance().addSharedWaiter(
							(DavidWaiterRoleShared) subRole);
				}
				break;
			case 5: //shane
				subRole = new SmilehamWaiterRole(mPerson);
				SmilehamAnimationPanel.addPerson((SmilehamWaiterRole) mPerson);
				break;
			case 6:
				break;
			case 7:
				break;
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
