package base;

import market.roles.MarketWorkerRole;
import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;
import housing.roles.HousingRenterRole;
import bank.roles.BankTellerRole;
import base.interfaces.Role;

public class SortingHat {
	
	
	//BANK


	public static Role getBankRole() {
		//SHANE: 1 do this
		Role role = new BankTellerRole();
		return role;
	}
	
	
	//HOUSING

	static int landlord_count = 0;
	static int renter_count = 0;
	final static int max_landlords = 5;
	final static int max_renters = 5;

	public static Role getHousingRole() {
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
	
	
	//MARKET
	
	public static Role getMarketRole(){
		// SHANE: ADD GETNEXTROLE METHODS
		Role worker = new MarketWorkerRole();
		return worker;
	}

}
