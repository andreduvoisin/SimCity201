package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_cwagoner.CwagonerRestaurant;
import restaurant.restaurant_cwagoner.roles.CwagonerSharedWaiterRole;
import restaurant.restaurant_cwagoner.roles.CwagonerWaiterRole;
import restaurant.restaurant_davidmca.DavidRestaurant;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;
import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_duvoisin.roles.AndreSharedWaiterRole;
import restaurant.restaurant_duvoisin.roles.AndreWaiterRole;
import restaurant.restaurant_jerryweb.JerrywebRSWaiterRole;
import restaurant.restaurant_jerryweb.JerrywebRestaurant;
import restaurant.restaurant_jerryweb.JerrywebWaiterRole;
import restaurant.restaurant_maggiyan.MaggiyanRestaurant;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;
import restaurant.restaurant_smileham.SmilehamRestaurant;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.roles.TranacWaiterBase;
import restaurant.restaurant_tranac.roles.TranacWaiterRSRole;
import restaurant.restaurant_tranac.roles.TranacWaiterRole;
import restaurant.restaurant_xurex.RexWaiterRole1;
import restaurant.restaurant_xurex.RexWaiterRole2;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;

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
					AndreRestaurant.addWaiter((AndreWaiterRole) subRole);
			} else if (mWaiterType == 0) {
				subRole = new AndreSharedWaiterRole(super.mPerson);
					AndreRestaurant.addSharedWaiter((AndreSharedWaiterRole) subRole);
			}
			break;
		case 1: // chase
			if (mWaiterType == 1) {
				subRole = new CwagonerWaiterRole(super.mPerson);
				CwagonerRestaurant.addPerson(subRole);
			}
			else if (mWaiterType == 0) {
				subRole = new CwagonerSharedWaiterRole(super.mPerson);
				CwagonerRestaurant.addPerson(subRole);
			}
			break;
		case 2:
			if (mWaiterType == 0) {
				subRole = new JerrywebWaiterRole(super.mPerson);
				JerrywebRestaurant.addPerson((JerrywebWaiterRole) subRole);
			} else if (mWaiterType == 1) {
				subRole = new JerrywebRSWaiterRole(super.mPerson);
				//JerrywebRestaurant.addPerson((JerrywebRSWaiterRole) subRole);
				JerrywebRestaurant.addRSWaiter((JerrywebRSWaiterRole) subRole);
			}
			break;
		case 3: // maggi
			if (mWaiterType == 1) {
				subRole = new MaggiyanWaiterRole(super.mPerson);
				MaggiyanRestaurant.addWaiter((MaggiyanWaiterRole) subRole);
			} else if (mWaiterType == 0) {
				subRole = new MaggiyanSharedWaiterRole(super.mPerson);
				MaggiyanRestaurant.addSharedWaiter((MaggiyanSharedWaiterRole) subRole);
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
			//if (mWaiterType == 1) {
				subRole = new SmilehamWaiterRole(super.mPerson);
				SmilehamRestaurant.addPerson((SmilehamWaiterRole) subRole);
			//} else if (mWaiterType == 0) {
				// SHANE: 1 add shared waiter
			//}
			break;
		case 6: // angelica
			if(mWaiterType == 1) {
				subRole = new TranacWaiterRole(mPerson);
				TranacRestaurant.addPerson((TranacWaiterBase) subRole);
			}
			else if (mWaiterType == 0) {
				subRole = new TranacWaiterRSRole(mPerson);
				TranacRestaurant.addPerson((TranacWaiterBase) subRole);
			}
			break;
		case 7: // rex
			mWaiterType = RexAnimationPanel.getInstance().getWaiterNumber(mWaiterType);
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
