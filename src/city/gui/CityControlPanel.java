package city.gui;

//import housing.gui.HousingHouseGuiPanel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import base.ConfigParser;

@SuppressWarnings("serial")
public class CityControlPanel extends JPanel implements ActionListener{
	
	SimCityGui city;
	public static final int CP_WIDTH = 200, CP_HEIGHT = 600;
	public static final int numConfigs = 5;
	JButton addRestaurant, addBank, housingGUIButton;
	JPanel configList;
	List<JButton> configOptions;
	int configChoice = -1;
	
	// Title & Pause Button
	JLabel title = new JLabel("Control Panel");
	JButton startButton = new JButton("Start SimCity201");
	
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		startButton.setPreferredSize(pauseBtnDim);
		startButton.setMinimumSize(pauseBtnDim);
		startButton.setMaximumSize(pauseBtnDim);
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.addActionListener(this);
		add(startButton);
		
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
        
        /*
         * Choose Config File
         */
        Dimension panelSize = new Dimension(scrollPaneDim.width - 20, scrollPaneDim.height);
    	configList = new JPanel();
    	configOptions = new ArrayList<JButton>();
    	configList.setPreferredSize(panelSize);
    	configList.setMinimumSize(panelSize);
    	configList.setMaximumSize(panelSize);
    	configList.setLayout(new FlowLayout());
    	
    	Dimension buttonDim = new Dimension(panelSize.width, panelSize.height/numConfigs);
    	for (int i=1; i<numConfigs; i++) {
	    	JButton config = new JButton("Configuration "+i);
	    	config.addActionListener(this);
	    	config.putClientProperty("configFile", i);
	    	config.setPreferredSize(buttonDim);
	    	config.setMinimumSize(buttonDim);
	    	config.setMaximumSize(buttonDim);
	    	configList.add(config);
	    	configOptions.add(config);
    	}
    	view.add(configList);
    	
        add(pane);
        
        // Tabs
        //JLabel filler = new JLabel("Panel #1");
        //filler.setHorizontalAlignment(JLabel.CENTER);
        tab1.setLayout(new GridLayout(1, 1));
        //tab1.add(filler);
        tabbedPane.addTab("Scenario Details", tab1);
//        tabbedPane.addTab("Advanced Configuration", tab2);
        add(tabbedPane);
        
        //tabbedPane.addTab("Tab 1", icon, panel1, "Does nothing");
        //tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
        //Add Buttons
//		addRestaurant = new JButton("Add Restaurant");
//		addRestaurant.addActionListener(this);
//		addRestaurant.setAlignmentX(Component.CENTER_ALIGNMENT);
//		add(addRestaurant);
//		addBank = new JButton("Add Bank");
//		addBank.addActionListener(this);
//		addBank.setAlignmentX(Component.CENTER_ALIGNMENT);
//		add(addBank);	
	}
	
	public void actionPerformed(ActionEvent e) {
		for (int i = 1; i < numConfigs; i++) {
			if (((JButton) e.getSource()).getText()
					.equals("Configuration " + i)) {
				configChoice = i;
			}
			for (JButton b : configOptions) {
				b.setEnabled(false);
			}
		}
		if (e.getSource() == startButton) {
			if (configChoice > 0) {
				ConfigParser config = ConfigParser.getInstanceOf();
				try {
					config.readFileCreatePersons(city, configChoice);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				startButton.setEnabled(false);
			}
		}
	}
}
