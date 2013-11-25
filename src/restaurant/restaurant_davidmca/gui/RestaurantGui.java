package restaurant.restaurant_davidmca.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import city.gui.CityCard;
import city.gui.CityPanel;
import city.gui.SimCityGui;
import restaurant.restaurant_davidmca.interfaces.Customer;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import restaurant.restaurant_davidmca.roles.CustomerRole;
import restaurant.restaurant_davidmca.roles.WaiterRole;

/**
 * Main GUI class. Contains the main frame and subsequent panels
 */
public class RestaurantGui extends CityCard implements ActionListener {

	AnimationPanel animationPanel = new AnimationPanel();

	private RestaurantPanel restPanel = new RestaurantPanel(this);

	/* infoPanel holds information about the clicked customer, if there is one */
	boolean currentlyPaused = false;

	private JPanel leftPanel;

	private JPanel textBoxes;

	private JPanel infoPanel;
	private JLabel infoLabel; // part of infoPanel
	private JCheckBox stateCB;// part of infoLabel
	private JCheckBox breakCB;

	private JPanel aboutPanel;
	private JLabel aboutLabel;

	private ListPanel customerPanel;
	private ListPanel waiterPanel;

	private Object currentPerson;

	/**
	 * Constructor for RestaurantGui class. Sets up all the gui components.
	 * 
	 * @throws IOException
	 */

	public RestaurantGui(SimCityGui city) throws IOException {
		super(city);
		int WINDOWX = 500;
		int WINDOWY = 500;
		int PAD = 0;

		setBounds(0, 0, WINDOWX, WINDOWY);

		setLayout(new GridLayout(1, 2));

		/*// Left panel
		Dimension leftSize = new Dimension(WINDOWX / 2 - PAD, WINDOWY - PAD);
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(leftSize);
		leftPanel.setMinimumSize(leftSize);
		leftPanel.setMaximumSize(leftSize);
		leftPanel.setLayout(new FlowLayout());

		// Text Boxes Panel
		Dimension textBoxesSize = new Dimension(WINDOWX / 4 - PAD, WINDOWY / 2);
		textBoxes = new JPanel();
		textBoxes.setPreferredSize(textBoxesSize);
		textBoxes.setMinimumSize(textBoxesSize);
		textBoxes.setMaximumSize(textBoxesSize);
		textBoxes.setLayout(new FlowLayout());

		// Now, setup the info panel
		Dimension infoDim = new Dimension(WINDOWX / 4 - PAD, WINDOWY / 4 - PAD
				/ 2);
		infoPanel = new JPanel();
		infoPanel.setPreferredSize(infoDim);
		infoPanel.setMinimumSize(infoDim);
		infoPanel.setMaximumSize(infoDim);
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
		infoPanel.setLayout(new BorderLayout());
		infoLabel = new JLabel();
		infoLabel
				.setText("<html>Use the name fields and <br>Add buttons below to add <br>customers and waiters</html>");
		stateCB = new JCheckBox();
		stateCB.setVisible(false);
		stateCB.addActionListener(this);

		breakCB = new JCheckBox();
		breakCB.setVisible(false);
		breakCB.addActionListener(this);

		infoPanel.add(infoLabel, BorderLayout.LINE_START);
		infoPanel.add(stateCB, BorderLayout.LINE_END);
		infoPanel.add(breakCB, BorderLayout.PAGE_END);
		textBoxes.add(infoPanel);

		// Restaurant panel
		Dimension restDim = new Dimension(WINDOWX / 4 - PAD, WINDOWY / 2 - PAD);
		restPanel.setBorder(BorderFactory.createTitledBorder("Restaurant"));
		restPanel.setPreferredSize(restDim);
		restPanel.setMinimumSize(restDim);
		restPanel.setMaximumSize(restDim);
		leftPanel.add(restPanel);

		// About me
		Dimension aboutDim = new Dimension(WINDOWX / 4 - PAD, WINDOWY / 4 - PAD);
		aboutPanel = new JPanel();
		aboutPanel.setPreferredSize(aboutDim);
		aboutPanel.setMinimumSize(aboutDim);
		aboutPanel.setMaximumSize(aboutDim);
		aboutPanel.setBorder(BorderFactory.createTitledBorder("About"));
		aboutLabel = new JLabel();
		aboutLabel
				.setText("<html>David Carr <br> CSCI 201 Restaurant v2</html>");
		aboutPanel.add(aboutLabel);
		textBoxes.add(aboutPanel);

		leftPanel.add(textBoxes);

		// List Panels
		Dimension listDim = new Dimension(WINDOWX / 4 - PAD, WINDOWY / 2
				- (5 * PAD));
		customerPanel = new ListPanel(restPanel, "Customers");
		customerPanel.setPreferredSize(listDim);
		customerPanel.setMaximumSize(listDim);
		customerPanel.setMinimumSize(listDim);
		customerPanel.setBorder(BorderFactory.createTitledBorder("Customers"));
		leftPanel.add(customerPanel);

		waiterPanel = new ListPanel(restPanel, "Waiters");
		waiterPanel.setPreferredSize(listDim);
		waiterPanel.setMaximumSize(listDim);
		waiterPanel.setMinimumSize(listDim);
		waiterPanel.setBorder(BorderFactory.createTitledBorder("Waiters"));
		leftPanel.add(waiterPanel);

		add(leftPanel);*/

		// Animation Panel
		Dimension animDim = new Dimension(500, 500);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		add(animationPanel);
	}

	/**
	 * updateInfoPanel() takes the given customer (or, for v3, Host) object and
	 * changes the information panel to hold that person's info.
	 * 
	 * @param person
	 *            customer (or waiter) object
	 */
	public void updateInfoPanel(Object person) {
		stateCB.setVisible(false);
		breakCB.setVisible(false);
		currentPerson = person;

		if (person instanceof CustomerRole) {
			Customer customer = (Customer) person;
			stateCB.setVisible(true);
			stateCB.setText("Hungry?");
			stateCB.setSelected(customer.getGui().isHungry());
			stateCB.setEnabled(!customer.getGui().isHungry());
			infoLabel.setText("<html><pre>     Name: " + customer.getName()
					+ " </pre></html>");
		}
		if (person instanceof WaiterRole) {
			Waiter waiter = (Waiter) person;
			breakCB.setVisible(true);
			breakCB.setText("Break?");
			breakCB.setSelected(waiter.getGui().isOnBreak());
			breakCB.setEnabled(!waiter.getGui().isOnBreak());
			infoLabel.setText("<html><pre>     Name: " + waiter.getName()
					+ " </pre></html>");
		}
		infoPanel.validate();
	}

	/**
	 * Action listener method that reacts to the checkbox being clicked; If it's
	 * the customer's checkbox, it will make him hungry For v3, it will propose
	 * a break for the waiter.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == stateCB) {
			if (currentPerson instanceof CustomerRole) {
				Customer c = (Customer) currentPerson;
				c.getGui().setHungry();
				stateCB.setEnabled(false);
			}
		}
		if (e.getSource() == breakCB) {
			if (currentPerson instanceof WaiterRole) {
				Waiter w = (Waiter) currentPerson;
				w.getGui().wantsBreak();
				breakCB.setEnabled(false);
			}
		}
	}

	/**
	 * Message sent from a customer gui to enable that customer's "I'm hungry"
	 * checkbox.
	 * 
	 * @param c
	 *            reference to the customer
	 */
	public void setCustomerEnabled(Customer c) {
		if (currentPerson instanceof CustomerRole) {
			Customer cust = (Customer) currentPerson;
			if (c.equals(cust)) {
				stateCB.setEnabled(true);
				stateCB.setSelected(false);
			}
		}
	}

	/**
	 * Main routine to get gui started
	 * 
	 * @throws IOException
	 */
	/*public static void main(String[] args) throws IOException {
		RestaurantGui gui = new RestaurantGui();
		gui.setTitle("csci201 Restaurant");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}*/
}
