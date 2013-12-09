package city.gui.properties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import base.ContactList;
import base.Item;
import base.Item.EnumItemType;

public class PlacesPropertiesLabel extends JPanel implements ActionListener{
	JLabel staffLabel;
	
	JLabel inventoryLabel;
	JPanel changeInventory;
	JComboBox changeInventoryType;
	JTextField changeInventoryField;
	JButton changeInventoryButton;
	JPanel changeStaff;
	JComboBox changeStaffType;
	JTextField changeStaffField;
	JButton changeStaffButton;
	
	public PlacesPropertiesLabel() {
		setPreferredSize(new Dimension(180,600));
		staffLabel = new JLabel();
		updateStaff();
		
		inventoryLabel = new JLabel();
		updateInventory();
		
		setUpStaffChange();
		setUpInventoryChange();

		add(staffLabel);
		add(changeStaff);
		add(inventoryLabel);
		add(changeInventory);
		setVisible(true);
	}
	
	private void setUpStaffChange() {
		changeStaff = new JPanel();
		changeStaff.setLayout(new BorderLayout());
		
		String [] staffPositions = {"","Cashier", "Delivery Truck", "Worker1", "Worker2"};
		
		changeStaffType = new JComboBox<String>(staffPositions);
		
		changeStaffField = new JTextField();
		changeStaffField.setPreferredSize(new Dimension(60,20));
		changeStaffButton = new JButton("Change Staff");
		changeStaffButton.setPreferredSize(new Dimension(120,20));
		changeStaffField.addActionListener(this);
		changeStaffButton.addActionListener(this);
		
		changeStaff.add(changeStaffType,BorderLayout.NORTH);
		changeStaff.add(changeStaffField, BorderLayout.WEST);
		changeStaff.add(changeStaffButton, BorderLayout.EAST);
		
	}
	private void setUpInventoryChange() {
		changeInventory = new JPanel();
		changeInventory.setLayout(new BorderLayout());
	//	changeInventory.setPreferredSize(new Dimension(180,60));
		String [] inventoryTypes = {"","Steak","Chicken","Salad","Pizza","Car"};
		changeInventoryType = new JComboBox<String>(inventoryTypes);
		
		changeInventoryField = new JTextField();
		changeInventoryField.setPreferredSize(new Dimension(60,20));
		changeInventoryButton = new JButton("Set Inventory");
		changeInventoryButton.setPreferredSize(new Dimension(120,20));
		
		changeInventory.add(changeInventoryType,BorderLayout.NORTH);
		changeInventory.add(changeInventoryField,BorderLayout.WEST);
		changeInventory.add(changeInventoryButton,BorderLayout.EAST);
		
		changeInventoryField.addActionListener(this);
		changeInventoryButton.addActionListener(this);
	}

	public void changeBuilding(String s) {
		switch(s) {
		case "None":
			inventoryLabel.setText("");
			break;
		case "Gringotts":
			inventoryLabel.setText("<html>" +
					"Master Teller: " + 
					
					"</html>");
			break;
		case "Piggy Bank":
			inventoryLabel.setText("Piggy Bank");
			break;
		case "Honeydukes":
			inventoryLabel.setText("<html>" +
					"Inventory:" +
					
					"</html>");
			break;
		case "Ollivanders":
			inventoryLabel.setText("ollivanders");
			break;
		case "duvoisin":
			inventoryLabel.setText("a");
			break;	
		case "cwagoner":
			inventoryLabel.setText("c");
			break;
		case "jerryweb":
			inventoryLabel.setText("j");
			break;
		case "maggiyan":
			inventoryLabel.setText("m");
			break;
		case "davidmca":
			inventoryLabel.setText("d");
			break;
		case "smileham":
			inventoryLabel.setText("s");
			break;
		case "tranac":
			inventoryLabel.setText("t");
			break;
		case "xurex":
			inventoryLabel.setText("x");
			break;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeStaffField || e.getSource() == changeStaffButton) {
        	if(!changeStaffType.getSelectedItem().toString().equals("")) {
        		System.out.println("blah, test");	//ANGELICA:
        		updateStaff();
        	}
        }
        if (e.getSource() == changeInventoryField || e.getSource() == changeInventoryButton) {
        	if(!changeInventoryType.getSelectedItem().toString().equals("")) {
        		ContactList.sMarketList.get(0).setInventory(Item.stringToEnum(changeInventoryType.getSelectedItem().toString()),
        				Integer.parseInt(changeInventoryField.getText()));
        		updateInventory();
        	}
        }
	}
	
	private void updateStaff() {
		staffLabel.setText("<html><center>Staff<br><table>" +
				"<tr><td>Cashier: </td><td>" + ContactList.sMarketList.get(0).mCashier.toString()+ "</td></tr>" +
				"<tr><td>DeliveryTruck: </td><td>" + ContactList.sMarketList.get(0).mDeliveryTruck.toString() + "</td></tr>" +
				"<tr><td>Worker: </td><td>" + ContactList.sMarketList.get(0).mWorkers.get(0).toString() + "</td></tr>" +
				"<tr><td>Worker: </td><td>" + ContactList.sMarketList.get(0).mWorkers.get(1).toString() + "</td></tr>" +
				"</table></center></html>");
	}
	
	private void updateInventory() {
		inventoryLabel.setText("<html><center>Inventory<br><table>" +
				"<tr><td>Chicken: </td><td>" + ContactList.sMarketList.get(0).getInventory(EnumItemType.CHICKEN) + "</td>" +
				"<td>Steak: </td><td>" + ContactList.sMarketList.get(0).getInventory(EnumItemType.STEAK) + "</td></tr>" +
				"<tr><td>Salad: </td><td>" + ContactList.sMarketList.get(0).getInventory(EnumItemType.SALAD) + "</td>" +
				"<td>Pizza: </td><td>" + ContactList.sMarketList.get(0).getInventory(EnumItemType.PIZZA) + "</td></tr>" +
				"<tr><td>Cars: </td><td>" + ContactList.sMarketList.get(0).getInventory(EnumItemType.CAR) + "</td></tr>" +
				"</table></center></html>");
	}
}
