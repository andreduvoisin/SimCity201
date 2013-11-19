package test.mock;


import base.interfaces.Person;

/**
 * MockPerson built to unit test Housing
 *
 * @author Maggi Yang 
 *
 */
public class MockPerson extends Mock implements Person {

	public Person person; 
	
	public MockPerson(String name) {
		super(name);

	}

	@Override
	public void msgTimeShift() {
		log.add(new LoggedEvent("Received msgTimeShift")); 
	}

	@Override
	public void setCredit(double credit) {
		log.add(new LoggedEvent("Initial credit set to: " + credit)); 
		
	}

	@Override
	public double getCredit() {
		return 0;
	}

	@Override
	public void addCredit(double amount) {
		log.add(new LoggedEvent("Added credit. New credit amount: " + amount)); 
		
	}



}
