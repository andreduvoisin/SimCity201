package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.DavidRestaurant;
import restaurant.restaurant_davidmca.gui.DavidAnimationPanel;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_duvoisin.roles.AndreSharedWaiterRole;
import restaurant.restaurant_duvoisin.roles.AndreWaiterRole;
import restaurant.restaurant_maggiyan.gui.MaggiyanAnimationPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.gui.TranacAnimationPanel;
import restaurant.restaurant_tranac.roles.TranacWaiterRole;
import restaurant.restaurant_xurex.RexWaiterRole1;
import restaurant.restaurant_xurex.RexWaiterRole2;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;
import base.reference.ContactList;

public class RestaurantWaiterRole extends BaseRole implements
		RestaurantBaseInterface {

	static int totalWaiters = 0;
	Role subRole = null;
	int mRestaurantID;
	int mWaiterType;

	public RestaurantWaiterRole(Person person, int restaurantID, int waiterType) {
		super(person);
		this.mRestaurantID = restaurantID;
		this.mWaiterType = waiterType;
	}

	public void setPerson(Person person) {
		super.mPerson = person;
		switch (mRestaurantID) {
		case 0: // andre
			if (mWaiterType == 1) {
				subRole = new AndreWaiterRole(super.mPerson);
				AndreRestaurantPanel.instance.addPerson((AndreWaiterRole) subRole);
			} else if (mWaiterType == 0) {
				subRole = new AndreSharedWaiterRole(super.mPerson);
				AndreRestaurantPanel.instance.addPerson((AndreSharedWaiterRole) subRole);
			}
			break;
//		case 1: // chase
//			if (mWaiterType == 1) {
//				subRole = new CwagonerWaiterRole(super.mPerson);
//				((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).addPerson(subRole);
//			}
//			else if (mWaiterType == 0) {
//				subRole = new CwagonerSharedWaiterRole(super.mPerson);
//				((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).addPerson(subRole);
//			}
//			break;
//		case 2:
//			if (mWaiterType == 1) {//JERRY: shouldn't you use citypanel.masterRestaurantList.get(2)?
//									// This statically adds a waiter to your rest.panel class
//									// but not to the masterRestaurantList (is taken care of elsewhere?)
//				subRole = new JerrywebWaiterRole(super.mPerson);
//				JerrywebRestaurantPanel.addWaiter((JerrywebWaiterRole) subRole);
//			} else if (mWaiterType == 0) {
//				subRole = new JerrywebRSWaiterRole(super.mPerson);
//				JerrywebRestaurantPanel
//						.addRSWaiter((JerrywebRSWaiterRole) subRole);
//			}
//			break;
		case 3: // maggi
			if (mWaiterType == 1) {
				subRole = new MaggiyanWaiterRole(mPerson);
				MaggiyanAnimationPanel.addPerson((MaggiyanWaiterRole) subRole);
			} else if (mWaiterType == 0) {
				subRole = new MaggiyanSharedWaiterRole(mPerson);
				MaggiyanAnimationPanel.addPerson((MaggiyanSharedWaiterRole) subRole);
			}
			break;
		case 4: // david
			if (mWaiterType == 1) {
				subRole = new DavidWaiterRole(super.mPerson);
				DavidRestaurant.addWaiter((DavidWaiterRole) subRole);
			} else if (mWaiterType == 0) {
				subRole = new DavidWaiterRoleShared(super.mPerson);
				DavidRestaurant.addSharedWaiter((DavidWaiterRoleShared) subRole);
			}
			break;
		case 5: // shane
			if (mWaiterType == 1) {
				subRole = new SmilehamWaiterRole(mPerson);
				SmilehamAnimationPanel.addPerson((SmilehamWaiterRole) subRole);
			} else if (mWaiterType == 0) {
				// SHANE: add shared waiter
			}
			break;
		case 6: // angelica
			if(mWaiterType == 1) {
				subRole = new TranacWaiterRole(mPerson);
				TranacRestaurant.addPerson((TranacWaiterRole) subRole);
			}
			else if (mWaiterType == 0) {
				//ANGELICA: add shared waiter
			}
			break;
		case 7: // rex
			if (mWaiterType == 1) {
				subRole = new RexWaiterRole1(mPerson);
				RexAnimationPanel.addPerson((RexWaiterRole1) subRole);
			} else if (mWaiterType == 0) {
				subRole = new RexWaiterRole2(mPerson);
				RexAnimationPanel.addPerson((RexWaiterRole2) subRole);
			}
			break;
		}

	}

	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}

	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
	}
}
