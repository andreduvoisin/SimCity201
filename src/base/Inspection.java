package base;

import java.util.HashMap;
import java.util.Map;

import city.gui.CityClosed;
import city.gui.CityComponent;

public class Inspection {
	
	//Closed Images
	public static Map<Location, CityComponent> sClosedImages = new HashMap<Location, CityComponent>();
	static{
		for (Location iLocation: ContactList.cRESTAURANT_LOCATIONS){ //Restaurant
			sClosedImages.put(iLocation, new CityClosed(iLocation.createNew()));
			
		}
		sClosedImages.put(ContactList.cBANK1_LOCATION, new CityClosed(ContactList.cBANK1_LOCATION.createNew()));
		sClosedImages.put(ContactList.cBANK2_LOCATION, new CityClosed(ContactList.cBANK2_LOCATION.createNew()));
		sClosedImages.put(ContactList.cMARKET1_LOCATION, new CityClosed(ContactList.cMARKET1_LOCATION.createNew()));
		sClosedImages.put(ContactList.cMARKET2_LOCATION, new CityClosed(ContactList.cMARKET2_LOCATION.createNew()));
	}
	
}
