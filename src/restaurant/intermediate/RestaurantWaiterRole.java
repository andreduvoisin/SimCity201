package restaurant.intermediate;

import city.gui.SimCityGui;
import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_cwagoner.gui.CwagonerRestaurantPanel;
import restaurant.restaurant_cwagoner.roles.CwagonerSharedWaiterRole;
import restaurant.restaurant_cwagoner.roles.CwagonerWaiterRole;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_duvoisin.roles.AndreSharedWaiterRole;
import restaurant.restaurant_duvoisin.roles.AndreWaiterRole;
import restaurant.restaurant_jerryweb.JerrywebRSWaiterRole;
import restaurant.restaurant_jerryweb.JerrywebWaiterRole;
import restaurant.restaurant_jerryweb.gui.JerrywebRestaurantPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import restaurant.restaurant_tranac.gui.TranacRestaurantPanel;
import restaurant.restaurant_tranac.roles.TranacRestaurantWaiterRole;
import restaurant.restaurant_xurex.RexWaiterRole1;
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
				((AndreRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(0)).addPerson(
						(AndreWaiterRole) subRole);
			} else if (mWaiterType == 0) {
				subRole = new AndreSharedWaiterRole(super.mPerson);
				((AndreRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(0)).addPerson(
						(AndreSharedWaiterRole) subRole);
			}
			break;
		case 1: // chase
			if (mWaiterType == 1) {
				subRole = new CwagonerWaiterRole(super.mPerson);
				((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).addPerson(subRole);
			}
			else if (mWaiterType == 0) {
				subRole = new CwagonerSharedWaiterRole(super.mPerson);
				((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).addPerson(subRole);
			}
			break;
		case 2:
			if (mWaiterType == 1) {//JERRY: shouldn't you use citypanel.masterRestaurantList.get(2)?
									// This statically adds a waiter to your rest.panel class
									// but not to the masterRestaurantList (is taken care of elsewhere?)
				subRole = new JerrywebWaiterRole(super.mPerson);
				JerrywebRestaurantPanel.addWaiter((JerrywebWaiterRole) subRole);
			} else if (mWaiterType == 0) {
				subRole = new JerrywebRSWaiterRole(super.mPerson);
				JerrywebRestaurantPanel
						.addRSWaiter((JerrywebRSWaiterRole) subRole);
			}
			break;
		case 3: // maggi
			if (mWaiterType == 1) {
				subRole = new MaggiyanWaiterRole(super.mPerson);
				((MaggiyanRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(3)).addWaiter(
						(MaggiyanWaiterRole) subRole);
			} else if (mWaiterType == 2) {
				subRole = new MaggiyanSharedWaiterRole(super.mPerson);
				((MaggiyanRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(3)).addSharedWaiter(
						(MaggiyanSharedWaiterRole) subRole);
			}
			break;
		case 4: // david
			if (mWaiterType == 1) {
				subRole = new DavidWaiterRole(super.mPerson);
				((DavidRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(4)).addWaiter(
						(DavidWaiterRole) subRole);
			} else if (mWaiterType == 2) {
				subRole = new DavidWaiterRoleShared(super.mPerson);
				((DavidRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(4)).addSharedWaiter(
						(DavidWaiterRoleShared) subRole);
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
			if (mWaiterType == 1) {
				subRole = new TranacRestaurantWaiterRole(mPerson);
				((TranacRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(6)).addWaiter(
						(TranacRestaurantWaiterRole) subRole);
			} else if (mWaiterType == 0) {
				// ANGELICA: add shared waiter
			}
			break;
		case 7: // rex
			// REX: what is going on with this stuff? Can it go inside your
			// RestaurantPanel?
			if (mWaiterType == 1) {
				RexWaiterRole1 temp = new RexWaiterRole1(
						((RexAnimationPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(7)));
				temp.setHost(RexAnimationPanel.getHost());
				temp.setCook(RexAnimationPanel.getCook());
				temp.setCashier(RexAnimationPanel.getCashier());
				subRole = temp;
				subRole.setPerson(super.mPerson);
				RexAnimationPanel.addPerson((RexWaiterRole1) subRole);
				// adds to host waiter list in addPerson
			} else if (mWaiterType == 0) {
				// REX: add shared waiter
			}
			break;
		}

	}

	public boolean pickAndExecuteAnAction() {
		// System.out.println("RestaurantWaiterRole pAEA run");
		return subRole.pickAndExecuteAnAction();
	}

	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
	}
}
