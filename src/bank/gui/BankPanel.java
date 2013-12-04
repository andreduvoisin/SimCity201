package bank.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import restaurant.restaurant_duvoisin.roles.AndreCustomerRole;
import city.gui.CityCard;
import city.gui.SimCityGui;
import bank.interfaces.BankGuard;
import bank.roles.BankCustomerRole;
import bank.roles.BankGuardRole;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import base.Gui;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;


public class BankPanel extends CityCard implements ActionListener{

	private int WINDOWX = 500;
	private int WINDOWY = 500;
	static final int TIMERDELAY = 5;
	
	private Image image;
    private Dimension bufferSize;
    public List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	
	Timer timer;
	private List<Gui> guis = new ArrayList<Gui>();
	
	static final int COUNTER_X = 0;
	static final int COUNTER_Y = 445;
	static final int COUNTER_SIZE_X = 500;
	static final int COUNTER_SIZE_Y = 15;
	
	static final int LINE_X = 225;
	static final int LINE_Y = 320;
	static final int LINE_INCREMENT = -25;	// in the y
	static int LINE_POSITION = 0;
	
	public BankGuardRole guard;
	public BankMasterTellerRole masterTeller;
	public BankTellerRole teller;
	public Vector<BankCustomerRole> customers = new Vector<BankCustomerRole>();
	
	public BankPanel(SimCityGui city) {
		super(city);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		
		timer = new Timer(TIMERDELAY, this);
    	timer.start();
    	    	
//    	guard.mGUI = new BankGuardGui(guard, this);
    	//addGui(guard.mGUI);
//    	teller.mGUI = new BankTellerGui(teller, this);
    	//addGui(teller.mGUI);
    	
    	//testBankGui();
    	
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/bankbg.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
	}
	
	public void testBankGui() {
		PersonAgent cust1 = new PersonAgent();
		BankCustomerRole bcr1 = new BankCustomerRole(cust1, 1);
		BankCustomerGui bcg1 = new BankCustomerGui(bcr1, this);
		bcr1.setGui(bcg1);
		addGui(bcg1);
		
		PersonAgent cust2 = new PersonAgent();
		BankCustomerRole bcr2 = new BankCustomerRole(cust2, 1);
		BankCustomerGui bcg2 = new BankCustomerGui(bcr2, this);
		bcr2.setGui(bcg2);
		addGui(bcg2);
		
		PersonAgent cust3 = new PersonAgent();
		BankCustomerRole bcr3 = new BankCustomerRole(cust3, 1);
		BankCustomerGui bcg3 = new BankCustomerGui(bcr3, this);
		bcr3.setGui(bcg3);
		addGui(bcg3);
		
		PersonAgent teller1 = new PersonAgent();
		BankTellerRole btr1 = new BankTellerRole(teller1, 1);
		BankTellerGui btg1 = new BankTellerGui(btr1, this, 1);
		btr1.setGui(btg1);
		addGui(btg1);
		
		PersonAgent teller2 = new PersonAgent();
		BankTellerRole btr2 = new BankTellerRole(teller2, 1);
		BankTellerGui btg2 = new BankTellerGui(btr2, this, 2);
		btr2.setGui(btg2);
		addGui(btg2);

		PersonAgent teller3 = new PersonAgent();
		BankTellerRole btr3 = new BankTellerRole(teller3, 1);
		BankTellerGui btg3 = new BankTellerGui(btr3, this, 3);
		btr3.setGui(btg3);
		addGui(btg3);
		
		PersonAgent guard = new PersonAgent();
		BankGuardRole bgr = new BankGuardRole(guard, 1);
		BankGuardGui bgg = new BankGuardGui(bgr, this);
		bgr.setGui(bgg);
		addGui(bgg);
		
		//Actions
		bcg1.DoGoWaitInLine();
		bcg2.DoGoWaitInLine();
		bcg3.DoGoWaitInLine();
		bcg1.DoGoToTeller(1);
		bcg2.DoGoToTeller(2);
		bcg3.DoGoToTeller(3);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        g2.drawImage(image, 0, 0, null); 
        
//        //Counter
//        g2.setColor(Color.GRAY);
//        g2.fillRect(COUNTER_X, COUNTER_Y, COUNTER_SIZE_X, COUNTER_SIZE_Y);
        
        // temp square, showing beginning of line
       // g2.fillRect(LINE_X, LINE_Y, 20, 20);
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
	}
	
	public void updateCustomerLine() {
		LINE_POSITION--;
		for(Gui gui : guis) {
            if (gui.isPresent()) {
                if(gui instanceof BankCustomerGui) {
                	((BankCustomerGui) gui).moveForwardInLine();
                }
            }
        }
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
	
	public void addGui(Gui gui) {
		guis.add(gui);
	}
	
	public void addPerson(Role role) {
		if(role instanceof BankCustomerRole) {
			customers.add((BankCustomerRole)role);
			((BankCustomerRole)role).setGuard(guard);
			((BankCustomerRole)role).setTeller(teller);
			((BankCustomerRole)role).mGUI = new BankCustomerGui((BankCustomerRole)role, this);
			addGui(((BankCustomerRole)role).mGUI);
		}
	}
}
