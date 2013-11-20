package base;

import java.util.List;
import java.util.Map;

import base.interfaces.Person;

public class EventParty extends Event{
	public EventParty(EnumEventType type, int time, Location location, Person host, List<Person> pplInvited) {
		super(type, time, location);
		mHost = host;
		for (Person iPerson : pplInvited){
			mAttendees.put(iPerson, false);
		}
	}
	
	public EventParty(EventParty party, EnumEventType type, int timeDelay, List<Person> pplInvited){
		super(type, party.mTime + timeDelay, party.mLocation);
		mHost = party.mHost;
		for (Person iPerson : pplInvited){
			mAttendees.put(iPerson, false);
		}
	}
	
	public EventParty(EventParty party, int timeDelay){
		super(party.mEventType, party.mTime + timeDelay, party.mLocation);
		mHost = party.mHost;
		mAttendees = party.mAttendees;
	}
	
	Person mHost;
	Map<Person, Boolean> mAttendees; //1 is attending, 0 unknown (remove if not coming)
}
