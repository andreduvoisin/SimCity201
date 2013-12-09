package city.gui.properties;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import restaurant.intermediate.RestaurantCookRole;
import market.Market;
import bank.Bank;
import base.ContactList;
import base.Item;
import base.Item.EnumItemType;

@SuppressWarnings("serial")
public class PlacesPropertiesLabel extends JPanel{
	CardLayout layout;
	
	JPanel emptyTab;
	BankLabel gringottsTab;
	BankLabel piggyBankTab;
	MarketLabel honeydukesTab;
	MarketLabel ollivandersTab;
	RestaurantLabel duvoisinTab;
	RestaurantLabel cwagonerTab;
	RestaurantLabel jerrywebTab;
	RestaurantLabel maggiyanTab;
	RestaurantLabel davidmcaTab;
	RestaurantLabel smilehamTab;
	RestaurantLabel tranacTab;
	RestaurantLabel xurexTab;
	
	public PlacesPropertiesLabel() {
		layout = new CardLayout();
		this.setLayout(layout);
		
		setPreferredSize(new Dimension(180,600));
		
		emptyTab = new JPanel();
		emptyTab.add(new JLabel(""));
		
		gringottsTab = new BankLabel(0);
		piggyBankTab = new BankLabel(1);
		honeydukesTab = new MarketLabel(0);
		ollivandersTab = new MarketLabel(1);
		duvoisinTab = new RestaurantLabel(0);
		cwagonerTab = new RestaurantLabel(1);
		jerrywebTab = new RestaurantLabel(2);
		maggiyanTab = new RestaurantLabel(3);
		davidmcaTab = new RestaurantLabel(4);
		smilehamTab = new RestaurantLabel(5);
		tranacTab = new RestaurantLabel(6);
		xurexTab = new RestaurantLabel(7);
		
		add(emptyTab,"None");
		add(gringottsTab,"Gringotts");
		add(piggyBankTab,"Piggy Bank");
		add(honeydukesTab,"Honeydukes");
		add(ollivandersTab,"Ollivanders");
		add(duvoisinTab,"duvoisin");
		add(cwagonerTab,"cwagoner");
		add(jerrywebTab,"jerryweb");
		add(maggiyanTab,"maggiyan");
		add(davidmcaTab,"davidmca");
		add(smilehamTab,"smileham");
		add(tranacTab,"tranac");
		add(xurexTab,"xurex");
		
		layout.show(this, "None");
		
		setVisible(true);
	}
	
	public void changeBuilding(String s) {
			layout.show(this, s);
	}
	
	class BankLabel extends JPanel implements ActionListener {
		String [] bankTypes = {"","Guard","Teller"};
		int bankNumber;
		Bank bank;
		
		JLabel bankLabel;
		JButton updateLabel;
		
		public BankLabel(int n) {
			bankNumber = n;
			bank = ContactList.sBankList.get(bankNumber);
			
			bankLabel = new JLabel();
			updateLabel = new JButton("Update Staff Info");
			updateLabel.addActionListener(this);
			updateBankLabel();
			
			add(bankLabel);
			add(updateLabel);
		}
		
		public void updateBankLabel() {
			switch(bank.mTellers.size()) {
			case 0:
				break;
			case 1:
				bankLabel.setText("<html><table>" +
					"<tr><td>Guard :</td><td>" + bank.mGuard.toString() + "</td></tr>" +
					"<tr><td>Teller:</td><td>" + bank.mTellers.get(0).toString() + "</td></tr>" +
					"</table></html>");
				break;
			case 2:
				bankLabel.setText("<html><table>" +
						"<tr><td>Guard :</td><td>" + bank.mGuard.toString() + "</td></tr>" +
						"<tr><td>Teller:</td><td>" + bank.mTellers.get(0).toString() + "</td></tr>" +
						"<tr><td>Teller:</td><td>" + bank.mTellers.get(1).toString() + "</td></tr>" +
						"</table></html>");
			case 3:
				bankLabel.setText("<html><table>" +
						"<tr><td>Guard :</td><td>" + bank.mGuard.toString() + "</td></tr>" +
						"<tr><td>Teller:</td><td>" + bank.mTellers.get(0).toString() + "</td></tr>" +
						"<tr><td>Teller:</td><td>" + bank.mTellers.get(1).toString() + "</td></tr>" +
						"<tr><td>Teller:</td><td>" + bank.mTellers.get(2).toString() + "</td></tr>" +
						"</table></html>");
				break;
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			updateBankLabel();
		}
	}
	
	class MarketLabel extends JPanel implements ActionListener {
		String [] staffTypes = {"","Cashier","Delivery Truck","Worker1","Worker2"};
		String [] inventoryTypes = {"","Steak","Chicken","Salad","Pizza","Car"};
		int marketNum;
		Market market;
		
		JPanel changeInventory;
		JLabel inventoryLabel;
		JComboBox<String> changeInventoryType;
		JTextField changeInventoryField;
		JButton changeInventoryButton;
		
		JPanel changeStaff;
		JLabel staffLabel;
		JComboBox<String> changeStaffType;
		JTextField changeStaffField;
		JButton changeStaffButton;
		
		public MarketLabel(int n) {
			marketNum = n;
			market = ContactList.sMarketList.get(marketNum);
			
			changeStaff = new JPanel();
			changeStaff.setLayout(new BorderLayout());
			staffLabel = new JLabel();
			changeStaffType = new JComboBox<String>(staffTypes);
			
			changeStaffField = new JTextField();
			changeStaffField.setPreferredSize(new Dimension(60,20));
			changeStaffButton = new JButton("Change Staff");
			changeStaffButton.setPreferredSize(new Dimension(120,20));
			changeStaffField.addActionListener(this);
			changeStaffButton.addActionListener(this);
			
			changeStaff.add(changeStaffType,BorderLayout.NORTH);
			changeStaff.add(changeStaffField, BorderLayout.WEST);
			changeStaff.add(changeStaffButton, BorderLayout.EAST);
			
			inventoryLabel = new JLabel();
			changeInventory = new JPanel();
			changeInventory.setLayout(new BorderLayout());
			changeInventory.setPreferredSize(new Dimension(180,60));
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
			
			updateStaff();
			updateInventory();
			
			add(staffLabel);
			add(changeStaff);
			add(inventoryLabel);
			add(changeInventory);
		}
		
		public void updateStaff() {
			staffLabel.setText("<html><center>Staff<br><table>" +
			"<tr><td>Cashier: </td><td>" + ContactList.sMarketList.get(0).mCashier.toString()+ "</td></tr>" +
			"<tr><td>DeliveryTruck: </td><td>" + ContactList.sMarketList.get(0).mDeliveryTruck.toString() + "</td></tr>" +
			"<tr><td>Worker: </td><td>" + ContactList.sMarketList.get(0).mWorkers.get(0).toString() + "</td></tr>" +
			"<tr><td>Worker: </td><td>" + ContactList.sMarketList.get(0).mWorkers.get(1).toString() + "</td></tr>" +
			"</table></center></html>");
		}
		
		public void updateInventory() {
			inventoryLabel.setText("<html><center>Inventory<br><table>" +
			"<tr><td>Chicken: </td><td>" + ContactList.sMarketList.get(marketNum).getInventory(EnumItemType.CHICKEN) + "</td>" +
			"<td>Steak: </td><td>" + ContactList.sMarketList.get(marketNum).getInventory(EnumItemType.STEAK) + "</td></tr>" +
			"<tr><td>Salad: </td><td>" + ContactList.sMarketList.get(marketNum).getInventory(EnumItemType.SALAD) + "</td>" +
			"<td>Pizza: </td><td>" + ContactList.sMarketList.get(marketNum).getInventory(EnumItemType.PIZZA) + "</td></tr>" +
			"<tr><td>Cars: </td><td>" + ContactList.sMarketList.get(marketNum).getInventory(EnumItemType.CAR) + "</td></tr>" +
			"</table></center></html>");
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
	        		market.setInventory(Item.stringToEnum(changeInventoryType.getSelectedItem().toString()),
	        				Integer.parseInt(changeInventoryField.getText()));
	        		updateInventory();
	        	}
	        }
		}
	}
	
	class RestaurantLabel extends JPanel implements ActionListener {
		String [] staffTypes = {"","Cashier","Cook","Host","Worker1","Worker2"};
		String [] inventoryTypes = {"","Steak","Chicken","Salad","Pizza"};
		
		int restaurantNum;
		RestaurantCookRole cook;
		
		JPanel changeInventory;
		JLabel inventoryLabel;
		JComboBox<String> changeInventoryType;
		JTextField changeInventoryField;
		JButton changeInventoryButton;
		
		JPanel changeStaff;
		JLabel staffLabel;
		JComboBox<String> changeStaffType;
		JTextField changeStaffField;
		JButton changeStaffButton;
		
		public RestaurantLabel(int n) {
			restaurantNum = n;
			
			changeStaff = new JPanel();
			changeStaff.setLayout(new BorderLayout());
	
			changeStaffType = new JComboBox<String>(staffTypes);
			
			staffLabel = new JLabel();
			changeStaffField = new JTextField();
			changeStaffField.setPreferredSize(new Dimension(60,20));
			changeStaffButton = new JButton("Change Staff");
			changeStaffButton.setPreferredSize(new Dimension(120,20));
			changeStaffField.addActionListener(this);
			changeStaffButton.addActionListener(this);
			
			changeStaff.add(changeStaffType,BorderLayout.NORTH);
			changeStaff.add(changeStaffField, BorderLayout.WEST);
			changeStaff.add(changeStaffButton, BorderLayout.EAST);
			
			inventoryLabel = new JLabel();
			changeInventory = new JPanel();
			changeInventory.setLayout(new BorderLayout());
			changeInventory.setPreferredSize(new Dimension(180,60));
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
			

			updateStaff();
			updateInventory();
			
			add(staffLabel);
			add(changeStaff);
			add(inventoryLabel);
			add(changeInventory);
		}
		
		@SuppressWarnings("static-access")
		public void updateStaff() {
			String host = "";
			String cashier = "";
			String cook = "";
//			int waiters = 0 ;
			
			switch(restaurantNum) {
			case 0:
				if(ContactList.AndreRestaurant.host != null) {
				host = ContactList.AndreRestaurant.host.toString();
				cashier = ContactList.AndreRestaurant.cashier.toString();
				cook = ContactList.AndreRestaurant.cook.toString();
				}
				break;
			case 1: //chase ANGELICA
				break;
			case 2:
				if(ContactList.JerrywebRestaurant.host != null) {
				host = ContactList.JerrywebRestaurant.host.toString();
				cashier = ContactList.JerrywebRestaurant.cashier.toString();
				cook = ContactList.JerrywebRestaurant.cook.toString();
				}
				break;
			case 3:
				if(ContactList.MaggiyanRestaurant.mHost != null) {
				host = ContactList.MaggiyanRestaurant.mHost.toString();
				cashier = ContactList.MaggiyanRestaurant.mCashier.toString();
				cook = ContactList.MaggiyanRestaurant.mCook.toString();
				}
				break;
			case 4:
				if(ContactList.DavidRestaurant.host != null) {
				host = ContactList.DavidRestaurant.host.toString();
				cashier = ContactList.DavidRestaurant.cashier.toString();
				cook = ContactList.DavidRestaurant.cook.toString();
				}
				break;
			case 5:
				if(ContactList.SmilehamRestaurant.mHost != null) {
				host = ContactList.SmilehamRestaurant.mHost.toString();
				cashier = ContactList.SmilehamRestaurant.mCashier.toString();
				cook = ContactList.SmilehamRestaurant.mCook.toString();
				}
				break;
			case 6:
				if(ContactList.TranacRestaurant.mHost != null) {
				host = ContactList.TranacRestaurant.mHost.toString();
				cashier = ContactList.TranacRestaurant.mCashier.toString();
				cook = ContactList.TranacRestaurant.mCook.toString();
				}
				break;
			case 7: //rex
				//ANGELICA;
				break;
			}
			
			staffLabel.setText("<html><center>Staff<br><table>" +
			"<tr><td>Host: </td><td>" + host + "</td></tr>" +
			"<tr><td>Cashier: </td><td>" + cashier + "</td></tr>" +
			"<tr><td>Cook: </td><td>" + cook + "</td></tr>" +
//			"<tr><td>Number of Waiters:</td><td>" + waiters + "</td></tr>" +
			"</table></center></html>");
		}
		
		@SuppressWarnings("static-access")
		public void updateInventory() {
			switch(restaurantNum) {
			case 0:
				if(ContactList.AndreRestaurant.cook != null) {
					System.out.println(0);
				cook = ContactList.AndreRestaurant.cook.mRole;
				}
				break;
			case 1: //chase ANGELICA
				break;
			case 2:
				if(ContactList.JerrywebRestaurant.cook != null) {
				cook = ContactList.JerrywebRestaurant.cook.mRole;
				}
				break;
			case 3:
				if(ContactList.MaggiyanRestaurant.mCook != null) {
				cook = ContactList.MaggiyanRestaurant.mCook.mRole;
				}
				break;
			case 4:
				if(ContactList.DavidRestaurant.cook != null) {
				cook = ContactList.DavidRestaurant.cook.mRole;
				System.out.println("k");
				}
				break;
			case 5:
				if(ContactList.SmilehamRestaurant.mCook != null) {
				cook = ContactList.SmilehamRestaurant.mCook.mRole;
				}
				break;
			case 6:
				if(ContactList.TranacRestaurant.mCook != null) {
				cook = ContactList.TranacRestaurant.mCook.mRole;
				}
				break;
			case 7: //rex
				//ANGELICA;
				break;
			}
			
			if(cook != null) {
			inventoryLabel.setText("<html><center>Inventory<br><table>" +
			"<tr><td>Chicken: </td><td>" + cook.getInventory(EnumItemType.CHICKEN) + "</td>" +
			"<td>Steak: </td><td>" + cook.getInventory(EnumItemType.STEAK) + "</td></tr>" +
			"<tr><td>Salad: </td><td>" + cook.getInventory(EnumItemType.SALAD) + "</td>" +
			"<td>Pizza: </td><td>" + cook.getInventory(EnumItemType.PIZZA) + "</td></tr>" +
			"</table></center></html>");
			}
			else {
				inventoryLabel.setText("<html><center>Inventory<br><table>" +
						"<tr><td>Chicken: </td><td>" + "</td>" +
						"<td>Steak: </td><td>" + "</td></tr>" +
						"<tr><td>Salad: </td><td>" + "</td>" +
						"<td>Pizza: </td><td>" + "</td></tr>" +
						"</table></center></html>");
			}
		}
		
		public void actionPerformed(ActionEvent e) {
	        if (e.getSource() == changeStaffField || e.getSource() == changeStaffButton) {
	        	if(!changeStaffType.getSelectedItem().toString().equals("")) {
	        		System.out.println("blah, test");	//ANGELICA:
	        	}
	        }
	        if (e.getSource() == changeInventoryField || e.getSource() == changeInventoryButton) {
	        	if(!changeInventoryType.getSelectedItem().toString().equals("")) {
	        		if(cook != null) {
	        			cook.setInventory(Item.stringToEnum(changeInventoryType.getSelectedItem().toString()),
	        					Integer.parseInt(changeInventoryField.getText()));
	        		}
	        	}
	        }
    		updateStaff();
    		updateInventory();
		}
	}
}
