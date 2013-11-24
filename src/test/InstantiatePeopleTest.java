package test;

import java.io.FileNotFoundException;

import restaurant.interfaces.RestaurantCustomerRole;
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
		assertEquals("Check size of master person list", panel.masterPersonList.size(),12);
		assertEquals("Verify time shifts", panel.masterPersonList.get(0).getTimeShift(), 0);
		assertEquals("Verify time shifts", panel.masterPersonList.get(1).getTimeShift(), 1);
		assertEquals("Verify time shifts", panel.masterPersonList.get(2).getTimeShift(), 2);
		for (int i =0; i< panel.masterPersonList.size(); i++) {
			System.out.println(panel.masterPersonList.get(i).getHousingRole().toString());
		}
		assertEquals("Verify that first person starts with 6", panel.masterPersonList.get(0).getRoles().size(), 6);
		RestaurantCustomerRole custrole1 = new RestaurantCustomerRole(panel.masterPersonList.get(0));
		custrole1.setRestaurant(1);
		panel.masterPersonList.get(0).addRole(custrole1, true);
		assertEquals("Verify that first person now has 7", panel.masterPersonList.get(0).getRoles().size(), 7);
		
	}
}
