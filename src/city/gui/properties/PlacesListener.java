package city.gui.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import base.ContactList;

//Listener for JComboBox (Drop Down Menu)
public class PlacesListener implements ActionListener { 
	JButton button;
	@SuppressWarnings("rawtypes")
	JComboBox places;
	PlacesPropertiesLabel label;
	public
	PlacesListener(JButton b, @SuppressWarnings("rawtypes") JComboBox p, PlacesPropertiesLabel l){
		button = b;
		places = p;
		label = l;
	}
	public void actionPerformed(ActionEvent e) {
		label.changeBuilding(places.getSelectedItem().toString());
		synchronized(ContactList.sOpenPlaces) {
			switch((String)places.getSelectedItem()) {
				case "None":
					button.setText("None");
					break;
				case "Gringotts":
					if (ContactList.sOpenPlaces.get(ContactList.cBANK1_LOCATION))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "Piggy Bank":
					if (ContactList.sOpenPlaces.get(ContactList.cBANK2_LOCATION))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "Honeydukes":
					if (ContactList.sOpenPlaces.get(ContactList.cMARKET1_LOCATION))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "Ollivanders":
					if (ContactList.sOpenPlaces.get(ContactList.cMARKET2_LOCATION))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "duvoisin":
					if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(0)))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;	
				case "cwagoner":
					if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(1)))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "jerryweb":
					if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(2)))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "maggiyan":
					if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(3)))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "davidmca":
					if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(4)))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "smileham":
					if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(5)))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "tranac":
					if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(6)))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
				case "xurex":
					if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(7)))
						button.setText("Disable");
					else
						button.setText("Enable");
					break;
			}
		}
	}
}
