package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.interfaces.BankMasterTeller;
import bank.roles.BankMasterTellerRole;
import bank.test.MasterTellerTest;
import base.interfaces.Person;
import base.interfaces.Role;

public class ContactList {
	
	//----------------------------------------------------------PEOPLE----------------------------------------------------------
	public static Map<Role, Location> sRoleLocations = new HashMap<Role, Location>();
	//SHANE: sRestaurantRoleLocations
	static List<Person> sPeople; //list of people
	
//	static Map<BankMasterTellerRole, Location> sBankMasterTellers;
//	static Map<HousingLandlordRole, Location> sHousingLandlords;
//	static Map<MarketCashierRole, Location> sMarketCashiers;
//	static Map<Person, Location> sRestaurantHosts; //REX: 1 Make a host interface and implement a restaurant
	
	
	//----------------------------------------------------------LOCATIONS----------------------------------------------------------
	static final Location cBANK_LOCATION = new Location(10,10); //JERRY: Bank coordinates go here
	static final Location cMARKET_LOCATION = new Location(10,10); //JERRY: Market coordinates
	static List<Location> cHOUSE_LOCATIONS;
	static List<Location> cRESTAURANT_LOCATIONS;
	
	//setup housing locations
	static final Location cHOUSE_LOCATION1 = new Location(10,10); //JERRY: Housing locations go here
	static final Location cHOUSE_LOCATION2 = new Location(10,10);
	static final Location cHOUSE_LOCATION3 = new Location(10,10);
	static final Location cHOUSE_LOCATION4 = new Location(10,10);
	static final Location cHOUSE_LOCATION5 = new Location(10,10);
	static {
		List<Location> list = new ArrayList<Location>();
		list.add(cHOUSE_LOCATION1);
		list.add(cHOUSE_LOCATION2);
		list.add(cHOUSE_LOCATION3);
		list.add(cHOUSE_LOCATION4);
		list.add(cHOUSE_LOCATION5);
		cHOUSE_LOCATIONS = Collections.unmodifiableList(list);
	}
	
	//setup job locations
	static final Location cRESTAURANT_LOCATION1 = new Location(0,0); //JERRY: Restaurant locations go here
	static final Location cRESTAURANT_LOCATION2 = new Location(0,0);
	static final Location cRESTAURANT_LOCATION3 = new Location(0,0);
	static final Location cRESTAURANT_LOCATION4 = new Location(0,0);
	static final Location cRESTAURANT_LOCATION5 = new Location(0,0);
	static final Location cRESTAURANT_LOCATION6 = new Location(0,0);
	static final Location cRESTAURANT_LOCATION7 = new Location(0,0);
	static final Location cRESTAURANT_LOCATION8 = new Location(0,0);
	static {
		List<Location> list = new ArrayList<Location>();
		list.add(cRESTAURANT_LOCATION1);
		list.add(cRESTAURANT_LOCATION2);
		list.add(cRESTAURANT_LOCATION3);
		list.add(cRESTAURANT_LOCATION4);
		list.add(cRESTAURANT_LOCATION5);
		list.add(cRESTAURANT_LOCATION6);
		list.add(cRESTAURANT_LOCATION7);
		list.add(cRESTAURANT_LOCATION8);
		cRESTAURANT_LOCATIONS = Collections.unmodifiableList(list);
	}
	
	//----------------------------------------------------------OTHER----------------------------------------------------------
		
	public static void SendPayment(int senderSSN, int receiverSSN, double amount){
		BankMasterTeller bankMasterTellerRole = new BankMasterTellerRole();
		for (Role iRole : sRoleLocations.keySet()){
			if (iRole instanceof BankMasterTeller){
				bankMasterTellerRole = (BankMasterTeller) iRole;
			}
		}
		bankMasterTellerRole.msgSendPayment(senderSSN, receiverSSN, amount);
	}
	
	
	//REX ALL: What else do we need here? -Shane
	// SHANE: consider above maps with location as a key, instead of value
	// so that we can look up the role-person by location
	// (finding the location of a role isn't as helpful, methinks)
	//^^^Why? Who is this?
}
