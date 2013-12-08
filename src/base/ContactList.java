package base;

import housing.House;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.Market;
import restaurant.restaurant_davidmca.DavidRestaurant;
import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_jerryweb.JerrywebRestaurant;
import restaurant.restaurant_smileham.SmilehamRestaurant;
import restaurant.restaurant_tranac.TranacRestaurant;
import transportation.TransportationBus;
import bank.Bank;
import bank.interfaces.BankMasterTeller;
import base.interfaces.Person;

public class ContactList {
	
	//------------------------------------------------------PROJECT SETTINGS----------------------------------------------------------
	
	//Variables
	public static final int cNumTimeShifts = 1;
	public static final int cNumBanks = 2;
	public static final int cNumMarkets = 2;
	public static final int cNumRestaurants = 8;
	public static final int cNumHouses = 80;
	public static final double rent = 10;
	
	//Restaurants (Decoupled data)
	public static DavidRestaurant DavidRestaurant;
	public static TranacRestaurant TranacRestaurant;
	public static JerrywebRestaurant JerrywebRestaurant;
	public static AndreRestaurant AndreRestaurant;
	public static SmilehamRestaurant SmilehamRestaurant;
	
	//Setup
	public static void setup(){
		//Banks
		for (int iBankNum = 0; iBankNum < cNumBanks; iBankNum++){
			sBankList.add(new Bank(iBankNum));
		}
		
		//Markets
		for (int iMarketNum = 0; iMarketNum < cNumMarkets; iMarketNum ++){
			sMarketList.add(new Market(iMarketNum));
		}
		//Houses
		for (int iHouseNum = 0; iHouseNum < cNumHouses; iHouseNum++){
			sHouseList.add(new House(iHouseNum, rent));
		}
		
		// Restaurants
		DavidRestaurant = new DavidRestaurant();
		TranacRestaurant = new TranacRestaurant();
		JerrywebRestaurant = new JerrywebRestaurant();
		AndreRestaurant = new AndreRestaurant();
		SmilehamRestaurant = new SmilehamRestaurant();
	}
	
	//----------------------------------------------------------LISTS----------------------------------------------------------
	//List of all people
	public static List<Person> sPersonList = Collections.synchronizedList(new ArrayList<Person>());
	
	//Decoupled data
	public static List<Market> sMarketList = Collections.synchronizedList(new ArrayList<Market>());
	public static List<House> sHouseList = Collections.synchronizedList(new ArrayList<House>());
	public static List<Bank> sBankList = Collections.synchronizedList(new ArrayList<Bank>());
	
	//Other people/things
	public static TransportationBus cBus;
	public static BankMasterTeller masterTeller;
	
	//Map of open places
	public static Map<Location, Boolean> sOpenPlaces = Collections.synchronizedMap(new HashMap<Location, Boolean>());
	
	//----------------------------------------------------------LOCATIONS----------------------------------------------------------
	
	//GRID LOCATIONS AND INTERFACE (Used here and in B*)
	public static int cGRID_POINT1 = 100;
	public static int cGRID_POINT2 = 180;
	public static int cGRID_POINT3 = 200;
	public static int cGRID_POINT4 = 280;
	public static int cGRID_POINT5 = 320;
	public static int cGRID_POINT6 = 400;
	public static int cGRID_POINT7 = 420;
	public static int cGRID_POINT8 = 500;
	
	public static Location gridLocation(int x, int y){
		assert ((x < 4) && (y < 4));
		int xdim = 0;
		switch (x){
			case 0: xdim = cGRID_POINT1;
				break;
			case 1: xdim = cGRID_POINT3;
				break;
			case 2: xdim = cGRID_POINT5;
				break;
			case 3: xdim = cGRID_POINT7;
				break;
		}
		
		int ydim = 0;
		switch (y){
			case 0: ydim = cGRID_POINT1;
				break;
			case 1: ydim = cGRID_POINT3;
				break;
			case 2: ydim = cGRID_POINT5;
				break;
			case 3: ydim = cGRID_POINT7;
				break;
		}
		
		return new Location(xdim, ydim);
	}
	
	public static Location getDoorLocation(Location location){
		return new Location(location.mX - 5, location.mY - 5);
	}
	
	
	
	//PERSON CORNERS
	// 0 1 
	// 3 2
	public static List<Location> cPERSONCORNERS;
	public static final Location cPERSONCORNER0 = new Location(cGRID_POINT1-5, cGRID_POINT1-5); //95, 95
	public static final Location cPERSONCORNER1 = new Location(cGRID_POINT8, cGRID_POINT1-5);	//500, 95
	public static final Location cPERSONCORNER2 = new Location(cGRID_POINT8, cGRID_POINT8);		//500, 500
	public static final Location cPERSONCORNER3 = new Location(cGRID_POINT1-5, cGRID_POINT8);	//95, 500
	static{
		List<Location> list = new ArrayList<Location>();
		list.add(cPERSONCORNER0);
		list.add(cPERSONCORNER1);
		list.add(cPERSONCORNER2);
		list.add(cPERSONCORNER3);
		cPERSONCORNERS = Collections.unmodifiableList(list);
	}
	
	//PARKING LOTS
	// 0 1 
	// 3 2
	public static final Location cPARKINGLOT0 = new Location(cGRID_POINT1-10, cGRID_POINT1-10); //90, 90
	public static final Location cPARKINGLOT1 = new Location(cGRID_POINT8, cGRID_POINT1-10);	//500, 90
	public static final Location cPARKINGLOT2 = new Location(cGRID_POINT8, cGRID_POINT8);		//500, 500
	public static final Location cPARKINGLOT3 = new Location(cGRID_POINT1-10, cGRID_POINT8);	//90, 500
	public static List<Location> cPARKINGLOTS;
	static {
		List<Location> list = new ArrayList<Location>();
		list.add(cPARKINGLOT0);
		list.add(cPARKINGLOT1);
		list.add(cPARKINGLOT2);
		list.add(cPARKINGLOT3);
		cRESTAURANT_LOCATIONS = Collections.unmodifiableList(list);
	}
	
	
	//INTERSECTION BLOCKS
	public static List<Block> cINTERSECTIONBLOCKS;
	public static final Block cINTERSECTIONBLOCK0 = new Block(280, 35, 320, 85);
	public static final Block cINTERSECTIONBLOCK1 = new Block(280, 280, 320, 320);
	public static final Block cINTERSECTIONBLOCK2 = new Block(35, 280, 85, 320);
	public static final Block cINTERSECTIONBLOCK3 = new Block(515, 280, 565, 320);
	public static final Block cINTERSECTIONBLOCK4 = new Block(280, 515, 320, 565);
	static{
		List<Block> list = new ArrayList<>();
		list.add(cINTERSECTIONBLOCK0);
		list.add(cINTERSECTIONBLOCK1);
		list.add(cINTERSECTIONBLOCK2);
		list.add(cINTERSECTIONBLOCK3);
		list.add(cINTERSECTIONBLOCK4);
		cINTERSECTIONBLOCKS = Collections.unmodifiableList(list);
	}
	
	
	//PERSON B* BLOCKS
	public static List<Block> cPERSONBLOCKS;
	public static final Block cPERSONBLOCK0  = new Block(cGRID_POINT1, cGRID_POINT1, cGRID_POINT2, cGRID_POINT2);
	public static final Block cPERSONBLOCK1  = new Block(cGRID_POINT3, cGRID_POINT1, cGRID_POINT4, cGRID_POINT2);
	public static final Block cPERSONBLOCK2  = new Block(cGRID_POINT5, cGRID_POINT1, cGRID_POINT6, cGRID_POINT2);
	public static final Block cPERSONBLOCK3  = new Block(cGRID_POINT7, cGRID_POINT1, cGRID_POINT8, cGRID_POINT2);
	public static final Block cPERSONBLOCK4  = new Block(cGRID_POINT1, cGRID_POINT3, cGRID_POINT2, cGRID_POINT4);
	public static final Block cPERSONBLOCK5  = new Block(cGRID_POINT3, cGRID_POINT3, cGRID_POINT4, cGRID_POINT4);
	public static final Block cPERSONBLOCK6  = new Block(cGRID_POINT5, cGRID_POINT3, cGRID_POINT6, cGRID_POINT4);
	public static final Block cPERSONBLOCK7  = new Block(cGRID_POINT7, cGRID_POINT3, cGRID_POINT8, cGRID_POINT4);
	public static final Block cPERSONBLOCK8  = new Block(cGRID_POINT1, cGRID_POINT5, cGRID_POINT2, cGRID_POINT6);
	public static final Block cPERSONBLOCK9  = new Block(cGRID_POINT3, cGRID_POINT5, cGRID_POINT4, cGRID_POINT6);
	public static final Block cPERSONBLOCK10 = new Block(cGRID_POINT5, cGRID_POINT5, cGRID_POINT6, cGRID_POINT6);
	public static final Block cPERSONBLOCK11 = new Block(cGRID_POINT7, cGRID_POINT5, cGRID_POINT8, cGRID_POINT6);
	public static final Block cPERSONBLOCK12 = new Block(cGRID_POINT1, cGRID_POINT7, cGRID_POINT2, cGRID_POINT8);
	public static final Block cPERSONBLOCK13 = new Block(cGRID_POINT3, cGRID_POINT7, cGRID_POINT4, cGRID_POINT8);
	public static final Block cPERSONBLOCK14 = new Block(cGRID_POINT5, cGRID_POINT7, cGRID_POINT6, cGRID_POINT8);
	public static final Block cPERSONBLOCK15 = new Block(cGRID_POINT7, cGRID_POINT7, cGRID_POINT8, cGRID_POINT8);
	static{
		List<Block> list = new ArrayList<Block>();
		list.add(cPERSONBLOCK0);
		list.add(cPERSONBLOCK1);
		list.add(cPERSONBLOCK2);
		list.add(cPERSONBLOCK3);
		list.add(cPERSONBLOCK4);
		list.add(cPERSONBLOCK5);
		list.add(cPERSONBLOCK6);
		list.add(cPERSONBLOCK7);
		list.add(cPERSONBLOCK8);
		list.add(cPERSONBLOCK9);
		list.add(cPERSONBLOCK10);
		list.add(cPERSONBLOCK11);
		list.add(cPERSONBLOCK12);
		list.add(cPERSONBLOCK13);
		list.add(cPERSONBLOCK14);
		list.add(cPERSONBLOCK15);
		cPERSONBLOCKS = Collections.unmodifiableList(list);
	}
	
	
	//CAR B* BLOCKS
	public static List<List<Block>> cCARBLOCKS;
	//Not needed =    Paths: AD, DC, CB, BA 
	//cCARBLOCKS[0] = Paths: AB, BC, CD, DA
	public static final Block cCARBLOCK0 = new Block(100, 500, 80, 520);
	public static final Block cCARBLOCK1 = new Block(80, 520, 100, 500);
	//cCARBLOCKS[1] = Paths: BD, DB
	public static final Block cCARBLOCK2 = new Block(90, 280, 90, 300);
	public static final Block cCARBLOCK3 = new Block(90, 300, 90, 280);
	public static final Block cCARBLOCK4 = new Block (300, 500, 320, 500); 
	public static final Block cCARBLOCK5 = new Block (320, 500, 300, 500); 
	//cCARBLOCKS[2] = Paths: AC, CA
	public static final Block cCARBLOCK6 = new Block (100, 280, 300, 500); 
	public static final Block cCARBLOCK7 = new Block (100, 300, 320, 500); 
	public static final Block cCARBLOCK8 = new Block (300, 500, 100, 280); 
	public static final Block cCARBLOCK9 = new Block (320, 500, 100, 300); 
	static{
		List<List<Block>> list = new ArrayList<List<Block>>();
		List<Block> list0 = new ArrayList<Block>();
		List<Block> list1 = new ArrayList<Block>();
		List<Block> list2 = new ArrayList<Block>();
		list0.add(cCARBLOCK0);
		list0.add(cCARBLOCK1);
		list1.add(cCARBLOCK2);
		list1.add(cCARBLOCK3);
		list1.add(cCARBLOCK4);
		list1.add(cCARBLOCK5);
		list2.add(cCARBLOCK6);
		list2.add(cCARBLOCK7);
		list2.add(cCARBLOCK8);
		list2.add(cCARBLOCK9);
		list.add(list0);
		list.add(list1);
		list.add(list2);
		cCARBLOCKS = Collections.unmodifiableList(list);
	}
	
	
	//BANKS, MARKETS
	public static final Location cBANK1_LOCATION = gridLocation(1, 1);
	public static final Location cBANK2_LOCATION = gridLocation(2, 1);
	public static final Location cMARKET1_LOCATION = gridLocation(1, 2);
	public static final Location cMARKET2_LOCATION = gridLocation(2, 2);
	
	
	//RESTAURANTS
	public static List<Location> cRESTAURANT_LOCATIONS;
	static final Location cRESTAURANT_LOCATION1 = gridLocation(1, 0); //aduvoisin	0
	static final Location cRESTAURANT_LOCATION2 = gridLocation(2, 0); //cwagoner	1
	static final Location cRESTAURANT_LOCATION3 = gridLocation(1, 3); //jerrywebb	2
	static final Location cRESTAURANT_LOCATION4 = gridLocation(2, 3); //maggiyang	3
	static final Location cRESTAURANT_LOCATION5 = gridLocation(0, 1); //davidmca	4
	static final Location cRESTAURANT_LOCATION6 = gridLocation(0, 2); //smileham	5
	static final Location cRESTAURANT_LOCATION7 = gridLocation(3, 1); //tranac		6
	static final Location cRESTAURANT_LOCATION8 = gridLocation(3, 2); //xurex		7
	static {
		List<Location> rlist = new ArrayList<Location>();
		rlist.add(cRESTAURANT_LOCATION1);
		rlist.add(cRESTAURANT_LOCATION2);
		rlist.add(cRESTAURANT_LOCATION3);
		rlist.add(cRESTAURANT_LOCATION4);
		rlist.add(cRESTAURANT_LOCATION5);
		rlist.add(cRESTAURANT_LOCATION6);
		rlist.add(cRESTAURANT_LOCATION7);
		rlist.add(cRESTAURANT_LOCATION8);
		cRESTAURANT_LOCATIONS = Collections.unmodifiableList(rlist);
	}
	
	//BUS STOPS
	public static List<Location> cBUS_STOPS;
	static final Location cBusStop0 = new Location(60, 60);
	static final Location cBusStop1 = new Location(515, 60);
	static final Location cBusStop2 = new Location(515, 515);
	static final Location cBusStop3 = new Location(60, 515);
	static {
		List<Location> bslist = new ArrayList<Location>();
		bslist.add(cBusStop0);
		bslist.add(cBusStop1);
		bslist.add(cBusStop2);
		bslist.add(cBusStop3);
		cBUS_STOPS = Collections.unmodifiableList(bslist);

		cBus = new TransportationBus();
	}
	
	//HOUSES
	public static List<Location> cHOUSE_LOCATIONS;
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
	
	//Workplace Availability
	static{
		sOpenPlaces.put(cBANK1_LOCATION, true);
		sOpenPlaces.put(cBANK2_LOCATION, true);
		sOpenPlaces.put(cMARKET1_LOCATION, true);
		sOpenPlaces.put(cMARKET2_LOCATION, true);
		synchronized(cRESTAURANT_LOCATIONS){
			for(Location iLocation : cRESTAURANT_LOCATIONS){
				sOpenPlaces.put(iLocation, true);
			}
		}
	}
	
		
	
	//----------------------------------------------------------OTHER----------------------------------------------------------
		
	public static void SendPayment(int senderSSN, int receiverSSN, double amount){
		masterTeller.msgSendPayment(senderSSN, receiverSSN, amount);
	}
	//REX: 5 change list iteration and put bank master teller outside
}
