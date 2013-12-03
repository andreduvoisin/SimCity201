package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_cwagoner.gui.CwagonerRestaurantPanel;
import restaurant.restaurant_cwagoner.roles.CwagonerCustomerRole;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_davidmca.roles.DavidCustomerRole;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_duvoisin.roles.AndreCustomerRole;
import restaurant.restaurant_jerryweb.JerrywebCustomerRole;
import restaurant.restaurant_jerryweb.gui.JerrywebRestaurantPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_tranac.gui.TranacRestaurantPanel;
import restaurant.restaurant_tranac.roles.TranacRestaurantCustomerRole;
import restaurant.restaurant_xurex.RexCustomerRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCustomerRole extends BaseRole implements RestaurantBaseInterface {

	static int totalCustomers = 0;
	
	Role subRole = null;
	int mRestaurantID = -1;

	public RestaurantCustomerRole(Person person) {
		super(person);
	}
	
	public RestaurantCustomerRole(Person person, int restaurantID) {
		super(person);
		this.mRestaurantID = restaurantID;
	}

	public void setPerson(Person person) {
		super.mPerson = person;
		switch(mRestaurantID){
			case 0: //andre
				subRole = new AndreCustomerRole(super.mPerson);
				AndreRestaurantPanel.getInstance().addPerson((AndreCustomerRole)subRole);
				break;
			case 1: //chase
				subRole = new CwagonerCustomerRole(super.mPerson);
				CwagonerRestaurantPanel.getInstance().addPerson(subRole);
				break;
			case 2: //jerry
				subRole = new JerrywebCustomerRole(super.mPerson);
				JerrywebRestaurantPanel.getInstance().addPerson((JerrywebCustomerRole) subRole);
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
				subRole = new TranacRestaurantCustomerRole(mPerson);
				TranacRestaurantPanel.getInstance().addCustomer((TranacRestaurantCustomerRole) subRole);
				break;
			case 7: //rex
				RexCustomerRole temp = new RexCustomerRole(RexAnimationPanel.getInstance(), RexAnimationPanel.getHost());
				temp.setName("Joe");
				temp.setCashier(RexAnimationPanel.cashier);
				subRole = temp;
				//creates CustomerGui and adds to animationPanels
				subRole.setPerson(super.mPerson);
				RexAnimationPanel.addPerson((RexCustomerRole)subRole);
				//calls gotHungry when addPerson for CustomerRole
				break;

		}
	}
	
	public void setRestaurant(int restaurantID){
		//DAVID: 1 add here
	}

	public boolean pickAndExecuteAnAction() {
		// print("generic pAEA called");
		return subRole.pickAndExecuteAnAction();
	}
}
