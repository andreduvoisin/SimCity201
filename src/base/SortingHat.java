package base;

import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;
import housing.roles.HousingRenterRole;

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
	static List<List<Role>> sRoles; //list for each timeshift (0-2)
	
	public static void InstantiateBaseRoles(){
		sRoles = new ArrayList<List<Role>>();
		
		for (int i = 0; i < 3; i++){
			ArrayList<Role> shift = new ArrayList<Role>();
			
			//Bank
			shift.add(new BankGuardRole(null));
			shift.add(new BankMasterTellerRole(null));
			shift.add(new BankTellerRole(null)); //REX: How many tellers do we need? -SHANE
			
			//Market
			shift.add(new MarketCashierRole(null, new Market())); 	//1 //ANGELICA: Is this how it's created? New market()?
			shift.add(new MarketCookCustomerRole(null));			//1
			shift.add(new MarketDeliveryTruckRole(null));			//1?
			shift.add(new MarketWorkerRole(null));					//? ANGELICA: How many do we need?
			
			//Restaurants
			//REX - ADD RESTAURANT STUFF HERE WHEN DONE
			
			sRoles.add(shift);
		}
		
	}
	
	//BANK
	public static Role getBankRole(int shift) {
		List<Role> shiftRoles = sRoles.get(shift);
		
		//Master Teller (1) - first priority
		for (Role iRole : shiftRoles){
			if (iRole instanceof BankMasterTellerRole){
				if (iRole.getPerson() == null) return (BankMasterTellerRole) iRole;
			}
		}
		//Guard (1) - second priority
		for (Role iRole : shiftRoles){
			if (iRole instanceof BankGuardRole){
				if (iRole.getPerson() == null) return (BankGuardRole) iRole;
			}
		}
		//Teller (limited) - third priority
		for (Role iRole : shiftRoles){
			if (iRole instanceof BankTellerRole){
				if (iRole.getPerson() == null) return (BankTellerRole) iRole;
			}
		}
		
		return null;
	}
	
	
	//MARKET
	public static Role getMarketRole(int shift){
		List<Role> shiftRoles = sRoles.get(shift);
		
		//MarketCashierRole (1) - first priority
		for (Role iRole : shiftRoles){
			if (iRole instanceof MarketCashierRole){
				if (iRole.getPerson() == null) return (MarketCashierRole) iRole;
			}
		}
		//MarketCookCustomerRole (1)
		for (Role iRole : shiftRoles){
			if (iRole instanceof MarketCookCustomerRole){
				if (iRole.getPerson() == null) return (MarketCookCustomerRole) iRole;
			}
		}
		//MarketDeliveryTruckRole
		for (Role iRole : shiftRoles){
			if (iRole instanceof MarketDeliveryTruckRole){
				if (iRole.getPerson() == null) return (MarketDeliveryTruckRole) iRole;
			}
		}
		//MarketWorkerRole
		for (Role iRole : shiftRoles){
			if (iRole instanceof MarketWorkerRole){
				if (iRole.getPerson() == null) return (MarketWorkerRole) iRole;
			}
		}
		
		return null;
	}
	
	//RESTAURANTS
	static int sRestaurantAssignment = 0; //0-7 for 8 restaurants
	
	public static Role getRestaurantRole(int shift){
		List<Role> shiftRoles = sRoles.get(shift);
		
		//SHANE REX: 1 DO THIS WHEN YOU FINISH THE RESTAURANT ROLES
		
		
		
		
		
		//Master Teller (1) - first priority
		for (Role iRole : shiftRoles){
			if (iRole instanceof BankMasterTellerRole){
				if (iRole.getPerson() == null) return (BankMasterTellerRole) iRole;
			}
		}
		
		Person hostPerson = (Person) ContactList.sRestaurantHosts.keySet().toArray()[sRestaurantAssignment];
		sRestaurantAssignment = (sRestaurantAssignment + 1) % ContactList.sRestaurantHosts.size(); //should be mod 8
		
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
