package base;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import base.interfaces.Person;

public class Time {
	
	// ANDRE: add clock (analog or digital) in corner of screen which will tell day/time in sim city
	
	static final int cLengthOfDay = 3*60; //seconds
	static final int cTimeShift = 8;
	
	static int sGlobalTimeInt = 0;
	static int sGlobalHour = 0;
	static int sGlobalShift = 0;
	static int sGlobalDate = 0;
	
	static boolean sFastForward = false;
	
	List<Person> mPersons;
	
	Timer mTimer;
	
	public Time(List<Person> people){
		mPersons = people;
		mTimer = new Timer();
		runTimer();
	}
	
	
	public void runTimer(){
		int simShift = cLengthOfDay/3;
		int simHour = simShift / cTimeShift; //15
		int realSeconds = ((int) System.currentTimeMillis()) / 1000;
		int realLengthOfSimHour = realSeconds / simHour;
		
		int timerLength = realLengthOfSimHour;
		if (sFastForward) timerLength /= 4;
				
		mTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			//Broadcast time
			public void run() {
				sGlobalTimeInt++;
				sGlobalHour = (sGlobalHour + 1) % 24;
				if (sGlobalHour % 8 == 0){
					sGlobalShift = (sGlobalShift + 1) % 3;
					synchronized (mPersons) {
						for (Person iPerson : mPersons) {
							iPerson.msgTimeShift();
						}
					}
				}
				if (sGlobalHour % 24 == 0){
					sGlobalDate = sGlobalDate + 1;
				}
			}
		}, new Date( System.currentTimeMillis()), 1000);
		
		/*mTimer.schedule(new TimerTask() {
			
			@Override
			//Broadcast time
			public void run() {
				sGlobalTimeInt++;
				sGlobalHour = (sGlobalHour + 1) % 24;
				if (sGlobalHour % 8 == 0) {
					sGlobalShift = (sGlobalShift + 1) % 3;
					synchronized (mPersons) {
						for (Person iPerson : mPersons) {
							iPerson.msgTimeShift();
						}
					}
				}
				if (sGlobalHour % 24 == 0) {
					sGlobalDate = sGlobalDate + 1;
				}
				runTimer();
			}
		}, 
		3000);*/
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
	
	public static void FlipFastForward(){ //turn FF on or off
		sFastForward = !sFastForward;
	}
	
	public static boolean IsWeekend(){ //sat and sun = 5 and 6
		return ((sGlobalDate%7 == 5) || (sGlobalDate%7 == 6));
	}
}
