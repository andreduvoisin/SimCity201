package restaurant.restaurant_maggiyan.test.mock;

/**
 * This is the base class for all mocks.
 *
 * @author Sean Turner
 *
 */
public class Mock {
	private String name;
	
	public EventLog log;

	public Mock(String name) {
		this.name = name;
		log = new EventLog(); 
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}

}
