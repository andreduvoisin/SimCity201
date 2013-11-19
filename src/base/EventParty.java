package base;

import java.util.List;
import java.util.Map;

import base.interfaces.Person;

public class EventParty extends Event{
	public EventParty(EnumEventType event, int time, Location location, Person host, List<Person> pplInvited) {
		super(event, time, location);
		mHost = host;
		for (Person iPerson : pplInvited){
			mAttendees.put(iPerson, false);
		}
	}
	Person mHost;
	Map<Person, Boolean> mAttendees; //1 is attending, 0 unknown (remove if not coming)
}
