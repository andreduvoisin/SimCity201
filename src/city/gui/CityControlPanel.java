package city.gui;

//import housing.gui.HousingHouseGuiPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import city.gui.CityComponent.EnumCityComponents;

public class CityControlPanel extends JPanel implements ActionListener{
	
	SimCityGui city;
	public static final int CP_WIDTH = 200, CP_HEIGHT = 700;
	JButton addRestaurant, addBank, housingGUIButton;
	
	// Title & Pause Button
	JLabel title = new JLabel("Control Panel");
	JButton pauseButton = new JButton("Start SimCity201");
	
	/* ANDRE JERRY No days of week - discuss w/Shane
	// Select Day
	JLabel dayTitle = new JLabel("Day of Week:");
	String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	JComboBox currentDay = new JComboBox(days);
	*/

	// Scenarios
	JLabel scenarioTitle = new JLabel("Load Selected Scenario:");
	JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JPanel view = new JPanel();
    
    // Tabs
    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel tab1 = new JPanel();
    JPanel tab2 = new JPanel();
	
	public CityControlPanel(SimCityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setVisible(true);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Title
		Font font = title.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		attributes.put(TextAttribute.SIZE, 20);
		title.setFont(font.deriveFont(attributes));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title);
		
		// Pause Button
		Dimension pauseBtnDim = new Dimension(CP_WIDTH - 10, 30);
		pauseButton.setPreferredSize(pauseBtnDim);
		pauseButton.setMinimumSize(pauseBtnDim);
		pauseButton.setMaximumSize(pauseBtnDim);
		pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(pauseButton);
		
//		// Select Day
//		dayTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
//		add(dayTitle);
//		
//		Dimension dayCBDim = new Dimension(CP_WIDTH - 10, 25);
//		currentDay.setPreferredSize(dayCBDim);
//		currentDay.setMinimumSize(dayCBDim);
//		currentDay.setMaximumSize(dayCBDim);
//		add(currentDay);
		
		scenarioTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(scenarioTitle);
		
		// Scenario
		view.setLayout(new BoxLayout((Container)view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        
        Dimension scrollPaneDim = new Dimension(CP_WIDTH - 10, 100);
        pane.setPreferredSize(scrollPaneDim);
        pane.setMinimumSize(scrollPaneDim);
        pane.setMaximumSize(scrollPaneDim);
        
        Dimension panelSize = new Dimension(scrollPaneDim.width - 20, (int)(scrollPaneDim.height / 5));
    	JPanel temp = new JPanel();
    	temp.setPreferredSize(panelSize);
    	temp.setMinimumSize(panelSize);
    	temp.setMaximumSize(panelSize);
    	temp.setLayout(new BorderLayout());
    	JButton tempBU = new JButton("Default Scenario");
    	tempBU.setPreferredSize(panelSize);
    	tempBU.setMinimumSize(panelSize);
    	tempBU.setMaximumSize(panelSize);
    	temp.add(tempBU, BorderLayout.CENTER);
    	view.add(temp);
    	
        add(pane);
        
        // Tabs
        //JLabel filler = new JLabel("Panel #1");
        //filler.setHorizontalAlignment(JLabel.CENTER);
        tab1.setLayout(new GridLayout(1, 1));
        //tab1.add(filler);
        tabbedPane.addTab("Scenario Details", tab1);
        tabbedPane.addTab("Advanced Configuration", tab2);
        add(tabbedPane);
        
        //tabbedPane.addTab("Tab 1", icon, panel1, "Does nothing");
        //tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
        //Add Buttons
		addRestaurant = new JButton("Add Restaurant");
		addRestaurant.addActionListener(this);
		addRestaurant.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(addRestaurant);
		addBank = new JButton("Add Bank");
		addBank.addActionListener(this);
		addBank.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(addBank);
		//Hack for Testing: DAVID 
		housingGUIButton = new JButton("Start Housing GUI");
		housingGUIButton.addActionListener(this);
		housingGUIButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(housingGUIButton);		
	}
	
	public void actionPerformed(ActionEvent e) { 
		if (e.getSource().equals(housingGUIButton)) {
			JFrame HousingGUITest = new JFrame("HousingGUITest");
			HousingGUITest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//HousingGUITest.getContentPane().add(HousingHouseGuiPanel.getInstance(), BorderLayout.CENTER);
			HousingGUITest.setLayout(new GridLayout(1, 1));
			HousingGUITest.setSize(300,400);
			HousingGUITest.setVisible(true);
		}
	}
}
