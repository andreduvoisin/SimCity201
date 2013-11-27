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
	public static final Location cBANK_LOCATION = new Location(205,100);
	public static final Location cMARKET_LOCATION = new Location(420,420);
//	public static final Location cCARDEALERSHIP_LOCATION = new Location(100,420);
	public static List<Location> cHOUSE_LOCATIONS;
	public static List<Location> cRESTAURANT_LOCATIONS;
	
	public static final Location cBANK_DOOR = new Location(cBANK_LOCATION.mX-5, cBANK_LOCATION.mY-5);
	public static final Location cMARKET_DOOR = new Location(cMARKET_LOCATION.mX-5, cMARKET_LOCATION.mY-5);
//	public static final Location cCARDEALERSHIP_DOOR = new Location(cCARDEALERSHIP_LOCATION.mX-5,cCARDEALERSHIP_LOCATION.mY-5);
	public static List<Location> cHOUSE_DOORS;
	public static List<Location> cRESTAURANT_DOORS;
	public static List<Location> cBUS_STOPS;
	
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
	
	public static int cGRID_POINT1 = 100;
	public static int cGRID_POINT2 = 180;
	public static int cGRID_POINT3 = 205;
	public static int cGRID_POINT4 = 285;
	public static int cGRID_POINT5 = 315;
	public static int cGRID_POINT6 = 395;
	public static int cGRID_POINT7 = 420;
	public static int cGRID_POINT8 = 500;
	
	//setup job locations
	static final Location cRESTAURANT_LOCATION1 = new Location(cGRID_POINT1,cGRID_POINT1); //aduvoisin	0
	static final Location cRESTAURANT_LOCATION2 = new Location(cGRID_POINT1,cGRID_POINT3); //cwagoner	1
	static final Location cRESTAURANT_LOCATION3 = new Location(cGRID_POINT1,cGRID_POINT5); //jerrywebb	2
	static final Location cRESTAURANT_LOCATION4 = new Location(cGRID_POINT3,cGRID_POINT7); //maggiyang	3
	static final Location cRESTAURANT_LOCATION5 = new Location(cGRID_POINT5,cGRID_POINT7); //davidmca	4
	static final Location cRESTAURANT_LOCATION6 = new Location(cGRID_POINT7,cGRID_POINT5); //smileham	5
	static final Location cRESTAURANT_LOCATION7 = new Location(cGRID_POINT7,cGRID_POINT3); //tranac		6
	static final Location cRESTAURANT_LOCATION8 = new Location(cGRID_POINT7,cGRID_POINT1); //xurex		7
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
	
	static final Location cRESTAURANT_DOOR1 = new Location(cGRID_POINT1-5,cGRID_POINT1-5); //aduvoisin	0
	static final Location cRESTAURANT_DOOR2 = new Location(cGRID_POINT1-5,cGRID_POINT3-5); //cwagoner	1
	static final Location cRESTAURANT_DOOR3 = new Location(cGRID_POINT1-5,cGRID_POINT5-5); //jerrywebb	2
	static final Location cRESTAURANT_DOOR4 = new Location(cGRID_POINT3-5,cGRID_POINT7-5); //maggiyang	3
	static final Location cRESTAURANT_DOOR5 = new Location(cGRID_POINT5-5,cGRID_POINT7-5); //davidmca	4
	static final Location cRESTAURANT_DOOR6 = new Location(cGRID_POINT7-5,cGRID_POINT5-5); //smileham	5
	static final Location cRESTAURANT_DOOR7 = new Location(cGRID_POINT7-5,cGRID_POINT3-5); //tranac		6
	static final Location cRESTAURANT_DOOR8 = new Location(cGRID_POINT7-5,cGRID_POINT1-5); //xurex		7
	static {
		List<Location> list = new ArrayList<Location>();
		list.add(cRESTAURANT_DOOR1);
		list.add(cRESTAURANT_DOOR2);
		list.add(cRESTAURANT_DOOR3);
		list.add(cRESTAURANT_DOOR4);
		list.add(cRESTAURANT_DOOR5);
		list.add(cRESTAURANT_DOOR6);
		list.add(cRESTAURANT_DOOR7);
		list.add(cRESTAURANT_DOOR8);
		cRESTAURANT_DOORS = Collections.unmodifiableList(list);
	}

	// Bus Stop locations
	static final Location cBusStop0 = new Location(60, 60);
	static final Location cBusStop1 = new Location(60, 515);
	static final Location cBusStop2 = new Location(515, 515);
	static final Location cBusStop3 = new Location(515, 60);
	static {
		List<Location> bslist = new ArrayList<Location>();
		bslist.add(cBusStop0);
		bslist.add(cBusStop1);
		bslist.add(cBusStop2);
		bslist.add(cBusStop3);
		cBUS_STOPS = Collections.unmodifiableList(bslist);
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
