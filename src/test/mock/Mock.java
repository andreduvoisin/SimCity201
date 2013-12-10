package test.mock;

import java.util.concurrent.Semaphore;

/**
 * This is the base class for test mocks.
 *
 * @author Maggi Yang
 *
 */
public class Mock {
	
	public EventLog log;
	public Semaphore inTransit = new Semaphore(0,true);

	public Mock() {
		log = new EventLog(); 
	}

//	UTILITIES
	@SuppressWarnings("unused")
	private void print(String message){
		System.out.println(message);
	}
}
