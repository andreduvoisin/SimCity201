package base;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import base.interfaces.Person;

public class Time {
	
	static final int cLengthOfDay = 6*60; //seconds
	static final int cTimeShift = 8;
	
	static int sGlobalTimeInt = 0;
	static int sGlobalHour = 0;
	static int sGlobalShift = 0;
	static int sGlobalDay = 0;
	
	List<Person> mPersons;
	
	Timer mTimer = new Timer();
	
	public Time(){
		
		int simShift = cLengthOfDay/3;
		int simHour = simShift / cTimeShift; //15
		int realSeconds = ((int) System.currentTimeMillis()) / 1000;
		int realLengthOfSimHour = realSeconds / simHour;
		
		mTimer.schedule(new TimerTask() {
			
			@Override
			//Broadcast time
			public void run() {
				sGlobalTimeInt++;
				sGlobalHour = (sGlobalHour + 1) % 24;
				if (sGlobalHour % 8 == 0){
					sGlobalShift = (sGlobalShift + 1) % 3;
					for (Person iPerson : mPersons){
						iPerson.msgTimeShift();
					}
				}
				if (sGlobalHour % 24 == 0){
					sGlobalDay = sGlobalDay + 1;
				}
			}
		}, 
		realLengthOfSimHour);
	}

	
	public static int GetHour(){ //0 to 23
		return sGlobalHour;
	}
	
	public static int GetShift(){ //0 to 2
		return sGlobalShift;
	}
	
	public static int GetDat(){
		return sGlobalDay;
	}
	
	public static int GetTime(){
		return sGlobalTimeInt;
	}
}
