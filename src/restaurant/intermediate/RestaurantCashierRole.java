package restaurant.intermediate;

import restaurant.restaurant_davidmca.roles.CashierRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCashierRole extends BaseRole {
	
	Role subRole = null;
	int restaurantID;

	public RestaurantCashierRole(Person person){
		mPerson = person;
	}
	
	public RestaurantCashierRole(Person person, int restaurantID){
		mPerson = person;
		setRestaurant(restaurantID);
	}
	
	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			subRole = new CashierRole("Cashier");
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
