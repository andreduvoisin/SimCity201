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
	public static final Location cPERSONCORNER0 = new Location(cGRID_POINT1-5, cGRID_POINT1-5); //95, 95 DAVID these may be wrong
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
	public static final Location cPARKINGLOT0 = new Location(cGRID_POINT1-20, cGRID_POINT1-20); //90, 90 DAVID these may be wrong
	public static final Location cPARKINGLOT1 = new Location(cGRID_POINT8, cGRID_POINT1-20);	//500, 90
	public static final Location cPARKINGLOT2 = new Location(cGRID_POINT8, cGRID_POINT8);		//500, 500
	public static final Location cPARKINGLOT3 = new Location(cGRID_POINT1-20, cGRID_POINT8);	//90, 500
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
	public static final Block cINTERSECTIONBLOCK0 = new Block(280 - 15,	35 - 15, 320 + 15, 85 + 15);
	public static final Block cINTERSECTIONBLOCK1 = new Block(280 - 15,	280 - 15, 320 + 15, 320 + 15);
	public static final Block cINTERSECTIONBLOCK2 = new Block(35 - 15,	280 - 15, 85 + 15, 320 + 15);
	public static final Block cINTERSECTIONBLOCK3 = new Block(515 - 15,	280 - 15, 565 + 15, 320 + 15);
	public static final Block cINTERSECTIONBLOCK4 = new Block(280 - 15,	515 - 15, 320 + 15, 565 + 15);
	static{
		List<Block> list = new ArrayList<>();
		list.add(cINTERSECTIONBLOCK0);
		list.add(cINTERSECTIONBLOCK1);
		list.add(cINTERSECTIONBLOCK2);
		list.add(cINTERSECTIONBLOCK3);
		list.add(cINTERSECTIONBLOCK4);
		cINTERSECTIONBLOCKS = Collections.unmodifiableList(list);
	}
	
	
	//NAVIGATION B* BLOCKS
	public static List<List<Block>> cNAVBLOCKS;
	
	// 0 1
	// 3 2

	// 0 - Walking
	// 1 - Clockwise
	// 2 - Counterclockwise 
	// 3 - Diagonal NE/SW
	// 4 - Diagonal NW/SE
	
	//cNAVBLOCKS[0] Walking
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

	//cNAVBLOCKS[1] = Clockwise
	public static final Block cCARBLOCK_Clockwise1 = new Block (85, 85, 280, 280); //Upper Left
	public static final Block cCARBLOCK_Clockwise2 = new Block (305, 85, 515, 280); //Upper Right
	public static final Block cCARBLOCK_Clockwise3 = new Block (85, 320, 280, 515); //Lower Left
	public static final Block cCARBLOCK_Clockwise4 = new Block (320, 320, 515, 515); //Lower Right
	
	//cNAVBLOCKS[2] = Counterclockwise 
	public static final Block cCARBLOCK_CounterClockwise1 = new Block(85, 65, 515, 535);
	public static final Block cCARBLOCK_CounterClockwise2 = new Block(65, 100, 535, 515);
	
	//cNAVBLOCKS[3] = Diagonal NE/SW
	public static final Block cCARBLOCK_NESW1 = new Block (85, 85, 300, 280); 
	public static final Block cCARBLOCK_NESW2 = new Block (320, 85, 515, 280); 
	public static final Block cCARBLOCK_NESW3 = new Block (85, 320, 280, 515); 
	public static final Block cCARBLOCK_NESW4 = new Block (300, 320, 515, 515);
	public static final Block cCARBLOCK_NESW5 = new Block (85, 85, 280, 300);
	public static final Block cCARBLOCK_NESW6 = new Block (320, 300, 515, 515);
	
	//cNAVBLOCKS[4] = Diagonal SE
	public static final Block cCARBLOCK_SE1 = new Block(65, 85, 280, 280);
	public static final Block cCARBLOCK_SE2 = new Block(300, 85, 515, 280);
	public static final Block cCARBLOCK_SE3 = new Block(320, 85, 515, 300);
	public static final Block cCARBLOCK_SE4 = new Block(85, 320, 280, 515);
	public static final Block cCARBLOCK_SE5 = new Block(320, 320, 515, 515);
	
	//cNAVBLOCKS[5] = Diagonal NW
	public static final Block cCARBLOCK_NW1 = new Block(85, 85, 280, 280);
	public static final Block cCARBLOCK_NW2 = new Block(305, 85, 515, 280);
	public static final Block cCARBLOCK_NW3 = new Block(85, 300, 280, 515);
	public static final Block cCARBLOCK_NW4 = new Block(85, 320, 300, 515);
	public static final Block cCARBLOCK_NW5 = new Block(320, 320, 535, 515);
	
	// 0 - Walking
	// 1 - Clockwise
	// 2 - Counterclockwise 
	// 3 - Diagonal NE/SW
	// 4 - Diagonal SE
	// 5 - Diagonal NW
	
	static{
		List<List<Block>> blockLists = new ArrayList<List<Block>>();
		List<Block> list0 = new ArrayList<Block>(); //Walking
			list0.add(cPERSONBLOCK0);
			list0.add(cPERSONBLOCK1);
			list0.add(cPERSONBLOCK2);
			list0.add(cPERSONBLOCK3);
			list0.add(cPERSONBLOCK4);
			list0.add(cPERSONBLOCK5);
			list0.add(cPERSONBLOCK6);
			list0.add(cPERSONBLOCK7);
			list0.add(cPERSONBLOCK8);
			list0.add(cPERSONBLOCK9);
			list0.add(cPERSONBLOCK10);
			list0.add(cPERSONBLOCK11);
			list0.add(cPERSONBLOCK12);
			list0.add(cPERSONBLOCK13);
			list0.add(cPERSONBLOCK14);
			list0.add(cPERSONBLOCK15);
			blockLists.add(list0); 
		List<Block> list1 = new ArrayList<Block>(); //Clockwise
			list1.add(cCARBLOCK_Clockwise1);
			list1.add(cCARBLOCK_Clockwise2);
			list1.add(cCARBLOCK_Clockwise3);
			list1.add(cCARBLOCK_Clockwise4);
			blockLists.add(list1); 
		List<Block> list2 = new ArrayList<Block>(); //Counterclockwise
			list2.add(cCARBLOCK_CounterClockwise1);
			list2.add(cCARBLOCK_CounterClockwise2);
			blockLists.add(list2); 
		List<Block> list3 = new ArrayList<Block>(); //Diagonal NE/SW
			list3.add(cCARBLOCK_NESW1);
			list3.add(cCARBLOCK_NESW2);
			list3.add(cCARBLOCK_NESW3);
			list3.add(cCARBLOCK_NESW4);
			list3.add(cCARBLOCK_NESW5);
			list3.add(cCARBLOCK_NESW6);
			blockLists.add(list3); 
		List<Block> list4 = new ArrayList<Block>(); //Diagonal SE
			list4.add(cCARBLOCK_SE1);
			list4.add(cCARBLOCK_SE2);
			list4.add(cCARBLOCK_SE3);
			list4.add(cCARBLOCK_SE4);
			list4.add(cCARBLOCK_SE5);
			blockLists.add(list4); 
		List<Block> list5 = new ArrayList<Block>(); //Diagonal NW
			list5.add(cCARBLOCK_NW1);
			list5.add(cCARBLOCK_NW2);
			list5.add(cCARBLOCK_NW3);
			list5.add(cCARBLOCK_NW4);
			list5.add(cCARBLOCK_NW5);
			blockLists.add(list5);
		cNAVBLOCKS = Collections.unmodifiableList(blockLists);
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

		cBus = new TransportationBus(false);
	}
	
	//HOUSES
	public static List<Location> cHOUSE_LOCATIONS;
	static {
		List<Location> houseList = new ArrayList<Location>();
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
			houseList.add(houseLocation);
		}
		cHOUSE_LOCATIONS = Collections.unmodifiableList(houseList);
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
