package base;

import java.io.FileNotFoundException;
import java.util.Scanner;

import base.PersonAgent.EnumJobType;
import base.interfaces.Person;
import city.gui.CityPanel;

/*
 * Reads in a config file filled with new people to instantiate
 * @author David Carr, Shane Mileham
 */
public class ConfigParser {

	private static ConfigParser instance = null;

	public void readFileCreatePersons(CityPanel citypanel) throws FileNotFoundException {
		Scanner scanFile = new Scanner(getClass().getResourceAsStream("/runconfig/config.txt"));
		
		//Instantiate the base roles before creating the people
		SortingHat.InstantiateBaseRoles();
		
		while (scanFile.hasNext()) {
			//Order of Inputs: Job Type (BANK, MARKET, RESTAURANT, NONE), Cash, Name
			Scanner scanPerson = new Scanner(scanFile.nextLine()); //separate by person
			
			//Job
			String jobString = scanPerson.next();
			EnumJobType jobType = null;
			if (jobString.equals("BANK")) {
				jobType = EnumJobType.BANK;
			}
			if (jobString.equals("RESTAURANT")) {
				jobType = EnumJobType.RESTAURANT;
			}
			//EnumJobType jobType = EnumJobType.valueOf(jobString);
			
			//Cash
			String cashString = scanPerson.next();
			double cash = Double.valueOf(cashString);
			
			//Name
			String name = scanPerson.next();
			
			//Person
			Person person = new PersonAgent(jobType, cash, name); //adds role automatically
			synchronized (person) {
				citypanel.masterPersonList.add(person);
//				((PersonAgent) person).startThread();
			}
			
			scanPerson.close();
		}
		scanFile.close();

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
