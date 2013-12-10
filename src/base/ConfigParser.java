package base;

import java.io.FileNotFoundException;
import java.util.Scanner;

import bank.BankAccount;
import bank.roles.BankMasterTellerRole;
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
	private final static int timeBlock = 8;

	public void readFileCreatePersons(SimCityGui simcitygui, String fileName) throws FileNotFoundException {
		Scanner scanFile = new Scanner(getClass().getResourceAsStream("/runconfig/"+fileName));
		
		//Instantiate the base roles before creating the people
//		boolean mInstantiateRoles = true;
		
		while (scanFile.hasNext()) {
			//Order of Inputs: Job Type (BANK, MARKET, RESTAURANT, NONE), Cash, Name
			Scanner scanPerson = new Scanner(scanFile.nextLine()); //separate by person
			
			//Job
			String firstString = scanPerson.next();	
//			if (firstString.contains("CMD")) {
//				if (scanPerson.next().contains("DisableIntersection")) {
//					SimCityGui.getInstance().citypanel.mCrashScenario = true;
//				}
//			}
//			else {
				EnumJobType jobType = null;
				switch(firstString) {
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
				
				//Account Creation
				((BankMasterTellerRole) ContactList.masterTeller).mAccounts.add(new BankAccount(0, cash, person));
				((BankMasterTellerRole) ContactList.masterTeller).mAccountIndex.put(person.getSSN(), ((BankMasterTellerRole) ContactList.masterTeller).mAccounts.size()-1);
				
				//Events			
				if(jobType != EnumJobType.NONE)
					person.msgAddEvent(new Event(EnumEventType.JOB, person.getTimeShift() * (24 / ContactList.cNumTimeShifts)));
				else {
					if(name.contains("bankcust"))
						person.msgAddEvent(new Event(EnumEventType.DEPOSIT_CHECK, 0));
					else if(name.contains("restcust"))
						person.msgAddEvent(new Event(EnumEventType.EAT, 0));
					else if(name.contains("party")){
						person.msgAddEvent(new Event(EnumEventType.PLANPARTY, -1));
					}
					else if(name.contains("car"))
						person.msgAddEvent(new Event(EnumEventType.GET_CAR, 0));
					else if(name.contains("house"))
						person.msgAddEvent(new Event(EnumEventType.MAINTAIN_HOUSE, 8));
					else if(name.contains("robber"))
						person.msgAddEvent(new Event(EnumEventType.DEPOSIT_CHECK, -1));
					else if(name.contains("inspection")){
						if(name.contains("hascar"))
							person.setHasCar(true);
						if(name.contains("noca"))
							person.setHasCar(false);
						person.msgAddEvent(new Event(EnumEventType.EAT, -1));
						person.msgAddEvent(new Event(EnumEventType.INSPECTION, 0));
					}
					
					//Interesting Interweaving
					if(name.contains("inter")){
						int ssn = person.getSSN();
						int time = Time.GetTime();
						int size = ContactList.sEventList.size();
						int timeDelay = timeBlock % ssn;
						Event e1 = ContactList.sEventList.get(ssn%size); ssn++;
						Event e2 = ContactList.sEventList.get(ssn%size); ssn++;
						Event e3 = ContactList.sEventList.get(ssn%size);
						//Set Time to Current
						e1.setTime(time); e2.setTime(time); e3.setTime(time);
						//Add Event to Person
						person.msgAddEvent(new Event(e1, 0*timeBlock + timeDelay));
						person.msgAddEvent(new Event(e2, 1*timeBlock + timeDelay));
						person.msgAddEvent(new Event(e3, 2*timeBlock + timeDelay));
					}
				}
	
				//DAVID SHANE: add more events
				
				synchronized (person) {
					ContactList.sPersonList.add(person);
					simcitygui.citypanel.addMoving(person.getPersonGui()); //allow to move
					((PersonAgent) person).startThread();
					System.out.println("num of persons: "+ContactList.sPersonList.size());
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
