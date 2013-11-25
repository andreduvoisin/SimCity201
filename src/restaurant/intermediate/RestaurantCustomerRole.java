package restaurant.intermediate;

import java.io.IOException;

import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import restaurant.restaurant_davidmca.roles.CustomerRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCustomerRole extends BaseRole {

	Role subRole = null;
	int restaurantID;

	public RestaurantCustomerRole(Person person) {
		super(person);
	}

	public void setRestaurant(int restaurantID) throws IOException {
		if (restaurantID == 1) {
			subRole = new CustomerRole(mPerson);
			RestaurantPanel.getInstance().addCustomer((CustomerRole) subRole);
		}
		// TODO DAVID add if statements for all the other restaurants
	}
	
	public void setPerson(Person person){
		mPerson = person;		
	}

	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}
}
