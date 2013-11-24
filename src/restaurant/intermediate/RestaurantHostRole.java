package restaurant.intermediate;

import restaurant.restaurant_davidmca.roles.HostRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantHostRole extends BaseRole {
	
	Role subRole = null;
	int restaurantID;
	
	public RestaurantHostRole(Person person){
		mPerson = person;
	}
	
	public RestaurantHostRole(Person person, int restID){
		mPerson = person;
		restaurantID = restID;
	}
	
	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			subRole = new HostRole("Host");
		}
		//TODO DAVID add if statements for all the other restaurants
	}
	
	public void setPerson(Person person){
		mPerson = person;
		setRestaurant(restaurantID);
		subRole.setPerson(person);
	}
	
	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}

}
