package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_davidmca.roles.DavidCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_tranac.gui.RestaurantPanel_at;
import restaurant.restaurant_tranac.roles.RestaurantCustomerRole_at;
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
		if (restaurantID == 4) {
			subRole = new DavidCustomerRole(super.mPerson);
			DavidRestaurantPanel.getInstance().addCustomer((DavidCustomerRole) subRole);
		}
		//Maggi
		if (restaurantID == 2) {
			subRole = new MaggiyanCustomerRole(super.mPerson);
			//MaggiyanRestaurantPanel.getRestPanel().addCustomer((CustomerRole) subRole);
		}
		
		//Angelica
		if (restaurantID == 3) {
			subRole = new RestaurantCustomerRole_at(mPerson);
			RestaurantPanel_at.getInstance().addCustomer((RestaurantCustomerRole_at) subRole);
		}
		// TODO DAVID add if statements for all the other restaurants
		if (restaurantID == 5) { //Shane's restaurant
			subRole = new SmilehamCustomerRole(super.mPerson, mPerson.getName(), null);
			SmilehamAnimationPanel.addPerson((SmilehamCustomerRole) subRole);
		}
	}
	
	public void setPerson(Person person){
		super.mPerson = person;
	}

	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}
}
