package city.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import base.ConfigParser;
import city.gui.trace.AlertLevel;
import city.gui.trace.AlertLog;
import city.gui.trace.AlertTag;
import city.gui.trace.TracePanel;

@SuppressWarnings("serial")
public class CityControlPanel extends JPanel implements ActionListener{
	SimCityGui city;
	public static final int CP_WIDTH = 200, CP_HEIGHT = 600;
	public static final int TABBED_HEIGHT_ADJ = 60;
	
	// Title & Display Button
	JLabel title;
	JButton toggleGUI;
    
    // Tabs
    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel ScenariosTab = new JPanel();
    JPanel PeopleTab = new JPanel();
    JPanel PropertiesTab = new JPanel();
    JPanel TraceTab = new JPanel();
    
    // Trace Panel
    TracePanel tracePanel;
    // Selection for Trace Panel
    JPanel traceSelectionPanel;
    // Levels
    JCheckBox CBLevel_ERROR;
    JCheckBox CBLevel_WARNING;
    JCheckBox CBLevel_INFO;
    JCheckBox CBLevel_MESSAGE;
    JCheckBox CBLevel_DEBUG;
    // Tags
    @SuppressWarnings("rawtypes")
	JComboBox tags;
    String[] tagList = {"All", "Person", "Bank Teller", "Bank Customer", "Bus Stop", "Restaurant", "Bank", "General City"};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CityControlPanel(SimCityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setMinimumSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setMaximumSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setVisible(true);
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 2));
		
		// Title
		title = new JLabel("Control Panel");
		Font font = title.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		attributes.put(TextAttribute.SIZE, 20);
		title.setFont(font.deriveFont(attributes));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title);
        
		// Tabs
		Dimension tabbedPaneDim = new Dimension(CP_WIDTH, CP_HEIGHT - TABBED_HEIGHT_ADJ);
        tabbedPane.setPreferredSize(tabbedPaneDim);
        tabbedPane.setMinimumSize(tabbedPaneDim);
        tabbedPane.setMaximumSize(tabbedPaneDim);
        
        // 3 4 1 2
        tabbedPane.addTab("People", PeopleTab);
        tabbedPane.addTab("Properties", PropertiesTab);
        tabbedPane.addTab("Scenarios", ScenariosTab);
        tabbedPane.addTab("Trace", TraceTab);
        tabbedPane.setSelectedIndex(2);	// Defaults to "Scenarios"
        add(tabbedPane);
        
        // Pretty/Ugly View
        toggleGUI = new JButton("Show Grading View");
        Dimension tgDim = toggleGUI.getPreferredSize();
        tgDim.width += 10;
        toggleGUI.setPreferredSize(tgDim);
        toggleGUI.setMinimumSize(tgDim);
        toggleGUI.setMaximumSize(tgDim);
        toggleGUI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(SimCityGui.GRADINGVIEW)
					toggleGUI.setText("Show Grading View");
				else
					toggleGUI.setText("Show Beautiful View");
				
				SimCityGui.GRADINGVIEW = !SimCityGui.GRADINGVIEW;
			}
		});
        add(toggleGUI);
        
        // Scenarios Panel
        initScenarios();
        
        // Trace Panel
        initTrace();
        
        // People Panel
        initPeople();
        
        // Properties Panel
        initProperties();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initScenarios() {
		//ScenariosTab.setLayout(new GridLayout(18, 1));
		
    	JLabel label1 = new JLabel("Normative - Baseline");
    	JButton scenarioA = new JButton("A: All Behaviors");
    	JButton scenarioB = new JButton("B: All Behaviors");
    	JButton scenarioC = new JButton("C: Cook/Cashier/Market");
    	JButton scenarioD = new JButton("D: Parties");
    	JButton scenarioE = new JButton("E: Bus Stops");
    	
    	JLabel label2 = new JLabel("Non-Normative - Baseline");
    	JButton scenarioF = new JButton("F: Can't Visit Building");
    	JButton scenarioG = new JButton("G: Market");
    	JButton scenarioH = new JButton("H: Party with Flakes");
    	JButton scenarioI = new JButton("I: Party Cancelled");
    	
    	JLabel label3 = new JLabel("Normative - Interleaving");
    	JButton scenarioJ = new JButton("J: Simulate All");
    	
    	JLabel label4 = new JLabel("Non-Normative - We Design");
    	JButton scenarioO = new JButton("O: Bank Robbery");
    	JButton scenarioP = new JButton("P: Vehicle Accident");
    	JButton scenarioQ = new JButton("Q: Vehicle Hits Person");
    	JButton scenarioR = new JButton("R: Different on Weekends");
    	JButton scenarioS = new JButton("S: Job Shifts");
    	
    	ScenariosTab.add(label1);
    	ScenariosTab.add(scenarioA);
    	ScenariosTab.add(scenarioB);
    	ScenariosTab.add(scenarioC);
    	ScenariosTab.add(scenarioD);
    	ScenariosTab.add(scenarioE);

    	ScenariosTab.add(label2);
    	ScenariosTab.add(scenarioF);
    	ScenariosTab.add(scenarioG);
    	ScenariosTab.add(scenarioH);
    	ScenariosTab.add(scenarioI);
    	
    	ScenariosTab.add(label3);
    	ScenariosTab.add(scenarioJ);
    	
    	ScenariosTab.add(label4);
    	ScenariosTab.add(scenarioO);
    	ScenariosTab.add(scenarioP);
    	ScenariosTab.add(scenarioQ);
    	ScenariosTab.add(scenarioR);
    	ScenariosTab.add(scenarioS);
    	
    	// Underline Labels
    	Font font = label1.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		label1.setFont(font.deriveFont(attributes));
		label2.setFont(font.deriveFont(attributes));
		label3.setFont(font.deriveFont(attributes));
		label4.setFont(font.deriveFont(attributes));
		
		// Dimensions of Buttons
		Dimension buttonDim = scenarioA.getPreferredSize();
		buttonDim.height -= 4;
		buttonDim.width = 180;
		scenarioA.setPreferredSize(buttonDim);
		scenarioB.setPreferredSize(buttonDim);
		scenarioC.setPreferredSize(buttonDim);
		scenarioD.setPreferredSize(buttonDim);
		scenarioE.setPreferredSize(buttonDim);
		scenarioF.setPreferredSize(buttonDim);
		scenarioG.setPreferredSize(buttonDim);
		scenarioH.setPreferredSize(buttonDim);
		scenarioI.setPreferredSize(buttonDim);
		scenarioJ.setPreferredSize(buttonDim);
		scenarioO.setPreferredSize(buttonDim);
		scenarioP.setPreferredSize(buttonDim);
		scenarioQ.setPreferredSize(buttonDim);
		scenarioR.setPreferredSize(buttonDim);
		scenarioS.setPreferredSize(buttonDim);
		
		// Action Listeners. THIS IS WHAT MAKES SHIT HAPPEN WHEN YOU CLICK A BUTTON
		scenarioA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigParser config = ConfigParser.getInstanceOf();
				try {
					config.readFileCreatePersons(city, "restConfig0.txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		scenarioB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioJ.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioQ.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		scenarioS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initTrace() {
		tracePanel = new TracePanel();
		
		// Show/Hide Alerts (defaults set here)
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();
		
		// Action Listener
		AlertLog.getInstance().addAlertListener(tracePanel);
		
		// Trace Selection Panel Panel
		traceSelectionPanel = new JPanel();
		traceSelectionPanel.setLayout(new BorderLayout(0, 5));
		
		// NOTE: Must do SOUTH before NORTH or ComboBox gets overlapped. lol
	    // South: JCheckBoxES
		JPanel holdLevels = new JPanel();
		holdLevels.setLayout(new BorderLayout());
		
		JLabel levelsTitle = new JLabel("Select Levels to View:");
		
		JPanel holdCBs = new JPanel();
		holdCBs.setLayout(new GridLayout(3, 2));
	    CBLevel_ERROR = new JCheckBox("Errors", true);
	    CBLevel_WARNING = new JCheckBox("Warnings", true);
	    CBLevel_INFO = new JCheckBox("Info", true);
	    CBLevel_MESSAGE = new JCheckBox("Messages", true);
	    CBLevel_DEBUG = new JCheckBox("Debugs", true);
	    holdCBs.add(CBLevel_ERROR);
	    holdCBs.add(CBLevel_WARNING);
	    holdCBs.add(CBLevel_INFO);
	    holdCBs.add(CBLevel_MESSAGE);
	    holdCBs.add(CBLevel_DEBUG);
	    
	    holdLevels.add(levelsTitle, BorderLayout.NORTH);
	    holdLevels.add(holdCBs, BorderLayout.CENTER);
	    traceSelectionPanel.add(holdLevels, BorderLayout.CENTER);
	    
		// North: COMBOBOX
		JPanel holdTags = new JPanel();
		holdTags.setLayout(new BorderLayout());
		
		JLabel tagsTitle = new JLabel("Select Tags to View:");
		
	    tags = new JComboBox(tagList);
	    tags.setSelectedIndex(0);
	    
	    holdTags.add(tagsTitle, BorderLayout.NORTH);
	    holdTags.add(tags, BorderLayout.CENTER);
	    traceSelectionPanel.add(holdTags, BorderLayout.NORTH);
	    
	    // Add to TraceTab
	    TraceTab.setLayout(new BorderLayout(0, 5));
        TraceTab.add(traceSelectionPanel, BorderLayout.NORTH);
        TraceTab.add(tracePanel, BorderLayout.CENTER);
	    
	    // Add action listeners.
	    CBLevel_ERROR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_ERROR.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
				}
			}
		});
	    CBLevel_WARNING.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_WARNING.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.WARNING);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.WARNING);
				}
			}
		});
	    CBLevel_INFO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_INFO.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.INFO);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.INFO);
				}
			}
		});
	    CBLevel_MESSAGE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_MESSAGE.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
				}
			}
		});
	    CBLevel_DEBUG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_DEBUG.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.DEBUG);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
				}
			}
		});
	    
	    tags.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tracePanel.hideAlertsForAllTags();
				switch((String)tags.getSelectedItem()) {
					case "All":
						tracePanel.showAlertsForAllTags();
						break;
					case "Person":
						tracePanel.showAlertsWithTag(AlertTag.PERSON);
						break;
					case "Bank Teller":
						tracePanel.showAlertsWithTag(AlertTag.BANK_TELLER);
						break;
					case "Bank Customer":
						tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
						break;
					case "Bus Stop":
						tracePanel.showAlertsWithTag(AlertTag.BUS_STOP);
						break;
					case "Restaurant":
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANT);
						break;
					case "Bank":
						tracePanel.showAlertsWithTag(AlertTag.BANK);
						break;
					case "General City":
						tracePanel.showAlertsWithTag(AlertTag.GENERAL_CITY);
						break;
				}
			}
		});
	}
	
	public void initPeople() {
		
	}
	
	public void initProperties() {
		
	}
	
	public void actionPerformed(ActionEvent e) {		
		// JButton
		if(e.getSource() instanceof JButton) {
			for (int i = 0; i < 8; i++) {
				if (((JButton) e.getSource()).getText()
						.equals("Restaurant " + i)) {
					try {
						ConfigParser.getInstanceOf().readFileCreatePersons(city, "restConfig"+i+".txt");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
			if (((JButton) e.getSource()).getText().equals("Bank")) {
				try {
					ConfigParser.getInstanceOf().readFileCreatePersons(city, "BankConfig"+".txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			if (((JButton) e.getSource()).getText().equals("Housing")) {
				try {
					ConfigParser.getInstanceOf().readFileCreatePersons(city, "HouseConfig"+".txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			if (((JButton) e.getSource()).getText().equals("Food Market")) {
				try {
					ConfigParser.getInstanceOf().readFileCreatePersons(city, "marketConfig"+".txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			if (((JButton) e.getSource()).getText().equals("Party")) {
				try {
					ConfigParser.getInstanceOf().readFileCreatePersons(city, "PartyConfig"+".txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			if (((JButton) e.getSource()).getText().equals("Simulate All")) {
				try {
					ConfigParser.getInstanceOf().readFileCreatePersons(city, "config1.txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
