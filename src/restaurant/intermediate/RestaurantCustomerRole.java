package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_davidmca.roles.DavidCustomerRole;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_duvoisin.roles.AndreCustomerRole;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_tranac.gui.RestaurantPanel_at;
import restaurant.restaurant_tranac.roles.RestaurantCustomerRole_at;
import restaurant.restaurant_xurex.RexCustomerRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCustomerRole extends BaseRole implements RestaurantBaseInterface {

	static int totalCustomers = 0;
	
	Role subRole = null;
	int restaurantID;

	public RestaurantCustomerRole(Person person) {
		super(person);
	}

	public void setRestaurant(int restaurantID) {

		// TODO ALL add if statements for all the other restaurants

		switch(restaurantID){
			case 0: //andre
				subRole = new AndreCustomerRole(super.mPerson);
				AndreRestaurantPanel.getInstance().addPerson((AndreCustomerRole)subRole);
				break;
			case 1: //chase
				break;
			case 2: //jerry
	//			subRole = new JerrywebCustomerRole(super.mPerson);
	//			JerrywebRestaurantPanel.getInstance().addPerson((JerrywebCustomerRole) subRole);
				break;
			case 3: //maggi
				subRole = new MaggiyanCustomerRole(super.mPerson);
				MaggiyanRestaurantPanel.getRestPanel().addCustomer((MaggiyanCustomerRole) subRole);
				break;
			case 4: //david
				subRole = new DavidCustomerRole(super.mPerson);
				DavidRestaurantPanel.getInstance().addCustomer((DavidCustomerRole) subRole);
				break;
			case 5: //shane
				System.out.println("Making Customer");
				subRole = new SmilehamCustomerRole(super.mPerson);
				SmilehamAnimationPanel.addPerson((SmilehamCustomerRole) subRole);
				break;
			case 6: //angelica
				subRole = new RestaurantCustomerRole_at(mPerson);
				RestaurantPanel_at.getInstance().addCustomer((RestaurantCustomerRole_at) subRole);
				break;
			case 7: //rex
				subRole = new RexCustomerRole(RexAnimationPanel.getInstance(), RexAnimationPanel.getHost());
				//creates CustomerGui and adds to animationPanels
				subRole.setPerson(super.mPerson);
				RexAnimationPanel.addPerson((RexCustomerRole)subRole);
				//calls gotHungry when addPerson for CustomerRole
				break;

		}
	}
	
	public void setPerson(Person person){
		super.mPerson = person;
	}

	public boolean pickAndExecuteAnAction() {
	//	print("generic pAEA called");
		return subRole.pickAndExecuteAnAction();
	}
}
