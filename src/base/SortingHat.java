package base;

import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;
import housing.roles.HousingRenterRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.gui.MarketPanel.EnumMarketType;
import market.roles.MarketCashierRole;
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
import city.gui.SimCityGui;

public class SortingHat {
	
	//list of all (non-ubiquitous) roles, accessed and instantiated 
	private static List<Role> sRoles; //list of roles
	static List<Map<Role, Boolean>> sRolesFilled;
	
	static int sNumBankTellers = 1;
	static int sNumMarketWorkers = 2;
	static int sNumRestaurantWaiters = 3;	
	
	public static void InstantiateBaseRoles(){
		sRoles = new ArrayList<Role>();
		sRolesFilled = new ArrayList<Map<Role, Boolean>>();
		
		//Bank
		int numBanks = 2;
		BankMasterTellerRole masterTeller = new BankMasterTellerRole(null);
		sRoles.add(masterTeller);
		ContactList.masterTeller = masterTeller;
		for (int iBankNumber = 0; iBankNumber < numBanks; iBankNumber++){
			sRoles.add(new BankGuardRole(null, iBankNumber));
			for (int iNumBankTellers = 0; iNumBankTellers < sNumBankTellers; iNumBankTellers++){
				sRoles.add(new BankTellerRole(null, iBankNumber));
			}
		}
		
		//Market
		int numMarkets = 2;
		for (int iMarketNumber = 0; iMarketNumber < numMarkets; iMarketNumber++){
			sRoles.add(new MarketCashierRole(null, EnumMarketType.BOTH, iMarketNumber));
			sRoles.add(new MarketDeliveryTruckRole(null, iMarketNumber));
			for (int iNumMarketWorkers = 0; iNumMarketWorkers < sNumMarketWorkers; iNumMarketWorkers++){
				sRoles.add(new MarketWorkerRole(null, iMarketNumber));
			}
		}
		
		//Restaurants
		int numRestaurants = 8;
		int numStart = 0;
		if (SimCityGui.TESTNUM >= 0) {
			numStart = SimCityGui.TESTNUM;
			numRestaurants = numStart + 1;
		}
		for (int iRestaurantNum = numStart; iRestaurantNum < numRestaurants; iRestaurantNum++){
			sRoles.add(new RestaurantHostRole(null, iRestaurantNum));
			sRoles.add(new RestaurantCashierRole(null, iRestaurantNum));
			sRoles.add(new RestaurantCookRole(null, iRestaurantNum));
			for (int iNumRestaurantWaiters = 0; iNumRestaurantWaiters < sNumRestaurantWaiters; iNumRestaurantWaiters++){
				sRoles.add(new RestaurantWaiterRole(null, iRestaurantNum, iNumRestaurantWaiters%2));
			}
		}
		
		//Create roles filled matrix
		for (int i = 0; i < 3; i++){
			Map<Role, Boolean> shiftRoles = new HashMap<Role, Boolean>();
			for (Role iRole : sRoles){
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
		
		//MarketWorkerRole
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof MarketWorkerRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (MarketWorkerRole) iRole;
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
			
			newLandLordRole.setHouse(ContactList.sHouseList.get(sHouseCount));
			sHouseCount++;
			return newLandLordRole;
		}
		
		if (sRenterCount < sMaxRenters){
			sRenterCount++;
			return new HousingRenterRole(person);
		}
		
		HousingOwnerRole newOwnerRole = new HousingOwnerRole(person);
		newOwnerRole.setHouse(ContactList.sHouseList.get(sHouseCount));
		sHouseCount++;
		return newOwnerRole;
	}
}
