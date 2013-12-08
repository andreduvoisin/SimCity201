package city.gui.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import base.ContactList;

//Listener for JComboBox (Drop Down Menu)
public class PlacesButtonListener implements ActionListener { 
	JButton button;
	@SuppressWarnings("rawtypes")
	JComboBox places;
	@SuppressWarnings("rawtypes")
	public
	PlacesButtonListener(JButton b, JComboBox p){
		button = b;
		places = p;
	}
	public void actionPerformed(ActionEvent e) {
		switch((String)places.getSelectedItem()) {
		case "None":
			break;
		case "Gringotts":
			if (ContactList.sOpenPlaces.get(ContactList.cBANK1_LOCATION)){
				ContactList.sOpenPlaces.put(ContactList.cBANK1_LOCATION, false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cBANK1_LOCATION, true);
				button.setText("Disable");
			}
			break;
		case "Piggy Bank":
			if (ContactList.sOpenPlaces.get(ContactList.cBANK2_LOCATION)){
				ContactList.sOpenPlaces.put(ContactList.cBANK2_LOCATION, false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cBANK2_LOCATION, true);
				button.setText("Disable");
			}
			break;
		case "Honeydukes":
			if (ContactList.sOpenPlaces.get(ContactList.cMARKET1_LOCATION)){
				ContactList.sOpenPlaces.put(ContactList.cMARKET1_LOCATION, false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cMARKET1_LOCATION, true);
				button.setText("Disable");
			}
			break;
		case "Ollivaders":
			if (ContactList.sOpenPlaces.get(ContactList.cMARKET2_LOCATION)){
				ContactList.sOpenPlaces.put(ContactList.cMARKET2_LOCATION, false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cMARKET2_LOCATION, true);
				button.setText("Disable");
			}
			break;
		case "duvoisin":
			if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(0))){
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(0), false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(0), true);
				button.setText("Disable");
			}
			break;	
		case "cwagoner":
			if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(1))){
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(1), false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(1), true);
				button.setText("Disable");
			}
			break;
		case "jerryweb":
			if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(2))){
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(2), false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(2), true);
				button.setText("Disable");
			}
			break;
		case "maggiyan":
			if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(3))){
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(3), false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(3), true);
				button.setText("Disable");
			}
			break;
		case "davidmca":
			if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(4))){
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(4), false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(4), true);
				button.setText("Disable");
			}
			break;
		case "smileham":
			if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(5))){
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(5), false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(5), true);
				button.setText("Disable");
			}
			break;
		case "tranac":
			if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(6))){
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(6), false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(6), true);
				button.setText("Disable");
			}
			break;
		case "xurex":
			if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(7))){
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(7), false);
				button.setText("Enable");
			}
			else{
				ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(7), true);
				button.setText("Disable");
			}
			break;
		}
	}
};