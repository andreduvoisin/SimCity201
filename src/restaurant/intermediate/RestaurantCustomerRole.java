package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.DavidAnimationPanel;
import restaurant.restaurant_davidmca.roles.DavidCustomerRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCustomerRole extends BaseRole implements
		RestaurantBaseInterface {

	static int totalCustomers = 0;

	Role subRole = null;
	int mRestaurantID = -1;

	public RestaurantCustomerRole(Person person) {
		super(person);
	}

	public void setPerson(Person person) {
		super.mPerson = person;
	}

	public void setRestaurant(int restaurantID) {
		this.mRestaurantID = restaurantID;
		switch (mRestaurantID) {
//		case 0: // andre
//			subRole = new AndreCustomerRole(super.mPerson);
//			((AndreRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(0)).addPerson(
//					(AndreCustomerRole) subRole);
//			break;
//		case 1: // chase
//			subRole = new CwagonerCustomerRole(super.mPerson);
//			((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).addPerson(subRole);
//			break;
//		case 2: // jerry
//			subRole = new JerrywebCustomerRole(super.mPerson);
//			((JerrywebRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(2)).addPerson(
//					(JerrywebCustomerRole) subRole);
//			break;
//		case 3: // maggi
//			subRole = new MaggiyanCustomerRole(super.mPerson);
//			((MaggiyanRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(3)).addCustomer(
//					(MaggiyanCustomerRole) subRole);
//			break;
		case 4: // david
			subRole = new DavidCustomerRole(super.mPerson);
			DavidAnimationPanel.addCustomer((DavidCustomerRole) subRole);
			break;
		case 5: // shane
			subRole = new SmilehamCustomerRole(super.mPerson);
			SmilehamAnimationPanel.addPerson((SmilehamCustomerRole) subRole);
			break;
//		case 6: // angelica
//			subRole = new TranacRestaurantCustomerRole(mPerson);
//			((TranacRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(6)).addCustomer(
//					(TranacRestaurantCustomerRole) subRole);
//			break;
//		case 7: // rex
//			RexCustomerRole temp = new RexCustomerRole(
//					((RexAnimationPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(7)),
//					RexAnimationPanel.getHost());
//			temp.setName("Joe");
//			temp.setCashier(RexAnimationPanel.cashier);
//			subRole = temp;
//			// creates CustomerGui and adds to animationPanels
//			subRole.setPerson(super.mPerson);
//			RexAnimationPanel.addPerson((RexCustomerRole) subRole);
//			// calls gotHungry when addPerson for CustomerRole
//			break;
		}
	}

	public boolean pickAndExecuteAnAction() {
		// print("generic pAEA called");
		return subRole.pickAndExecuteAnAction();
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
	}
}
