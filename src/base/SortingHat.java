package base;

import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;
import housing.roles.HousingRenterRole;
import base.interfaces.Role;

public class SortingHat {

	static int landlord_count = 0;
	static int renter_count = 0;
	final static int max_landlords = 5;
	final static int max_renters = 5;

	public static Role getNextRole() {
		Role newRole = null;
		if (landlord_count < max_landlords) {
			newRole = new HousingLandlordRole();
		}
		if (renter_count < max_renters) {
			newRole = new HousingRenterRole();
		} else {
			newRole = new HousingOwnerRole();
		}
		return newRole;
	}

}
