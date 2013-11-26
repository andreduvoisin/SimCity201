package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_cwagoner.gui.CwagonerRestaurantPanel;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_jerryweb.gui.JerrywebRestaurantPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import restaurant.restaurant_tranac.gui.RestaurantPanel_at;
import restaurant.restaurant_xurex.RexCashierRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCashierRole extends BaseRole implements RestaurantBaseInterface {
	
	static int totalCashiers = 0;
	
	Role subRole = null;
	int restaurantID;

	public RestaurantCashierRole(Person person){
		super(person);
	}
	
	public void setRestaurant(int restaurantID) {

		switch(restaurantID){
			case 0: //andre
				subRole = AndreRestaurantPanel.getInstance().cashier;
				subRole.setPerson(super.mPerson);
				break;
			case 1: //chase
				subRole = CwagonerRestaurantPanel.getInstance().cashier;
				subRole.setPerson(super.mPerson);
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
				subRole = RestaurantPanel_at.getInstance().mCashier;
				subRole.setPerson(mPerson);
				break;
			case 7: //rex
				subRole =  RexAnimationPanel.getCashier();
				subRole.setPerson(super.mPerson);
				RexAnimationPanel.addPerson((RexCashierRole)subRole);
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
