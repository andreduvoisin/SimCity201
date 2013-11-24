package bank.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Timer;

import city.gui.CityCard;
import city.gui.SimCityGui;
import bank.roles.BankCustomerRole;
import bank.roles.BankGuardRole;
import bank.roles.BankTellerRole;
import base.Gui;
import base.PersonAgent;
import base.interfaces.Person;


public class BankPanel extends CityCard implements ActionListener{
	private static BankPanel instance = null;
	private int WINDOWX = 500;
	private int WINDOWY = 500;
	static final int TIMERDELAY = 5;
	
	private Image bufferImage;
    private Dimension bufferSize;
    public List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	
	Timer timer;
	private List<Gui> guis = new ArrayList<Gui>();
	
	static final int COUNTER_X = 0;
	static final int COUNTER_Y = 445;
	static final int COUNTER_SIZE_X = 500;
	static final int COUNTER_SIZE_Y = 15;
	
	static final int LINE_X = 250;
	static final int LINE_Y = 350;
	static final int LINE_INCREMENT = -25;	// in the y
	static int LINE_POSITION = 0;
	
	static final int INTERACT_X = 250;
	static final int INTERACT_Y = 420;
	
	public BankPanel(SimCityGui city) {
		super(city);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		
		timer = new Timer(TIMERDELAY, this);
    	timer.start();
    	
    	testBankGui();
	}
	
	public void testBankGui() {
		PersonAgent cust = new PersonAgent();
		BankCustomerRole bcr = new BankCustomerRole(cust);
		BankCustomerGui bcg = new BankCustomerGui(bcr);
		bcr.setGui(bcg);
		addGui(bcg);
		bcg.DoGoWaitInLine();
		bcg.DoGoToTeller();
		
		PersonAgent cust2 = new PersonAgent();
		BankCustomerRole bcr2 = new BankCustomerRole(cust2);
		BankCustomerGui bcg2 = new BankCustomerGui(bcr2);
		bcr2.setGui(bcg2);
		addGui(bcg2);
		bcg2.DoGoWaitInLine();
		
		PersonAgent cust3 = new PersonAgent();
		BankCustomerRole bcr3 = new BankCustomerRole(cust3);
		BankCustomerGui bcg3 = new BankCustomerGui(bcr3);
		bcr3.setGui(bcg3);
		addGui(bcg3);
		bcg3.DoGoWaitInLine();
		
		PersonAgent teller = new PersonAgent();
		BankTellerRole btr = new BankTellerRole(teller);
		BankTellerGui btg = new BankTellerGui(btr);
		btr.setGui(btg);
		addGui(btg);
		
		PersonAgent guard = new PersonAgent();
		BankGuardRole bgr = new BankGuardRole(guard);
		BankGuardGui bgg = new BankGuardGui(bgr);
		bgr.setGui(bgg);
		addGui(bgg);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        //Counter
        g2.setColor(Color.GRAY);
        g2.fillRect(COUNTER_X, COUNTER_Y, COUNTER_SIZE_X, COUNTER_SIZE_Y);
        
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
		for(Gui gui : guis) {
            if (gui.isPresent()) {
                if(gui instanceof BankCustomerGui) {
                	
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
}
