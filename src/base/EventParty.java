package base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.interfaces.Person;

/* SHANE: Check this
  People like parties. They like to give them when they get the urge and go to them when we're invited. Here are the requirements for a PersonAgent. 
	• Your friends will be located in your contact list. 
	• Planning a Party at your house:
	o Plan a party when you get the urge. 
	o Pick a date, time, and your rsvp deadline. 
	o From your friends, select your best ones to invite. 
	[Nobody invites all their friends!]
	o Don't forget to get enough food for the party.
	o Even among your best friends, you'll have some deadbeats who have NOT responded by the rsvp deadline. 
		Send them another message reminding them of the party; if they say they will come, update your food plans.
	• Giving the party involves having it at your house.
	
	Getting invited to a party:
	• Somehow you must decide if you want to go.
	• Don't forget to go to the parties when the time arrives!!
 */

//REX: remove redundancies by removing event location

public class EventParty extends Event{
	public EventParty(EnumEventType type, int time, Location location, Person host, List<Person> pplInvited) {
		super(type, time, location);
		mAttendees = new HashMap<Person, Boolean>();
		mHost = host;
		for (Person iPerson : pplInvited){
			mAttendees.put(iPerson, false);
		}
	}
	
	public EventParty(EnumEventType type, int time, Person host) {
		super(type, time, null);
		mAttendees = new HashMap<Person, Boolean>();
		mHost = host;
	}
	
	public EventParty(EventParty party, EnumEventType type, int timeDelay, List<Person> pplInvited){
		super(type, party.mTime + timeDelay, party.mLocation);
		mAttendees = new HashMap<Person, Boolean>();
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
	
	public Person mHost;
	public Map<Person, Boolean> mAttendees; //1 is attending, 0 unknown (remove if not coming)
}
