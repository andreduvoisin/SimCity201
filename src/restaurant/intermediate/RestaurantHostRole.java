package restaurant.intermediate;

import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantHostRole extends BaseRole implements RestaurantBaseInterface {
	
	Role subRole = null;
	int restaurantID;
	
	public RestaurantHostRole(Person person){
		super(person);
	}
	
	public void setRestaurant(int restaurantID) {
		//David's Restaurant 
		if (restaurantID == 1) {
			subRole = RestaurantPanel.getInstance().host;
			subRole.setPerson(super.mPerson);
		}
		//Maggi's Restaurant 
		if (restaurantID == 2) {
			subRole = MaggiyanRestaurantPanel.getRestPanel().host;
			subRole.setPerson(super.mPerson);
		}
		//TODO DAVID add if statements for all the other restaurants
	}
	
	public void setPerson(Person person){
		super.mPerson = person;
	}
	
	public boolean pickAndExecuteAnAction() {
		//System.out.println("RestaurantHostRole pAEA run");
		return subRole.pickAndExecuteAnAction();
	}

}
