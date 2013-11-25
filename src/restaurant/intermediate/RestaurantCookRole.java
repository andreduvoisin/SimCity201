package restaurant.intermediate;

import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import restaurant.restaurant_davidmca.roles.CookRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCookRole extends BaseRole {
	
	Role subRole = null;
	int restaurantID;
	static int DEFAULT_FOOD_QTY = 5;
	
	public RestaurantCookRole(Person person){
		super(person);
	}
	
	public RestaurantCookRole(Person person, int restaurantID){
		super(person);
		setRestaurant(restaurantID);
	}
	
	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			subRole = RestaurantPanel.getInstance().cook;
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
