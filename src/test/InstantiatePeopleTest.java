package test;

import java.io.FileNotFoundException;

import junit.framework.TestCase;
import base.ConfigParser;
import city.gui.CityPanel;

public class InstantiatePeopleTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void testCreatePeople() throws FileNotFoundException {
		ConfigParser config = ConfigParser.getInstanceOf();
		config.readFileCreatePersons();
		CityPanel panel = CityPanel.getInstanceOf();
		assertEquals("Check size of master person list", panel.masterPersonList.size(),3);
		
	}
}
