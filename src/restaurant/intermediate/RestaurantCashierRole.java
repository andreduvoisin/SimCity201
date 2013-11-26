package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_jerryweb.gui.JerrywebRestaurantPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCashierRole extends BaseRole implements RestaurantBaseInterface {
	
	Role subRole = null;
	int restaurantID;

	public RestaurantCashierRole(Person person){
		super(person);
	}
	
	public void setRestaurant(int restaurantID) {
		switch(restaurantID){
			case 0: //andre
				break;
			case 1: //chase
				break;
			case 2: //jerry
				subRole = JerrywebRestaurantPanel.getInstance().cashier;
				subRole.setPerson(super.mPerson);
				break;
			case 3: //maggi
				subRole = MaggiyanRestaurantPanel.getRestPanel().cashier;
				subRole.setPerson(super.mPerson);
				break;
			case 4: //david
				subRole = DavidRestaurantPanel.getInstance().cash;
				subRole.setPerson(super.mPerson);
				break;
			case 5: //shane
				subRole = new SmilehamCashierRole(super.mPerson);
				SmilehamAnimationPanel.addPerson((SmilehamCashierRole) subRole);
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
		return subRole.pickAndExecuteAnAction();
	}

}
