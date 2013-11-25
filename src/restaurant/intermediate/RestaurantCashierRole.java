package restaurant.intermediate;

import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCashierRole extends BaseRole implements RestaurantBaseInterface {
	
	Role subRole = null;
	int restaurantID;

	public RestaurantCashierRole(Person person){
		super(person);
	}
	
	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			subRole = RestaurantPanel.getInstance().cash;
			subRole.setPerson(mPerson);
		}
		//TODO DAVID add if statements for all the other restaurants
	}
	
	public void setPerson(Person person){
		super.mPerson = person;
	}
	
	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}

}
