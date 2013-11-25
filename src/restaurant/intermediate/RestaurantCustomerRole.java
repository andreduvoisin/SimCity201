package restaurant.intermediate;

import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import restaurant.restaurant_davidmca.roles.CustomerRole;
import restaurant.restaurant_smileham.gui.SmilehamAgentPanel;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
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
		if (restaurantID != 5) {
			subRole = new CustomerRole(super.mPerson);
			RestaurantPanel.getInstance().addCustomer((CustomerRole) subRole);
		}
		// TODO DAVID add if statements for all the other restaurants
		if (restaurantID == 5) { //Shane's restaurant
			subRole = new SmilehamCustomerRole(super.mPerson, mPerson.getName(), null);
			SmilehamAgentPanel.addPerson((SmilehamCustomerRole) subRole);
		}
	}
	
	public void setPerson(Person person){
		super.mPerson = person;
	}

	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}
}
