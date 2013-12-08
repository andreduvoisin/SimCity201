package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.DavidRestaurant;
import restaurant.restaurant_davidmca.roles.DavidHostRole;
import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_duvoisin.roles.AndreHostRole;
import restaurant.restaurant_jerryweb.JerrywebHostRole;
import restaurant.restaurant_jerryweb.JerrywebRestaurant;
import restaurant.restaurant_maggiyan.gui.MaggiyanAnimationPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanHostRole;
import restaurant.restaurant_smileham.SmilehamRestaurant;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.roles.TranacHostRole;
import restaurant.restaurant_xurex.RexHostRole;
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
			subRole = new AndreHostRole(super.mPerson);
			if(AndreRestaurant.host == null) {
				AndreRestaurant.host = (AndreHostRole) subRole;
			} else {
				subRole = AndreRestaurant.host;
			}
			break;
//		case 1: // chase
//			subRole = ((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).host;
//			subRole.setPerson(super.mPerson);
//			break;
		case 2: // jerry
			subRole = new JerrywebHostRole(super.mPerson);
			JerrywebRestaurant.host = (JerrywebHostRole) subRole;
			break;
		case 3: // maggi
			subRole = new MaggiyanHostRole(super.mPerson);
			MaggiyanAnimationPanel.addPerson((MaggiyanHostRole) subRole);
			break;
		case 4: // david
			subRole = new DavidHostRole(super.mPerson);
			if (DavidRestaurant.host == null) {
				DavidRestaurant.host = (DavidHostRole) subRole;
			} else {
				subRole = DavidRestaurant.host;
			}
			break;
		case 5: // shane
			subRole = new SmilehamHostRole(super.mPerson);
			if (SmilehamRestaurant.mHost == null) {
				SmilehamRestaurant.addPerson((SmilehamHostRole) subRole);
			} else {
				subRole = SmilehamRestaurant.mHost;
			}
			break;
		case 6: // angelica
			subRole = new TranacHostRole(mPerson);
			TranacRestaurant.addPerson((TranacHostRole)subRole);
			break;
		case 7: // rex
			subRole = new RexHostRole(super.mPerson);
			RexAnimationPanel.addPerson((RexHostRole)subRole);
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
