package base;

import java.util.HashMap;
import java.util.Map;

import city.gui.CityClosed;
import city.gui.CityComponent;
import city.gui.CityInspection;

public class Inspection {
	
	//Closed Images
	public static Map<Location, CityComponent> sClosedImages = new HashMap<Location, CityComponent>();
	public static Map<Location, CityInspection> sInspectionImages = new HashMap<Location, CityInspection>();
	static{
		for (Location iLocation: ContactList.cRESTAURANT_LOCATIONS){ //Restaurant
			sClosedImages.put(iLocation, new CityClosed(iLocation.createNew()));
			sInspectionImages.put(iLocation, new CityInspection(iLocation.createNew()));
		}
		sClosedImages.put(ContactList.cBANK1_LOCATION, new CityClosed(ContactList.cBANK1_LOCATION.createNew()));
		sClosedImages.put(ContactList.cBANK2_LOCATION, new CityClosed(ContactList.cBANK2_LOCATION.createNew()));
		sClosedImages.put(ContactList.cMARKET1_LOCATION, new CityClosed(ContactList.cMARKET1_LOCATION.createNew()));
		sClosedImages.put(ContactList.cMARKET2_LOCATION, new CityClosed(ContactList.cMARKET2_LOCATION.createNew()));
		
		sInspectionImages.put(ContactList.cBANK1_LOCATION, new CityInspection(ContactList.cBANK1_LOCATION.createNew()));
		sInspectionImages.put(ContactList.cBANK2_LOCATION, new CityInspection(ContactList.cBANK2_LOCATION.createNew()));
		sInspectionImages.put(ContactList.cMARKET1_LOCATION, new CityInspection(ContactList.cMARKET1_LOCATION.createNew()));
		sInspectionImages.put(ContactList.cMARKET2_LOCATION, new CityInspection(ContactList.cMARKET2_LOCATION.createNew()));
	}
}
