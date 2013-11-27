package base;

import java.io.FileNotFoundException;
import java.util.Scanner;

import base.PersonAgent.EnumJobType;
import base.interfaces.Person;
import city.gui.SimCityGui;

/*
 * Reads in a config file filled with new people to instantiate
 * @author David Carr, Shane Mileham
 */
public class ConfigParser {

	private static ConfigParser instance = null;

	public void readFileCreatePersons(SimCityGui simcitygui, String fileName) throws FileNotFoundException {
		Scanner scanFile = new Scanner(getClass().getResourceAsStream("/runconfig/"+fileName));
		
		//Instantiate the base roles before creating the people
		boolean mInstantiateRoles = true;
//		SortingHat.InstantiateBaseRoles();
		
		while (scanFile.hasNext()) {
			//Order of Inputs: Job Type (BANK, MARKET, RESTAURANT, NONE), Cash, Name
			Scanner scanPerson = new Scanner(scanFile.nextLine()); //separate by person
			
			//Job
			String jobString = scanPerson.next();			

//			EnumJobType jobType = null;
//			if (jobString.equals("BANK")) {
//				jobType = EnumJobType.BANK;
//			}
//			if (jobString.equals("RESTAURANT")) {
//				jobType = EnumJobType.RESTAURANT;
//			}
//			if (jobString.equals("NONE")) {
//				jobType = EnumJobType.NONE;
//			}
			EnumJobType jobType = EnumJobType.valueOf(jobString);
			
			if (jobType == EnumJobType.RESTAURANT) {
				String restaurant = scanPerson.next();
				switch (restaurant) {
				case "0":
					SimCityGui.TESTNUM = 0;
					break;
				case "1":
					SimCityGui.TESTNUM = 1;
					break;
				case "2":
					SimCityGui.TESTNUM = 2;
					break;
				case "3":
					SimCityGui.TESTNUM = 3;
					break;
				case "4":
					SimCityGui.TESTNUM = 4;
					break;
				case "5":
					SimCityGui.TESTNUM = 5;
					break;
				case "6":
					SimCityGui.TESTNUM = 6;
					break;
				case "7":
					SimCityGui.TESTNUM = 7;
					break;
				}
			}

			//Cash
			String cashString = scanPerson.next();
			double cash = Double.valueOf(cashString);
			
			//Name
			String name = scanPerson.next();
			
			//Scenario
//			String scenarioString = scanPerson.next();
//			EnumScenarioType scenarioType = EnumScenarioType.valueOf(scenarioString);
			
			//Instantiate Roles
			if (mInstantiateRoles){
				SortingHat.InstantiateBaseRoles();
				mInstantiateRoles = false;
			}
			
			//Person
			Person person = new PersonAgent(jobType, cash, name); //adds role automatically
			synchronized (person) {
				simcitygui.citypanel.masterPersonList.add(person);
				simcitygui.citypanel.addMoving(person.getPersonGui()); //allow to move
				((PersonAgent) person).startThread();
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
