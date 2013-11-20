package transportation;

import base.interfaces.Person;

public class Rider {
	Person mPerson;
	int mDestination;
	
	public Rider(Person p, int dest) {
		mPerson = p;
		mDestination = dest;
	}
}
