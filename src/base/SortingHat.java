package base;

import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;
import housing.roles.HousingRenterRole;
import intermediate.RestaurantCashierRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	static Map<Role, Location> sRoleLocations; //list of roles (pointer to that in contact list)
	static List<Map<Role, Boolean>> sRolesFilled;
	
	public static void InstantiateBaseRoles(){
		sRoleLocations = ContactList.sRoleLocations;
		sRolesFilled = new ArrayList<Map<Role, Boolean>>();
		
		//Bank
		BankMasterTellerRole bankMasterTellerRole = new BankMasterTellerRole(null);
		sRoleLocations.put(bankMasterTellerRole, ContactList.cBANK_LOCATION);
		BankGuardRole bankGuardRole = new BankGuardRole(null);
		sRoleLocations.put(bankGuardRole, ContactList.cBANK_LOCATION);
		BankTellerRole bankTellerRole = new BankTellerRole(null); //REX: How many tellers do we need? -SHANE
		sRoleLocations.put(bankTellerRole, ContactList.cBANK_LOCATION);
		
		//Market
		MarketCashierRole marketCashierRole = new MarketCashierRole(null);
		sRoleLocations.put(marketCashierRole, ContactList.cMARKET_LOCATION);
		MarketCookCustomerRole marketCookCustomerRole = new MarketCookCustomerRole(null); //ANGELICA: Is this just the restaurant cook?
		sRoleLocations.put(marketCookCustomerRole, ContactList.cMARKET_LOCATION);
		MarketDeliveryTruckRole marketDeliveryTruckRole = new MarketDeliveryTruckRole(null);
		sRoleLocations.put(marketDeliveryTruckRole, ContactList.cMARKET_LOCATION);
		MarketWorkerRole marketWorkerRole = new MarketWorkerRole(null);//? ANGELICA: How many do we need?
		sRoleLocations.put(marketWorkerRole, ContactList.cMARKET_LOCATION);

		//Restaurants
		
		
		//Create roles filled matrix
		for (int i = 0; i < 3; i++){
			Map<Role, Boolean> shiftRoles = new HashMap<Role, Boolean>();
			
			//Bank
			shiftRoles.put(bankGuardRole, false);
			shiftRoles.put(bankMasterTellerRole, false);
			shiftRoles.put(bankTellerRole, false);
			
			//Market
			
			shiftRoles.put(marketCashierRole, false);
			shiftRoles.put(marketCookCustomerRole, false);
			shiftRoles.put(marketDeliveryTruckRole, false);
			shiftRoles.put(marketWorkerRole, false);
			
			//Restaurants
			
			sRolesFilled.add(shiftRoles);
		}
		
		//SHANE: Add locations to contact list
		
		
	}
	
	//BANK
	public static Role getBankRole(int shift) {
		Map<Role, Boolean> shiftRoles = sRolesFilled.get(shift);
		
		//Master Teller (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof BankMasterTellerRole){ //find role
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true); //fill it
					return (BankMasterTellerRole) iRole; //return role
				}
			}
		}
		//Guard (1) - second priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof BankGuardRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (BankGuardRole) iRole;
				}
			}
		}
		//Teller (limited) - third priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof BankTellerRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (BankTellerRole) iRole;
				}
			}
		}
		
		return null;
	}
	
	
	//MARKET
	public static Role getMarketRole(int shift){
		Map<Role, Boolean> shiftRoles = sRolesFilled.get(shift);
		
		//MarketCashierRole (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof MarketCashierRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (MarketCashierRole) iRole;
				}
			}
		}
		//MarketCookCustomerRole (1)
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof MarketCookCustomerRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (MarketCookCustomerRole) iRole;
				}
			}
		}
		//MarketDeliveryTruckRole
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof MarketDeliveryTruckRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (MarketDeliveryTruckRole) iRole;
				}
			}
		}
		//MarketWorkerRole
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof MarketWorkerRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (MarketWorkerRole) iRole;
				}
			}
		}
		
		return null;
	}
	
	//RESTAURANTS
	static int sRestaurantAssignment = 0; //0-7 for 8 restaurants
	
	public static Role getRestaurantRole(int shift){
		Map<Role, Boolean> shiftRoles = sRolesFilled.get(shift);
		
		//SHANE REX: 1 DO THIS WHEN YOU FINISH THE RESTAURANT ROLES
		
		return new MarketWorkerRole(); //holding place
	}
	
	
	
	

	//HOUSING
	static int sLandlordCount = 0;
	static int sRenterCount = 0;
	static final int sHouseSize = 5;
	static final int sMaxLandlords = 5;
	static final int sMaxRenters = sMaxLandlords*sHouseSize;

	public static Role getHousingRole(Person person) {
		//landlord, renter, owner (in that order)
		
		if (sLandlordCount < sMaxLandlords){
			sLandlordCount++;
			return new HousingLandlordRole(person);
		}
		
		if (sRenterCount < sMaxRenters){
			sRenterCount++;
			return new HousingRenterRole(person);
		}
		return new HousingOwnerRole(person);
	}

	
}
