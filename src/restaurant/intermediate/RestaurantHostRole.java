package restaurant.intermediate;

import city.gui.SimCityGui;
import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_cwagoner.gui.CwagonerRestaurantPanel;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_jerryweb.gui.JerrywebRestaurantPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
import restaurant.restaurant_tranac.gui.TranacRestaurantPanel;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantHostRole extends BaseRole implements
		RestaurantBaseInterface {

	static int totalHosts = 0;
	public Role subRole = null;
	int mRestaurantID;

	public RestaurantHostRole(Person person, int restaurantID){
		super(person);
		this.mRestaurantID = restaurantID;
	}

	public void setPerson(Person person) {

		super.mPerson = person;
		switch (mRestaurantID) {
		case 0: // andre
			subRole = ((AndreRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(0)).host;
			subRole.setPerson(super.mPerson);
			break;
		case 1: // chase
			subRole = ((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).host;
			subRole.setPerson(super.mPerson);
			break;
		case 2: // jerry
			subRole = ((JerrywebRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(2)).host;
			subRole.setPerson(super.mPerson);
			break;
		case 3: // maggi
			subRole = ((MaggiyanRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(3)).host;
			subRole.setPerson(super.mPerson);
			break;
		case 4: // david
			subRole = ((DavidRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(4)).host;
			subRole.setPerson(super.mPerson);
			break;
		case 5: // shane
			subRole = new SmilehamHostRole(super.mPerson);
			SmilehamAnimationPanel.addPerson((SmilehamHostRole) subRole);
			break;
		case 6: // angelica
			subRole = ((TranacRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(6)).mHost;
			subRole.setPerson(mPerson);
			break;
		case 7: // rex
			subRole = ((RexAnimationPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(7)).host;
			subRole.setPerson(super.mPerson);
			break;
		}
	}

	public boolean pickAndExecuteAnAction() {
		// System.out.println("RestaurantHostRole pAEA run");
		return subRole.pickAndExecuteAnAction();
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
	}

}
