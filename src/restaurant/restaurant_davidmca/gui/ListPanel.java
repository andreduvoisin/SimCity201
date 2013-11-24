package restaurant.restaurant_davidmca.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Subpanel of restaurantPanel. This holds the scroll panes for the customers
 * and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {
	public JScrollPane pane = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();

	private JPanel addForm = new JPanel();
	private JPanel addBox = new JPanel();
	
	private List<JButton> list = new ArrayList<JButton>();
	private String type;
	private JButton addPersonB;
	private JCheckBox stateCB;

	private JTextField personName = new JTextField(12);

	private RestaurantPanel restPanel;

	/**
	 * Constructor for ListPanel. Sets up all the gui
	 * 
	 * @param rp
	 *            reference to the restaurant_davidmca panel
	 * @param type
	 *            indicates if this is for customers or waiters
	 */
	public ListPanel(RestaurantPanel rp, String type) {
		restPanel = rp;
		this.type = type;

		stateCB = new JCheckBox();
		stateCB.setText("Hungry?");
		
		
		if (type.equals("Customers")) {
			personName = new JTextField(10);
		}

		if (type.equals("Waiters")) {
			personName = new JTextField(15);
			stateCB.setVisible(false);
		}

		setLayout(new BorderLayout());

		addPersonB = new JButton("Add");
		addPersonB.addActionListener(this);

		addForm.setLayout(new FlowLayout());
		addForm.add(personName);
		addForm.add(stateCB);

		addBox.setLayout(new BorderLayout());
		addBox.add(addForm, BorderLayout.PAGE_START);
		addBox.add(addPersonB, BorderLayout.PAGE_END);

		add(addBox, BorderLayout.PAGE_START);
		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);
		add(pane, BorderLayout.CENTER);
	}

	/**
	 * Method from the ActionListener interface. Handles the event of the add
	 * button being pressed
	 */
	public void actionPerformed(ActionEvent e) {
		/*if (e.getSource() == addPersonB) {
			addPerson(personName.getText(), stateCB.isSelected());
			personName.setText("");
		}

		else {

			for (JButton temp : list) {
				if (e.getSource() == temp)
					restPanel.showInfo(type, temp.getText());
			}
		}*/
	}

	/**
	 * If the add button is pressed, this function creates a spot for it in the
	 * scroll pane, and tells the restaurant_davidmca panel to add a new person.
	 * 
	 * @param name
	 *            name of new person
	 */
	/*public void addPerson(String name, boolean isHungry) {
		if (name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 20,
					(int) (17));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			
			list.add(button);
			view.add(button);
			restPanel.addPerson(type, name, isHungry);// puts customer on list
			restPanel.showInfo(type, name);// updates info based on name and
											// type
			validate();
		}
	}*/

	public void addTable(String x, String y, String numSeats) {
		restPanel.addTable(Integer.parseInt(x), Integer.parseInt(y),
				Integer.parseInt(numSeats));
	}

}
