package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
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
				subRole = new SmilehamHostRole(super.mPerson);
				SmilehamAnimationPanel.addPerson((SmilehamHostRole) subRole);
				break;
			case 6: //angelica
				break;
			case 7: //rex
				break;
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
