package base;

import java.io.FileNotFoundException;
import java.util.Scanner;

import base.Event.EnumEventType;
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
//		boolean mInstantiateRoles = true;
		
		SortingHat.InstantiateBaseRoles();
		
		while (scanFile.hasNext()) {
			//Order of Inputs: Job Type (BANK, MARKET, RESTAURANT, NONE), Cash, Name
			Scanner scanPerson = new Scanner(scanFile.nextLine()); //separate by person
			
			//Job
			String jobString = scanPerson.next();	
			EnumJobType jobType = null;
			switch(jobString) {
				case "BANK":
					jobType = EnumJobType.BANK;
					break;
				case "MARKET":
					jobType = EnumJobType.MARKET;
					break;
				case "RESTAURANT":
					jobType = EnumJobType.RESTAURANT;
					break;
				case "NONE":
					jobType = EnumJobType.NONE;
					break;
			}

			//Cash
			String cashString = scanPerson.next();
			double cash = Double.valueOf(cashString);
			
			//Name
			String name = scanPerson.next();
			
			//Instantiate Person
			Person person = new PersonAgent(jobType, cash, name); //adds role automatically
			
			//Events
			
			if(jobType != EnumJobType.NONE)
				person.msgAddEvent(new Event(EnumEventType.JOB, person.getTimeShift() * (24 / ContactList.cNumTimeShifts)));
//			person.msgAddEvent(new Event(EnumEventType.DEPOSIT_CHECK, 0));
//			person.msgAddEvent(new Event(EnumEventType.EAT, -1));
//			person.msgAddEvent(new Event(EnumEventType.REQUEST_HOUSE, 1));

			
			if(name.contains("party"))
				person.msgAddEvent(new Event(EnumEventType.PLANPARTY, -1));
			
			if(name.contains("car"))
				person.msgAddEvent(new Event(EnumEventType.GET_CAR, 0));

			if(name.contains("house"))
				person.msgAddEvent(new Event(EnumEventType.MAINTAIN_HOUSE, 8));
			
			if(name.contains("robber"))
				person.msgAddEvent(new Event(EnumEventType.DEPOSIT_CHECK, 1));

			//DAVID SHANE: add more events
			
			synchronized (person) {
				ContactList.sPersonList.add(person);
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
