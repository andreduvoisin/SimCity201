package test;

import java.io.IOException;

import junit.framework.TestCase;
import base.ConfigParser;
import city.gui.CityPanel;

public class InstantiatePeopleTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void testRestaurantIntegration() throws IOException {
		ConfigParser config = ConfigParser.getInstanceOf();
		config.readFileCreatePersons(null, 0);
		CityPanel panel = CityPanel.getInstance();
		assertEquals("Check size of master person list", panel.masterPersonList.size(),12);
		/*assertEquals("Verify time shifts", panel.masterPersonList.get(0).getTimeShift(), 0);
		assertEquals("Verify time shifts", panel.masterPersonList.get(1).getTimeShift(), 1);
		assertEquals("Verify time shifts", panel.masterPersonList.get(2).getTimeShift(), 2);*/
		for (int i =0; i< panel.masterPersonList.size(); i++) {
			//System.out.println(panel.masterPersonList.get(i).getHousingRole().toString());
		}
		assertEquals("Verify that first person now has 7", panel.masterPersonList.get(0).getRoles().size(), 7);
		//((PersonAgent) panel.masterPersonList.get(0)).eatFood();
		//((PersonAgent) panel.masterPersonList.get(0)).pickAndExecuteAnAction();
		//RestaurantGui restgui = new RestaurantGui();
		//RestaurantPanel restpanel = RestaurantPanel.getInstance();
		//assertEquals("Make sure restaurant now has one customer", RestaurantPanel.getInstance().customers.size(), 1);
	}
}
