package base;

import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;
import housing.roles.HousingRenterRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.roles.MarketCashierRole;
import market.roles.MarketCookCustomerRole;
import market.roles.MarketDeliveryTruckRole;
import market.roles.MarketWorkerRole;
import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import bank.roles.BankGuardRole;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityHousing;
import city.gui.SimCityGui;

public class SortingHat {
	
	//list of all (non-ubiquitous) roles, accessed and instantiated 
	static Map<Role, Location> sRoleLocations; //list of roles (pointer to that in contact list)
	static List<Map<Role, Boolean>> sRolesFilled;
	
	static int sNumBankTellers = 1;
	static int sNumMarketWorkers = 1;
	static int sNumRestaurantWaiters = 1;	
	
	public static void InstantiateBaseRoles(){
		sRoleLocations = ContactList.sRoleLocations;
		sRolesFilled = new ArrayList<Map<Role, Boolean>>();
		
		//Bank
		sRoleLocations.put(new BankMasterTellerRole(null), ContactList.cBANK_LOCATION);
		sRoleLocations.put(new BankGuardRole(null), ContactList.cBANK_LOCATION);
		for (int iNumBankTellers = 0; iNumBankTellers < sNumBankTellers; iNumBankTellers++){
			sRoleLocations.put(new BankTellerRole(null), ContactList.cBANK_LOCATION);
		}
		
		//Market
		sRoleLocations.put(new MarketCashierRole(null), ContactList.cMARKET_LOCATION);
		sRoleLocations.put(new MarketDeliveryTruckRole(null), ContactList.cMARKET_LOCATION);
		for (int iNumMarketWorkers = 0; iNumMarketWorkers < sNumMarketWorkers; iNumMarketWorkers++){
			sRoleLocations.put(new MarketWorkerRole(null), ContactList.cMARKET_LOCATION);
		}

		//Restaurants

		for (int iRestaurantNum = 1; iRestaurantNum < 2; iRestaurantNum++){
			sRoleLocations.put(new RestaurantHostRole(null, iRestaurantNum), ContactList.cRESTAURANT_LOCATIONS.get(iRestaurantNum));
			sRoleLocations.put(new RestaurantCashierRole(null, iRestaurantNum), ContactList.cRESTAURANT_LOCATIONS.get(iRestaurantNum));
			sRoleLocations.put(new RestaurantCookRole(null, iRestaurantNum), ContactList.cRESTAURANT_LOCATIONS.get(iRestaurantNum));
			for (int iNumRestaurantWaiters = 0; iNumRestaurantWaiters < sNumRestaurantWaiters; iNumRestaurantWaiters++){
				sRoleLocations.put(new RestaurantWaiterRole(null, iRestaurantNum), ContactList.cRESTAURANT_LOCATIONS.get(iRestaurantNum));
			}
		}
		
		//Create roles filled matrix
		for (int i = 0; i < 3; i++){
			Map<Role, Boolean> shiftRoles = new HashMap<Role, Boolean>();
			for (Role iRole : sRoleLocations.keySet()){
				shiftRoles.put(iRole, false);
			}
			sRolesFilled.add(shiftRoles);
		}
		
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
	public static Role getRestaurantRole(int shift){
		Map<Role, Boolean> shiftRoles = sRolesFilled.get(shift);
		
		//RestaurantHostRole (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof RestaurantHostRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (RestaurantHostRole) iRole;
				}
			}
		}
		
		//RestaurantCookRole (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof RestaurantCookRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (RestaurantCookRole) iRole;
				}
			}
		}
		
		//RestaurantCashierRole (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof RestaurantCashierRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (RestaurantCashierRole) iRole;
				}
			}
		}
		
		//RestaurantWaiterRole (limited)
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof RestaurantWaiterRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (RestaurantWaiterRole) iRole;
				}
			}
		}
		
		return null;
	}
	
	
	//HOUSING
	static int sLandlordCount = 0;
	static int sRenterCount = 0;
	static int sHouseCount = 0;
	static final int sHouseSize = 5;
	static final int sMaxLandlords = 5;
	static final int sMaxRenters = sMaxLandlords*sHouseSize;

	public static Role getHousingRole(Person person) {
		//landlord, renter, owner (in that order)		

		if (sLandlordCount < sMaxLandlords){
			sLandlordCount++;
			HousingLandlordRole newLandLordRole = new HousingLandlordRole(person);
			sHouseCount++;
			return newLandLordRole;
		}
		
		if (sRenterCount < sMaxRenters){
			sRenterCount++;
			return new HousingRenterRole(person);
		}
		
		HousingOwnerRole newOwnerRole = new HousingOwnerRole(person);
		newOwnerRole.setHouse(SimCityGui.getInstance().citypanel.masterHouseList.get(sHouseCount));
		sHouseCount++;
		return newOwnerRole;
	}

	
}
