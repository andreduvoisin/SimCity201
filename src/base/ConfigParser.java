package base;

import housing.interfaces.Renter;
import housing.roles.RenterRole;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import city.gui.CityPanel;
import base.interfaces.Person;
import base.interfaces.Role;

public class ConfigParser {

	private static ConfigParser instance = null;

	public void readFileCreatePersons() throws FileNotFoundException {
		Scanner scanner = new Scanner(getClass().getResourceAsStream("/config.txt"));
		CityPanel citypanel = CityPanel.getInstanceOf();
		while (scanner.hasNext()) {
			String mName = scanner.next();
			Person newPerson = new PersonAgent("mName");
			Double mStartcash = Double.parseDouble(scanner.next());
			newPerson.setCash(mStartcash);
			String mStartingRole = scanner.next();
			if (mStartingRole.equals("Renter")) {
				Renter newRenterRole = new RenterRole();
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
