package base;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import base.interfaces.Person;

public class Time {
	
	public static int sGlobalTimeInt = 0; //minutes
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
				sGlobalTimeInt++;
				
				//state changed
				if (sGlobalTimeInt % 60 == 0){
					synchronized (mPersons) {
						for (Person iPerson : mPersons) {
							iPerson.msgStateChanged();
						}
					}
					
					if (sGlobalTimeInt % ((24 / ContactList.cNumTimeShifts)*60) == 0){
						System.out.println("Time Shift! (but not)");
						synchronized (mPersons) {
							for (Person iPerson : mPersons) {
								iPerson.msgTimeShift(); 
							}
						}
					}
				}
			}
		}, new Date( System.currentTimeMillis()), cSYSCLK); //SHANE: 2
		
	}

	public static int GetMinute(){
		return (sGlobalTimeInt) % 60;
	}
	
	public static int GetHour(){ //0 to 23
		return (sGlobalTimeInt/60) % 24;
	}
	
	public static int GetShift(){ //0 to 1
		return (sGlobalTimeInt/((24 / ContactList.cNumTimeShifts)*60)) % ContactList.cNumTimeShifts;
	}
	
	public static int GetDate(){
		return (sGlobalTimeInt/((24 / ContactList.cNumTimeShifts)*60)) % 7;
	}
	
	public static int GetTime(){
		return sGlobalTimeInt;
	}
	
//	public static void FlipFastForward(){ //turn FF on or off
//		sFastForward = !sFastForward;
//	}
	
	public static boolean IsWeekend(){ //sat and sun = 5 and 6
		return ((GetDate()%7 == 5) || (GetDate()%7 == 6));
	}
}
