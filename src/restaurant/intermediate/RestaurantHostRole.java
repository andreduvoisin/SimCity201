package restaurant.intermediate;

import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantHostRole extends BaseRole {
	
	Role subRole = null;
	int restaurantID;
	
	public RestaurantHostRole(Person person){
		super(person);
	}
	
	public RestaurantHostRole(Person person, int restID){
		super(person);
		restaurantID = restID;
	}
	
	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			subRole = RestaurantPanel.getInstance().host;
			subRole.setPerson(mPerson);
		}
		//TODO DAVID add if statements for all the other restaurants
	}
	
	public void setPerson(Person person){
		mPerson = person;
	}
	
	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}

}
