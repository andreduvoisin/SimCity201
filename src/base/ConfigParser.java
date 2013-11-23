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

	public void readFileCreatePersons() throws FileNotFoundException {
		Scanner scanFile = new Scanner(getClass().getResourceAsStream("/config.txt"));
		CityPanel citypanel = CityPanel.getInstanceOf();
		
		while (scanFile.hasNext()) {
			//Order of Inputs: Job Place, Cash, Name
			Scanner scanPerson = new Scanner(scanFile.nextLine()); //separate by person
			
			String jobString = scanPerson.next();
			EnumJobType jobPlace = EnumJobType.valueOf(jobString);
			
			String cashString = scanPerson.next();
			double cash = Double.valueOf(cashString);
			
			String name = scanPerson.next();
			
			Person person = new PersonAgent(jobPlace, cash, name); //adds role automatically
			
			synchronized (person) {
				citypanel.masterPersonList.add(person);
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
