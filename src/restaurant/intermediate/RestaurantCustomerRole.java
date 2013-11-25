package restaurant.intermediate;

import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import restaurant.restaurant_davidmca.roles.CustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCustomerRole extends BaseRole implements RestaurantBaseInterface {

	Role subRole = null;
	int restaurantID;

	public RestaurantCustomerRole(Person person) {
		super(person);
	}

	public void setRestaurant(int restaurantID) {
		//David
		if (restaurantID == 1) {
			subRole = new CustomerRole(super.mPerson);
			RestaurantPanel.getInstance().addCustomer((CustomerRole) subRole);
		}
		//Maggi
		if (restaurantID == 2) {
			subRole = new MaggiyanCustomerRole(super.mPerson);
			//MaggiyanRestaurantPanel.getRestPanel().addCustomer((CustomerRole) subRole);
		}
		// TODO DAVID add if statements for all the other restaurants
	}
	
	public void setPerson(Person person){
		super.mPerson = person;
	}

	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}
}
