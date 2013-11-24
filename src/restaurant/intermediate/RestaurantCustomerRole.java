package restaurant.intermediate;

import java.io.IOException;

import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import restaurant.restaurant_davidmca.roles.CustomerRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCustomerRole extends BaseRole {

	Role subRole = null;

	public RestaurantCustomerRole(Person person) {
		mPerson = person;
	}
	
	public RestaurantCustomerRole(Person person, int restaurantID){
		mPerson = person;
		try {
			setRestaurant(restaurantID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setRestaurant(int restaurantID) throws IOException {
		if (restaurantID == 1) {
			subRole = new CustomerRole(mPerson.getName());
			RestaurantPanel.getInstance().addCustomer((CustomerRole) subRole);
		}
		// TODO DAVID add if statements for all the other restaurants
	}
	
	public void setPerson(Person person){
		mPerson = person;
		subRole.setPerson(person);
	}

	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}
}
