package restaurant.intermediate;

import java.util.Random;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantHostRole extends BaseRole implements RestaurantBaseInterface {
	
	static int totalHosts = 0;
	Role subRole = null;
	int restaurantID;
	
	public RestaurantHostRole(Person person){
		super(person);
	}
	
	public void setRestaurant(int restaurantID) {
		if (totalHosts < 1) {
		switch(restaurantID){
			case 0: //andre
				break;
			case 1: //chase
				break;
			case 2: //jerry
				break;
			case 3: //maggi
				subRole = MaggiyanRestaurantPanel.getRestPanel().host;
				subRole.setPerson(super.mPerson);
				break;
			case 4: //david
				subRole = DavidRestaurantPanel.getInstance().host;
				subRole.setPerson(super.mPerson);
				break;
			case 5: //shane
				break;
			case 6: //angelica
				break;
			case 7: //rex
				break;
		}
		totalHosts++;
		}
	}
	
	public void setPerson(Person person){
		super.mPerson = person;
	}
	
	public boolean pickAndExecuteAnAction() {
		//System.out.println("RestaurantHostRole pAEA run");
		return subRole.pickAndExecuteAnAction();
	}

}
