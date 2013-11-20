package test.mock;

/**
 * This is the base class for test mocks.
 *
 * @author Maggi Yang
 *
 */
public class Mock {
	
	public EventLog log;

	public Mock() {
		log = new EventLog(); 
	}


//	UTILITIES
	private void print(String message){
		System.out.println(message);
	}
}
