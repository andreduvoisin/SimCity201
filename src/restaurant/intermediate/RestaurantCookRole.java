package restaurant.intermediate;

import restaurant.restaurant_davidmca.roles.CookRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCookRole extends BaseRole {
	
	Role subRole = null;
	int restaurantID;
	static int DEFAULT_FOOD_QTY = 5;
	
	public RestaurantCookRole(Person person){
		mPerson = person;
	}
	
	public RestaurantCookRole(Person person, int restaurantID){
		mPerson = person;
		setRestaurant(restaurantID);
	}
	
	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			subRole = new CookRole("Cook", DEFAULT_FOOD_QTY);
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
