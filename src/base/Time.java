package base;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import base.interfaces.Person;

public class Time {
	
	public static int sGlobalTimeInt = 0;
	public static int sGlobalMinute = 0;
	public static int sGlobalHour = 1;
	public static int sGlobalShift = 0;
	public static int sGlobalDate = 0;
	
	public final static int cSYSCLK = 200;
//	static boolean sFastForward = false;
	
	List<Person> mPersons = ContactList.sPersonList; //same pointer
	
	Timer mTimer;
	
	public Time(){
		mTimer = new Timer();
		runTimer();
	}
	
	
	public void runTimer(){
				
		mTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			//Broadcast time
			public void run() {
				sGlobalMinute++;
				sGlobalTimeInt++;
				
//				synchronized (mPersons) {
//					for (Person iPerson : mPersons) {
//						iPerson.msgTimeShift(); //ALL ADD BACK IN LATER (V2)
//						do a state changed here?
//					}
//				}
				
				if (sGlobalMinute == 60){
//					sGlobalTimeInt++;
					sGlobalMinute = 0;
					sGlobalHour++;
					synchronized (mPersons) {
						for (Person iPerson : mPersons) {
							iPerson.msgStateChanged();
						}
					}
				}
				if (sGlobalHour == 24){
					sGlobalHour = 0;
					sGlobalDate++;
				}
				if (sGlobalHour % 12 == 0){
					//SHANE: This isn't working correctly. In it's current state, a time shift happens every minute (!) passed.
					//SHANE: This is because 0 % 12 = 0.
					//SHANE: (On a funny note, when this is taken out, delay issues on animations are fixed.)
					//SHANE: I was fixing it by changing global hour to start at 1 (at the top), and increasing the rate at
					//SHANE: which the timer fires (the 10000 below). :D
					System.out.println("Time shift.");
					sGlobalShift = (sGlobalShift + 1) % 2;
					synchronized (mPersons) {
						for (Person iPerson : mPersons) {
							iPerson.msgTimeShift(); 
						}
					}
				}
			}
		}, new Date( System.currentTimeMillis()), cSYSCLK); //SHANE: 2
		
	}

	public static int GetMinute(){
		return sGlobalMinute;
	}
	
	public static int GetHour(){ //0 to 23
		return sGlobalHour;
	}
	
	public static int GetShift(){ //0 to 2
		return sGlobalShift;
	}
	
	public static int GetDate(){
		return sGlobalDate;
	}
	
	public static int GetTime(){
		return sGlobalTimeInt;
	}
	
//	public static void FlipFastForward(){ //turn FF on or off
//		sFastForward = !sFastForward;
//	}
	
	public static boolean IsWeekend(){ //sat and sun = 5 and 6
		return ((sGlobalDate%7 == 5) || (sGlobalDate%7 == 6));
	}
}
