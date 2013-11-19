package base;

public class Event implements Comparable<Event>{
	enum EnumEventType {BUY_HOME, JOB, EAT, GET_CAR, DEPOSIT_CHECK, INVITE1, INVITE2, RSVP1, RSVP2, PARTY};
	EnumEventType mEvent;
	int mTime;
	Location mLocation;
	
	public Event(EnumEventType type, int time){
		mEvent = type;
		mTime = time;
	}
	
	public Event(EnumEventType event, int time, Location location){
		mEvent = event;
		mTime = time;
		mLocation = location;
	}
	
	public Event(Event event, int timeDelay){
		mEvent = event.mEvent;
		mTime = event.mTime + timeDelay;
		mLocation = event.mLocation;
	}
	
	@Override
	public int compareTo(Event event) {
		//http://docs.oracle.com/javase/1.4.2/docs/api/java/lang/Comparable.html
		if (mTime < event.mTime){
			return -1;
		}
		if (mTime == event.mTime){
			return 0;
		}
		return 1;
	}
}
