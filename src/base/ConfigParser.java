package base;

import housing.House;
import housing.interfaces.Landlord;
import housing.interfaces.Renter;
import housing.roles.LandlordRole;
import housing.roles.RenterRole;

import java.io.FileNotFoundException;
import java.util.Scanner;

import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityPanel;

/*
 * Reads in a config file filled with new people to instantiate
 * @author David Carr
 */
public class ConfigParser {

	private static ConfigParser instance = null;

	public void readFileCreatePersons() throws FileNotFoundException {
		Scanner scanner = new Scanner(getClass().getResourceAsStream("/config.txt"));
		CityPanel citypanel = CityPanel.getInstanceOf();
		while (scanner.hasNext()) {
			String mName = scanner.next();
			Person newPerson = new PersonAgent(mName);
			Double mStartcash = Double.parseDouble(scanner.next());
			newPerson.setCash(mStartcash);
			String mStartingRole = scanner.next();
			if (mStartingRole.equals("Landlord")) {
				LandlordRole newLandlordRole = new LandlordRole();
				for (int i=0; i<4; i++) {
					newLandlordRole.mHousesList.add(new House(5, 5, 60));				
				}
			}
			if (mStartingRole.equals("Renter")) {
				RenterRole newRenterRole = new RenterRole();
				newPerson.addRole((Role) newRenterRole);
			}
			//TODO: add handling for all the other possible roles
			synchronized (newPerson) {
				citypanel.masterPersonList.add(newPerson);
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
