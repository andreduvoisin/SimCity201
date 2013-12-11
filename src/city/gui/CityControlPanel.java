package city.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import bank.roles.BankTellerRole;
import base.ConfigParser;
import base.ContactList;
import base.Inspection;
import base.Location;
import base.SortingHat;
import city.gui.SimCityPanel.EnumCrashType;
import city.gui.properties.PlacesButtonListener;
import city.gui.properties.PlacesListener;
import city.gui.properties.PlacesPropertiesTab;
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
    //Custom Buttons
    JButton scenarioP;
    JButton scenarioQ;
    JButton scenarioR;
    JButton scenarioS;
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
    String[] tagList = {	"All", 
    						"Person", 
    						"r_duvoisin", 
    						"r_cwagoner", 
    						"r_jerryweb", 
    						"r_maggiyan", 
    						"r_davidmca", 
    						"r_smileham", 
    						"r_tranac", 
    						"r_xurex", 
    						"Bank", 
    						"Market", 
    						"Housing", 
    						"Transportation", 
    						"None"};


    // People panel
    JLabel peopleLabel;
    JLabel jobLabel;
	@SuppressWarnings("rawtypes")
	JComboBox jobs;
    String[] jobList = {	"Housing",
    						"Bank",
    						"Market",
    						"Restaurant",
    						"None"};
    JLabel cashLabel;
	JSpinner cashSpin;
	JLabel nameLabel;
	JTextArea nameArea;
	JButton createButton;
	JLabel fireLabel;
	JButton gringotts;
	JButton piggybank;
	JButton ollivanders;
	JButton honeydukes;
	JButton r0;
	JButton r1;
	JButton r2;
	JButton r3;
	JButton r4;
	JButton r5;
	JButton r6;
	JButton r7;

    //Properties Panel
    @SuppressWarnings("rawtypes")
	JComboBox places;
    String[] placeList = {	"None", 
    						"Gringotts", 
    						"Piggy Bank", 
    						"Ollivanders", 
    						"Honeydukes", 
    						"cwagoner", 
    						"davidmca", 
    						"duvoisin", 
    						"jerryweb", 
    						"maggiyan", 
    						"smileham", 
    						"tranac", 
    						"xurex"};
    
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
//        tabbedPane.setSelectedIndex(1);
        add(tabbedPane);
        
        // Pretty/Ugly View
        toggleGUI = new JButton("Show Beautiful View");
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
		
		//Create labels and buttons
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
	    	scenarioP = new JButton("P: Vehicle Accident");
	    	scenarioQ = new JButton("Q: Vehicle Hits Person");
	    	scenarioR = new JButton("R: Different on Weekends");
	    	scenarioS = new JButton("S: Job Shifts");
    	
    	//Add labels and buttons to tab
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
		
		// Action Listeners.
			scenarioA.addActionListener(getActionListener("A_all_inspect1.txt"));
			scenarioB.addActionListener(getActionListener("B_all_inspect3.txt"));
			scenarioC.addActionListener(getActionListener("C_Market_Cook_Cashier.txt"));
			scenarioD.addActionListener(getActionListener("D_Party_All.txt"));
			scenarioE.addActionListener(getActionListener("E_Bus_Stop.txt"));
			scenarioF.addActionListener(getActionListener("F_Inspection.txt"));
			scenarioG.addActionListener(getActionListener("G_Market_Redeliver.txt"));
			scenarioH.addActionListener(getActionListener("H_Party_Half.txt"));
			scenarioI.addActionListener(getActionListener("I_Party_None.txt"));
			scenarioJ.addActionListener(getActionListener("J_All_Interweave.txt"));
			scenarioO.addActionListener(getActionListener("O_Bank_Robbery.txt"));
			scenarioP.addActionListener(getActionListener("P_Car_Crash.txt"));
			scenarioQ.addActionListener(getActionListener("Q_Person_Crash.txt"));
			scenarioR.addActionListener(getActionListener("R_Weekend.txt"));
			scenarioS.addActionListener(getActionListener("S_Firing.txt"));
	}
	
	//Used to shorten above code
	private ActionListener getActionListener(final String filename){
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()== scenarioP) {
					Timer collisionTimer = new Timer();
					TimerTask detectCrashes = new TimerTask() {
						public void run() {
							SimCityGui.getInstance().citypanel.mCrashScenario = EnumCrashType.VEHICLE_VEHICLE;
						}
					};
					collisionTimer.schedule(detectCrashes, 6500);
				}
				if (e.getSource()== scenarioQ) {
					Timer collisionTimer = new Timer();
					TimerTask detectCrashes = new TimerTask() {
						public void run() {
							SimCityGui.getInstance().citypanel.mCrashScenario = EnumCrashType.PERSON_VEHICLE;
						}
					};
					collisionTimer.schedule(detectCrashes, 6500);
				}
				if (e.getSource() == scenarioR) {
					ContactList.sOpenPlaces.put (ContactList.cBANK1_LOCATION, false);
					ContactList.sOpenPlaces.put (ContactList.cBANK2_LOCATION, false);
					ContactList.sOpenBuildings.put("B1", false);
					ContactList.sOpenBuildings.put("B2", false);
					Inspection.sClosedImages.get(ContactList.cBANK1_LOCATION).enable();
					Inspection.sClosedImages.get(ContactList.cBANK2_LOCATION).enable();
					
					ContactList.sOpenPlaces.put (ContactList.cMARKET1_LOCATION, false);
					ContactList.sOpenBuildings.put("M1", false);
					Inspection.sClosedImages.get(ContactList.cMARKET1_LOCATION).enable();
					
					ContactList.sOpenPlaces.put (ContactList.cRESTAURANT_LOCATIONS.get(3), false);
					ContactList.sOpenPlaces.put (ContactList.cRESTAURANT_LOCATIONS.get(5), false);
					ContactList.sOpenPlaces.put (ContactList.cRESTAURANT_LOCATIONS.get(7), false);
					ContactList.sOpenBuildings.put("R3", false);
					ContactList.sOpenBuildings.put("R5", false);
					ContactList.sOpenBuildings.put("R7", false);
					Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(3)).enable();
					Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(5)).enable();
					Inspection.sClosedImages.get(ContactList.cRESTAURANT_LOCATIONS.get(7)).enable();
				}
				if (e.getSource() == scenarioS) {
					SortingHat.sNumBankTellers++;
					SortingHat.sNumMarketWorkers++;
					SortingHat.sNumRestaurantWaiters++;
				}
				ConfigParser config = ConfigParser.getInstanceOf();
				try {
					config.readFileCreatePersons(city, filename);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		};
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
					case "r_duvoisin":
						tracePanel.showAlertsWithTag(AlertTag.R0);
						break;
					case "r_cwagoner":
						tracePanel.showAlertsWithTag(AlertTag.R1);
						break;
					case "r_jerryweb":
						tracePanel.showAlertsWithTag(AlertTag.R2);
						break;
					case "r_maggiyan":
						tracePanel.showAlertsWithTag(AlertTag.R3);
						break;
					case "r_davidmca":
						tracePanel.showAlertsWithTag(AlertTag.R4);
						break;
					case "r_smileham":
						tracePanel.showAlertsWithTag(AlertTag.R5);
						break;
					case "r_tranac":
						tracePanel.showAlertsWithTag(AlertTag.R6);
						break;
					case "r_xurex":
						tracePanel.showAlertsWithTag(AlertTag.R7);
						break;
					case "Bank":
						tracePanel.showAlertsWithTag(AlertTag.BANK);
						break;
					case "Market":
						tracePanel.showAlertsWithTag(AlertTag.MARKET);
						break;
					case "Housing":
						tracePanel.showAlertsWithTag(AlertTag.HOUSING);
						break;
					case "Transportation":
						tracePanel.showAlertsWithTag(AlertTag.TRANSPORTATION);
						break;
					case "None":
						tracePanel.hideAlertsForAllTags();
						break;
				}
			}
		});
	}



	//CHASE initPeople()
	//PeopleTab
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initPeople() {
		Dimension size = new Dimension(90, 20);
		Dimension preferred = new Dimension(180, 20);

		peopleLabel = new JLabel("Create a Person:");
		peopleLabel.setPreferredSize(preferred);

		jobLabel = new JLabel("Job Type:");
		jobLabel.setPreferredSize(size);
	    jobs = new JComboBox(jobList);
	    jobs.setSelectedIndex(0);
	    jobs.setPreferredSize(size);

	    cashLabel = new JLabel("Initial Cash:");
	    cashLabel.setPreferredSize(size);
	    cashSpin = new JSpinner();
	    cashSpin.setModel(new SpinnerNumberModel(100, 0, 50000, 10));
	    cashSpin.setPreferredSize(size);

	    nameLabel = new JLabel("Name:");
	    nameLabel.setPreferredSize(size);
	    nameArea = new JTextArea();
	    nameArea.setPreferredSize(size);
	    nameArea.addKeyListener(new KeyListener() {
	    	public void keyPressed(KeyEvent e) {
	    		nameArea.setBackground(Color.white);
	    	}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
	    });

	    createButton = new JButton("Create Person");
	    createButton.setPreferredSize(preferred);
	    createButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		CreatePerson();
	    	}
	    });
	    
	    
	    //FIRING PEOPLE
	    fireLabel = new JLabel("Fire person from:");
	    fireLabel.setPreferredSize(new Dimension(180, 50));
	    fireLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    gringotts 	= new JButton("Gringotts");
	    piggybank 	= new JButton("Piggy Bank");
	    ollivanders = new JButton("Ollivanders");
	    honeydukes	= new JButton("Honeydukes");
	    
	    gringotts.setPreferredSize(preferred);
	    piggybank.setPreferredSize(preferred);
	    ollivanders.setPreferredSize(preferred);
	    honeydukes.setPreferredSize(preferred);
	    
	    r0 = new JButton("Duvoisin");
	    r1 = new JButton("Cwagoner");
	    r2 = new JButton("Jerryweb");
	    r3 = new JButton("Maggiyan");
	    r4 = new JButton("Davidmca");
	    r5 = new JButton("Smileham");
	    r6 = new JButton("Tranac");
	    r7 = new JButton("Xurex");
	    
	    r0.setPreferredSize(preferred);
	    r1.setPreferredSize(preferred);
	    r2.setPreferredSize(preferred);
	    r3.setPreferredSize(preferred);	    
	    r4.setPreferredSize(preferred);
	    r5.setPreferredSize(preferred);
	    r6.setPreferredSize(preferred);
	    r7.setPreferredSize(preferred);

	    PeopleTab.add(peopleLabel);
	    PeopleTab.add(jobLabel);
	    PeopleTab.add(jobs);
	    PeopleTab.add(cashLabel);
	    PeopleTab.add(cashSpin);
	    PeopleTab.add(nameLabel);
	    PeopleTab.add(nameArea);
	    PeopleTab.add(createButton);
	    PeopleTab.add(fireLabel);
	    PeopleTab.add(gringotts);
	    PeopleTab.add(piggybank);
	    PeopleTab.add(ollivanders);
	    PeopleTab.add(honeydukes);
	    PeopleTab.add(r0);
	    PeopleTab.add(r1);
	    PeopleTab.add(r2);
	    PeopleTab.add(r3);
	    PeopleTab.add(r4);
	    PeopleTab.add(r5);
	    PeopleTab.add(r6);
	    PeopleTab.add(r7);
	    
	    //ACTION LISTENERS FOR FIRE BUTTONS
	    gringotts.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
//	    		System.out.println("test"); //This works
	    		//SHANE 0 Action Listener Here
	    		
	    		//fire teller
	    		List<BankTellerRole> tellers = ContactList.sBankList.get(0).mTellers;
	    		BankTellerRole teller = tellers.get(tellers.size()-1);
	    		teller.fired();
	    		
	    		//get new person
	    	}
	    });
	    piggybank.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    ollivanders.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    honeydukes.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    r0.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    r1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    r2.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    r3.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    r4.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    r5.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    r6.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	    r7.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//
	    	}
	    });
	}

	private void CreatePerson() {
		if (nameArea.getText().equals("")) {
			nameArea.setBackground(Color.pink);
			return;
		}
		StringBuilder configString = new StringBuilder();
		configString.append(jobs.getSelectedItem().toString().toUpperCase() + " ");
		configString.append(cashSpin.getValue().toString() + " ");
		configString.append(nameArea.getText());

		try {
			ConfigParser.getInstanceOf().readFileCreatePersons(SimCityGui.getInstance(), configString.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}



	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initProperties() {
		// North: COMBOBOX
		JPanel holdPlaces = new JPanel();
		holdPlaces.setLayout(new BorderLayout());
		holdPlaces.setPreferredSize(new Dimension(180,50));
		
		JLabel placesTitle = new JLabel("Select Property:");
	    placesTitle.setPreferredSize(new Dimension(180,20));

    	JButton change = new JButton("None");
	    change.setPreferredSize(new Dimension(180,20));
	    change.setMaximumSize(new Dimension(180,20));
	    change.setMinimumSize(new Dimension(180,20));
	    change.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JButton disable = new JButton("DISABLE ALL");
	    disable.setPreferredSize(new Dimension(180,20));
	    disable.setMaximumSize(new Dimension(180,20));
	    disable.setMinimumSize(new Dimension(180,20));
	    disable.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JButton enable = new JButton("ENABLE ALL");
	    enable.setPreferredSize(new Dimension(180,20));
	    enable.setMaximumSize(new Dimension(180,20));
	    enable.setMinimumSize(new Dimension(180,20));
	    enable.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    PlacesPropertiesTab placeProperties = new PlacesPropertiesTab();
	    
	    places = new JComboBox(placeList);
	    places.setPreferredSize(new Dimension(180,20));
	    places.setSelectedIndex(0);
	    places.addActionListener(new PlacesListener(change, places, placeProperties));
	    
	//    holdPlaces.add(placesTitle, BorderLayout.NORTH);
	//    holdPlaces.add(places, BorderLayout.CENTER);
    	
		/*Dimension buttonDim = enable.getPreferredSize();
		buttonDim.height -= 4;
		buttonDim.width = 180;
		enable.setPreferredSize(buttonDim);*/
	    
	    // Add to TraceTab
	    PropertiesTab.setLayout(new BoxLayout(PropertiesTab,BoxLayout.Y_AXIS));
	    PropertiesTab.add(new JLabel());
	    PropertiesTab.add(placesTitle);
	    PropertiesTab.add(places);
	    PropertiesTab.add(change);
	    PropertiesTab.add(disable);
	    PropertiesTab.add(enable);
	    PropertiesTab.add(placeProperties);
	    PropertiesTab.add(holdPlaces);
	    
	    //Add ActionListener for Change
	    change.addActionListener(new PlacesButtonListener(change, places));	
	    
    	disable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized(ContactList.sOpenPlaces) {
					for(Location iLocation : ContactList.sOpenPlaces.keySet()){
						ContactList.sOpenPlaces.put(iLocation, false);
						Inspection.sClosedImages.get(iLocation).enable();
					}
				}
				synchronized(ContactList.sOpenBuildings) {
					ContactList.sOpenBuildings.put("B1", false);
					ContactList.sOpenBuildings.put("B2", false);
					ContactList.sOpenBuildings.put("M1", false);
					ContactList.sOpenBuildings.put("M2", false);
					ContactList.sOpenBuildings.put("R0", false);
					ContactList.sOpenBuildings.put("R1", false);
					ContactList.sOpenBuildings.put("R2", false);
					ContactList.sOpenBuildings.put("R3", false);
					ContactList.sOpenBuildings.put("R4", false);
					ContactList.sOpenBuildings.put("R5", false);
					ContactList.sOpenBuildings.put("R6", false);
					ContactList.sOpenBuildings.put("R7", false);
				}
			}
		});
    	
    	enable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized(ContactList.sOpenPlaces) {
					for(Location iLocation : ContactList.sOpenPlaces.keySet()){
						ContactList.sOpenPlaces.put(iLocation, true);
						Inspection.sClosedImages.get(iLocation).disable();
					}
				}
				synchronized(ContactList.sOpenBuildings) {
					ContactList.sOpenBuildings.put("B1", true);
					ContactList.sOpenBuildings.put("B2", true);
					ContactList.sOpenBuildings.put("M1", true);
					ContactList.sOpenBuildings.put("M2", true);
					ContactList.sOpenBuildings.put("R0", true);
					ContactList.sOpenBuildings.put("R1", true);
					ContactList.sOpenBuildings.put("R2", true);
					ContactList.sOpenBuildings.put("R3", true);
					ContactList.sOpenBuildings.put("R4", true);
					ContactList.sOpenBuildings.put("R5", true);
					ContactList.sOpenBuildings.put("R6", true);
					ContactList.sOpenBuildings.put("R7", true);
				}
			}
		});
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		
	}
}
