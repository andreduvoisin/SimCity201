package base;

import interfaces.Person;

import java.util.ArrayList;
import java.util.List;

import market.MarketCustomerRole;

public class PersonAgent extends Agent implements Person{

	//Data
	private static int sSSN = 0;
	private int mSSN;
	private static int sTimeSchedule = 0;
	private int mTimeSchedule;
	
	private List<Role> mRoles;
	private double mCredit; //credit card
	private int mCreditScore;
	private boolean mHasLoan; //credit?
	private boolean mHasCar;
	private int mMealsToEat;
	private int mAge;
	
	
//	public RestaurantGui mGUI;
	//everything about the city

	public PersonAgent(){
		mSSN = sSSN++; //assign SSN
		mTimeSchedule = (sTimeSchedule++ % 24); //assign time schedule 
		mRoles = new ArrayList<Role>();
		mCredit = 0; //TODO: 3 update this val
		mCreditScore = 0; //TODO: 3 update this val
		
	}
	
	
	
	//Messages
	
	
	//Scheduler

	/*
	 * WORK
	 * go to work
	 * 
	 * PERSONAL
	 * eat x2
	 * if () buy house
	 * 
	 * SLEEP
	 * reset member vars
	 */		
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		
		for (Role iRole : mRoles){
			paea(iRole);
			
		}
		//check active
		
		
		Role role = new MarketCustomerRole();
		mRoles.add(role);
		
		
		return false;
	}
	
	public void paea(Role role){
		while(role.pickAndExecuteAnAction());
	}
	
	
	//Actions
	
	
}
