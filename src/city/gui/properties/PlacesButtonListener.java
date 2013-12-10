package city.gui.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import base.ContactList;
import base.Inspection;
import city.gui.CityClosed;

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
		synchronized(ContactList.sOpenPlaces) {
			synchronized(ContactList.sOpenBuildings) {
				switch((String)places.getSelectedItem()) {
					case "None":
						break;
					case "Gringotts":
						if (ContactList.sOpenPlaces.get(ContactList.cBANK1_LOCATION)){
							ContactList.sOpenPlaces.put(ContactList.cBANK1_LOCATION, false);
							ContactList.sOpenBuildings.put("B1", false);
							CityClosed c = (CityClosed) Inspection.sClosedImages.get(ContactList.cBANK1_LOCATION);
							c.enable();
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cBANK1_LOCATION, true);
							ContactList.sOpenBuildings.put("B1", true);
							Inspection.sClosedImages.get(ContactList.cBANK1_LOCATION).disable();
							button.setText("Disable");
						}
						break;
					case "Piggy Bank":
						if (ContactList.sOpenPlaces.get(ContactList.cBANK2_LOCATION)){
							ContactList.sOpenPlaces.put(ContactList.cBANK2_LOCATION, false);
							ContactList.sOpenBuildings.put("B2", false);
							Inspection.sClosedImages.get(ContactList.cBANK2_LOCATION).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cBANK2_LOCATION, true);
							ContactList.sOpenBuildings.put("B2", true);
							Inspection.sClosedImages.get(ContactList.cBANK2_LOCATION).disable();
							button.setText("Disable");
						}
						break;
					case "Honeydukes":
						if (ContactList.sOpenPlaces.get(ContactList.cMARKET1_LOCATION)){
							ContactList.sOpenPlaces.put(ContactList.cMARKET1_LOCATION, false);
							ContactList.sOpenBuildings.put("M1", false);
							Inspection.sClosedImages.get(ContactList.cMARKET1_LOCATION).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cMARKET1_LOCATION, true);
							ContactList.sOpenBuildings.put("M1", true);
							Inspection.sClosedImages.get(ContactList.cMARKET1_LOCATION).disable();
							button.setText("Disable");
						}
						break;
					case "Ollivanders":
						if (ContactList.sOpenPlaces.get(ContactList.cMARKET2_LOCATION)){
							ContactList.sOpenBuildings.put("M2", false);
							ContactList.sOpenPlaces.put(ContactList.cMARKET2_LOCATION, false);
							Inspection.sClosedImages.get(ContactList.cMARKET2_LOCATION).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cMARKET2_LOCATION, true);
							ContactList.sOpenBuildings.put("M2", true);
							Inspection.sClosedImages.get(ContactList.cMARKET2_LOCATION).disable();
							button.setText("Disable");
						}
						break;
					case "duvoisin":
						if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(0))){
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(0), false);
							ContactList.sOpenBuildings.put("R0", false);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(0)).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(0), true);
							ContactList.sOpenBuildings.put("R0", true);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(0)).disable();
							button.setText("Disable");
						}
						break;	
					case "cwagoner":
						if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(1))){
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(1), false);
							ContactList.sOpenBuildings.put("R1", false);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(1)).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(1), true);
							ContactList.sOpenBuildings.put("R1", true);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(1)).disable();
							button.setText("Disable");
						}
						break;
					case "jerryweb":
						if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(2))){
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(2), false);
							ContactList.sOpenBuildings.put("R2", false);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(2)).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(2), true);
							ContactList.sOpenBuildings.put("R2", true);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(2)).disable();
							button.setText("Disable");
						}
						break;
					case "maggiyan":
						if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(3))){
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(3), false);
							ContactList.sOpenBuildings.put("R3", false);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(3)).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(3), true);
							ContactList.sOpenBuildings.put("R3", true);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(3)).disable();
							button.setText("Disable");
						}
						break;
					case "davidmca":
						if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(4))){
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(4), false);
							ContactList.sOpenBuildings.put("R4", false);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(4)).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(4), true);
							ContactList.sOpenBuildings.put("R4", true);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(4)).disable();
							button.setText("Disable");
						}
						break;
					case "smileham":
						if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(5))){
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(5), false);
							ContactList.sOpenBuildings.put("R5", false);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(5)).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(5), true);
							ContactList.sOpenBuildings.put("R5", true);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(5)).disable();
							button.setText("Disable");
						}
						break;
					case "tranac":
						if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(6))){
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(6), false);
							ContactList.sOpenBuildings.put("R6", false);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(6)).enable();
			
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(6), true);
							ContactList.sOpenBuildings.put("R6", true);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(6)).disable();
							button.setText("Disable");
						}
						break;
					case "xurex":
						if (ContactList.sOpenPlaces.get(ContactList.cRESTAURANT_LOCATIONS.get(7))){
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(7), false);
							ContactList.sOpenBuildings.put("R7", false);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(7)).enable();
							button.setText("Enable");
						}
						else{
							ContactList.sOpenPlaces.put(ContactList.cRESTAURANT_LOCATIONS.get(7), true);
							ContactList.sOpenBuildings.put("R7", true);
							Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(7)).disable();
							button.setText("Disable");
						}
						break;
				}
			}
		}
	}
};