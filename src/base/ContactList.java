package base;

import housing.roles.HousingLandlordRole;

import java.util.Map;

import market.roles.MarketCashierRole;
import bank.roles.BankMasterTellerRole;
import base.interfaces.Person;

public class ContactList {
	static Map<BankMasterTellerRole, Location> sBankMasterTellers;
	static Map<HousingLandlordRole, Location> sHousingLandlords;
	static Map<MarketCashierRole, Location> sMarketCashiers;
	static Map<Person, Location> sRestaurantHosts; //ALL: Make a host interface and implement a restaurant SHANE: 1 TELL PEOPLE TO ADD A L L
	
	//REX ALL: What else do we need here?
	// SHANE: consider above maps with location as a key, instead of value
	// so that we can look up the role-person by location
	// (finding the location of a role isn't as helpful, methinks)
}
