package base;

import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;

import java.util.ArrayList;
import java.util.List;

import market.Market;
import market.roles.MarketCashierRole;
import market.roles.MarketCookCustomerRole;
import market.roles.MarketDeliveryTruckRole;
import market.roles.MarketWorkerRole;
import bank.roles.BankGuardRole;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class SortingHat {
	
	//list of all (non-ubiquitous) roles, accessed and instantiated 
	static List<List<Role>> sRoles;
	
	public static void InstantiateBaseRoles(){
		sRoles = new ArrayList<List<Role>>();
		
		for (int i = 0; i < 3; i++){
			ArrayList<Role> shift = new ArrayList<Role>();
			
			//Bank
			shift.add(new BankGuardRole(null));
			shift.add(new BankMasterTellerRole(null));
			shift.add(new BankTellerRole(null));
			
			//Housing
			shift.add(new HousingLandlordRole(null));
			shift.add(new HousingOwnerRole(null));
			
			//Market
			shift.add(new MarketCashierRole(null, new Market())); //ANGELICA: Is this how it's created? New market()?
			shift.add(new MarketCookCustomerRole(null));
			shift.add(new MarketDeliveryTruckRole(null));
			shift.add(new MarketWorkerRole(null));
			
			//Restaurants
			//REX - ADD RESTAURANT STUFF HERE WHEN DONE
			
			sRoles.add(shift);
		}
		
	}
	
	//BANK
	public static Role getBankRole(int shift) {
		
		
		//SHANE: 1 do this
		//loop through roles and assign them in priority order
		return role;
	}
	
	
	//HOUSING

	static int landlord_count = 0;
	static int renter_count = 0;
	final static int max_landlords = 5;
	final static int max_renters = 5;

	public static Role getHousingRole(int shift) {
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
	
	public static Role getMarketRole(int shift){
		// SHANE: ADD GETNEXTROLE METHODS
		Role worker = new MarketWorkerRole();
		return worker;
	}
	
	//RESTAURANTS
	static int sRestaurantAssignment = 0; //0-7 for 8 restaurants
	
	public static Role getRestaurantRole(int shift){
		Person hostPerson = (Person) ContactList.sRestaurantHosts.keySet().toArray()[sRestaurantAssignment];
		sRestaurantAssignment = (sRestaurantAssignment + 1) % ContactList.sRestaurantHosts.size(); //should be mod 8
		
		return new MarketWorkerRole(); //holding place
	}

	
}
