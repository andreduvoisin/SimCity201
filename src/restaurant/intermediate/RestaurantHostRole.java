package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_duvoisin.roles.AndreCustomerRole;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
import restaurant.restaurant_xurex.RexHostRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import restaurant.restaurant_tranac.gui.RestaurantPanel_at;
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
				subRole = AndreRestaurantPanel.getInstance().host;
				subRole.setPerson(super.mPerson);
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
				subRole = RestaurantPanel_at.getInstance().mHost;
				subRole.setPerson(mPerson);
				break;
			case 7: //rex
				subRole = RexAnimationPanel.getHost();
				subRole.setPerson(super.mPerson);
				RexAnimationPanel.addPerson((RexHostRole)subRole);
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
