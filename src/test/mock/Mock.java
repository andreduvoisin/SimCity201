package test.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the base class for all mocks.
 *
 * @author Sean Turner
 *
 */
public class Mock {
	protected String mName;
	
	public Mock(String name) {
		this.mName = name;
	}

	public String getName() {
		return mName;
	}

	public String toString() {
		return this.getClass().getName() + ": " + mName;
	}

}
