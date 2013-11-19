package base;

public class Event implements Comparable<Event>{
	enum EnumEventType {JOB, EAT, CAR, DEPOSIT_CHECK, PARTY};
	EnumEventType mEvent;
	int mTime;
	Location mLocation;
	
	
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
