package base;


public class Event implements Comparable<Event> {
	public enum EnumEventType {GET_CAR, 									//One time event
						JOB, EAT, 									//Daily Recurring Events
						DEPOSIT_CHECK, 								//Intermittent Events
						ASK_FOR_RENT, MAINTAIN_HOUSE,				//Housing Events
						INVITE1, INVITE2, RSVP1, RSVP2, PARTY};		//Party Events

	EnumEventType mEventType;
	public int mTime;
	Location mLocation;

	public Event(EnumEventType type, int time) {
		mEventType = type;
		mTime = time;
	}

	public Event(EnumEventType type, int time, Location location) {
		mEventType = type;
		mTime = time;
		mLocation = location;
	}

	public Event(Event event, int timeDelay) {
		mEventType = event.mEventType;
		mTime = event.mTime + timeDelay;
		mLocation = event.mLocation;
	}

	@Override
	public int compareTo(Event event) {
		// http://docs.oracle.com/javase/1.4.2/docs/api/java/lang/Comparable.html
		if (mTime < event.mTime) {
			return -1;
		}
		if (mTime == event.mTime) {
			return 0;
		}
		return 1;
	}

}
