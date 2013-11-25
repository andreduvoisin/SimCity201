package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.interfaces.BankMasterTeller;
import base.interfaces.Person;
import base.interfaces.Role;

public class ContactList {
	
	//----------------------------------------------------------PEOPLE----------------------------------------------------------
	public static Map<Role, Location> sRoleLocations = new HashMap<Role, Location>();
	static List<Person> sPeople; //list of people
	
	//----------------------------------------------------------LOCATIONS----------------------------------------------------------
	public static final Location cBANK_LOCATION = new Location(200,100);
	public static final Location cMARKET_LOCATION = new Location(100,400);
	public static final Location cCARDEALERSHIP_LOCATION = new Location(400,400);
	public static List<Location> cHOUSE_LOCATIONS;
	public static List<Location> cRESTAURANT_LOCATIONS;
	
	public static final Location cBANK_DOOR = new Location(200,100);
	public static final Location cMARKET_DOOR = new Location(100,400);
	public static final Location cCARDEALERSHIP_DOOR = new Location(500,500);
	public static List<Location> cHOUSE_DOORS;
	public static List<Location> cRESTAURANT_DOORS;
	
	//SHANE: add house doors
	//setup housing locations
	static {
		List<Location> list = new ArrayList<Location>();
		for (int iHouse = 0 ; iHouse < 80; iHouse++){ //80 Houses
			int xCord, yCord = 0;
			if (iHouse / 20 == 0) {					//North
				xCord = 100 + 20 * (iHouse % 20);
				yCord = 0;
			} else if (iHouse / 20 == 2) {			//South
				xCord = 100 + 20 * (iHouse % 20);
				yCord = 580;
			} else if (iHouse / 20 == 3) {			//West
				xCord = 0;
				yCord = 100 + 20 * (iHouse % 20);
			} else {								//East
				xCord = 580;
				yCord = 100 + 20 * (iHouse % 20);
			}
			Location houseLocation = new Location(xCord, yCord);
			list.add(houseLocation);
		}
		cHOUSE_LOCATIONS = Collections.unmodifiableList(list);
	}
	
	//setup job locations
	static final Location cRESTAURANT_LOCATION1 = new Location(100,100); //aduvoisin	0
	static final Location cRESTAURANT_LOCATION2 = new Location(100,200); //cwagoner		1
	static final Location cRESTAURANT_LOCATION3 = new Location(100,300); //jerrywebb	2
	static final Location cRESTAURANT_LOCATION4 = new Location(215,420); //maggiyang	3
	static final Location cRESTAURANT_LOCATION5 = new Location(310,420); //davidmca		4
	static final Location cRESTAURANT_LOCATION6 = new Location(420,300); //smileham		5
	static final Location cRESTAURANT_LOCATION7 = new Location(420,200); //tranac		6
	static final Location cRESTAURANT_LOCATION8 = new Location(420,100); //xurex		7
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
		BankMasterTeller bankMasterTellerRole = null;
		for (Role iRole : sRoleLocations.keySet()){
			if (iRole instanceof BankMasterTeller){
				bankMasterTellerRole = (BankMasterTeller) iRole;
			}
		}
		bankMasterTellerRole.msgSendPayment(senderSSN, receiverSSN, amount);
	}
	//REX: 5 change list iteration and put bank master teller outside
}
