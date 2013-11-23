package base;

import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;
import housing.roles.HousingRenterRole;

import java.util.List;

import market.Market;
import market.roles.MarketCashierRole;
import market.roles.MarketCookCustomerRole;
import market.roles.MarketCustomerRole;
import market.roles.MarketDeliveryTruckRole;
import market.roles.MarketWorkerRole;
import bank.roles.BankCustomerRole;
import bank.roles.BankGuardRole;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class SortingHat {
	
	//list of all roles, accessed and instantiated 
	static List<Role> sRoles;
	
	static void InstantiateBaseRoles(){
		//Bank
		sRoles.add(new BankCustomerRole(null));
		sRoles.add(new BankGuardRole(null));
		sRoles.add(new BankMasterTellerRole(null));
		sRoles.add(new BankTellerRole(null));
		
		//Housing
		sRoles.add(new HousingLandlordRole(null));
		sRoles.add(new HousingOwnerRole(null));
		sRoles.add(new HousingRenterRole(null));
		
		//Market
		sRoles.add(new MarketCashierRole(null, new Market())); //ANGELICA SHANE REMOVE THIS (MARKET)
		sRoles.add(new MarketCookCustomerRole(null));
		sRoles.add(new MarketCustomerRole(null));
		sRoles.add(new MarketDeliveryTruckRole(null));
		sRoles.add(new MarketWorkerRole(null));
		
		//Restaurants
		//REX - ADD RESTAURANT STUFF HERE WHEN DONE
	}
	
	//BANK


	public static Role getBankRole() {
		//SHANE: 1 do this
		//loop through roles and assign them in priority order
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
//			newRole = new HousingRenterRole();
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
	
	//RESTAURANTS
	static int sRestaurantAssignment = 0; //0-7 for 8 restaurants
	
	public static Role getRestaurantRole(){
		Person hostPerson = (Person) ContactList.sRestaurantHosts.keySet().toArray()[sRestaurantAssignment];
		sRestaurantAssignment = (sRestaurantAssignment + 1) % ContactList.sRestaurantHosts.size(); //should be mod 8
		
		return new MarketWorkerRole(); //holding place
	}

	
}
