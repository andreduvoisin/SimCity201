package base;

import java.util.Map;

import base.interfaces.Person;

public class EventParty extends Event{
	Person mHost;
	Map<Person, Boolean> mAttendees; //1 is attending
}
