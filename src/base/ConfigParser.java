package base;

import housing.House;
import housing.interfaces.Landlord;
import housing.interfaces.Renter;
import housing.roles.LandlordRole;
import housing.roles.RenterRole;

import java.io.FileNotFoundException;
import java.util.Scanner;

import base.PersonAgent.EnumJobPlaces;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityPanel;

/*
 * Reads in a config file filled with new people to instantiate
 * @author David Carr, Shane Mileham
 */
public class ConfigParser {

	private static ConfigParser instance = null;

	public void readFileCreatePersons() throws FileNotFoundException {
		Scanner scanner = new Scanner(getClass().getResourceAsStream("/config.txt"));
		CityPanel citypanel = CityPanel.getInstanceOf();
		
		while (scanner.hasNext()) {
			//Order of Inputs: Job Place, Cash, Name
			Scanner scanPerson = new Scanner(scanner.nextLine()); //separate by person
			
			String jobString = scanPerson.next();
			EnumJobPlaces jobPlace = EnumJobPlaces.valueOf(jobString);
			
			String cashString = scanPerson.next();
			double cash = Double.valueOf(cashString);
			
			String name = scanPerson.next();
			
			Person person = new PersonAgent(); //SHANE: add params after fixing in PersonAgent
			
			
//			if (mStartingRole.equals("Landlord")) {
//				LandlordRole newLandlordRole = new LandlordRole();
//				for (int i=0; i<4; i++) {
//					newLandlordRole.mHousesList.add(new House(5, 5, 60));				
//				}
//			}
//			if (mStartingRole.equals("Renter")) {
//				RenterRole newRenterRole = new RenterRole();
//				newPerson.addRole((Role) newRenterRole, true);
//			}
			//DAVID MAGGI: add handling for all the other possible roles
			synchronized (person) {
				citypanel.masterPersonList.add(person);
			}
		}

	}

	private ConfigParser() {
	}

	public static ConfigParser getInstanceOf() {
		if (instance == null) {
			instance = new ConfigParser();
		}
		return instance;
	}

}
