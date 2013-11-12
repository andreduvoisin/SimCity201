package restaurant_smileham.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import restaurant_smileham.Order;
import restaurant_smileham.agent.Agent;
import restaurant_smileham.agents.CashierAgent;
import restaurant_smileham.agents.CookAgent;
import restaurant_smileham.agents.CustomerAgent;
import restaurant_smileham.agents.HostAgent;
import restaurant_smileham.agents.WaiterAgent;
import restaurant_smileham.interfaces.Customer;
import restaurant_smileham.interfaces.Host;
import restaurant_smileham.interfaces.Waiter;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
@SuppressWarnings("serial")
public class AgentPanel extends JPanel implements ActionListener {

	//instance variables
    private RestaurantPanel mRestPanel;
    private Timer mTimer;
    
	//GUI
	private JPanel mAddCustomerPanel;
		private JTextField mAddCustomerField;
		private JLabel mHungryLabel;
		private JCheckBox mHungryCB;
		private JButton mButtonAddCustomer;
		private JLabel mCustomerInfo;
	public JScrollPane mCustomerPane;
    	private JPanel mCustomerView;
	private JPanel mAddWaiterPanel;
		private JTextField mAddWaiterField;
		private JButton mButtonAddWaiter;
		private JLabel mWaiterInfo;
	public JScrollPane mWaiterPane;
    	private JPanel mWaiterView;
	private JButton mButtonPause;
	private JButton mButtonAddMarket;
	
	//HACKS
	private JCheckBox mHackFlakeCB;
	private JCheckBox mHackBusyCB;
	private JPanel mHackCustomerCashPanel;
		private JTextField mHackCustomerCashText;
		private JLabel mHackCustomerCashLabel;
	
    private RestaurantGui mGUI;
    

    /**
     * Constructor for AgentPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     */
    public AgentPanel(RestaurantPanel rp) {
    	//initializations
        mRestPanel = rp;
        mGUI = rp.getGui();
        mTimer = new Timer();
        

        //Layout
        setLayout(new GridLayout(10,1)); //num elements x 1
        setBorder(BorderFactory.createTitledBorder("Agent Panel"));

        //mCustomerPane
        mCustomerPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        	mCustomerView = new JPanel();
        		mCustomerView.setLayout(new BoxLayout((Container) mCustomerView, BoxLayout.Y_AXIS));
        	mCustomerPane.setViewportView(mCustomerView);
        	
        //mAddCustomerPanel
        mAddCustomerPanel = new JPanel();
        	mAddCustomerField = new JTextField();
        		mAddCustomerField.setPreferredSize(new Dimension(40, 20));
        	mHungryCB = new JCheckBox();
        	mHungryLabel = new JLabel("H?");
        	mButtonAddCustomer = new JButton("Add Customer");
        		mButtonAddCustomer.addActionListener(this);
        	mCustomerInfo = new JLabel("Click customer to make hungry");
        		
        	mAddCustomerPanel.add(mAddCustomerField);
        	mAddCustomerPanel.add(mHungryLabel);
        	mAddCustomerPanel.add(mHungryCB);
        	mAddCustomerPanel.add(mButtonAddCustomer);
        	mAddCustomerPanel.add(mCustomerInfo);
        	
    	//mWaiterPane
        mWaiterPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        	mWaiterView = new JPanel();
        		mWaiterView.setLayout(new BoxLayout((Container) mWaiterView, BoxLayout.Y_AXIS));
        	mWaiterPane.setViewportView(mWaiterView);
        	
    	//mAddWaiterPanel
        mAddWaiterPanel = new JPanel();
        	mAddWaiterField = new JTextField();
        		mAddWaiterField.setPreferredSize(new Dimension(80, 20));
        	mButtonAddWaiter = new JButton("Add Waiter");
        		mButtonAddWaiter.addActionListener(this);
        	mWaiterInfo = new JLabel("Click waiter to initiate break");
        		
    		mAddWaiterPanel.add(mAddWaiterField);
    		mAddWaiterPanel.add(mButtonAddWaiter);
    		mAddWaiterPanel.add(mWaiterInfo);
    		
    	
		//Add Market Button
		mButtonAddMarket = new JButton("Add Market (1 of each food)");
    		mButtonAddMarket.addActionListener(this);
        
		//Pause Button
    	mButtonPause = new JButton("PAUSE");
    		mButtonPause.addActionListener(this);
    		
    	//Hacks
    	mHackFlakeCB = new JCheckBox("Customers are flakes", false); //HACK: flake gui
    	mHackBusyCB = new JCheckBox("When full, waiting customers stay"); //HACK: busy gui
    	mHackCustomerCashPanel = new JPanel();
    		mHackCustomerCashText = new JTextField();
    			mHackCustomerCashText.setPreferredSize(new Dimension(80, 20));
    			mHackCustomerCashPanel.add(mHackCustomerCashText);
    		mHackCustomerCashLabel = new JLabel("Initial Customer Cash");
    			mHackCustomerCashPanel.add(mHackCustomerCashLabel);
    		
        		
        //Order
        add(mCustomerPane);
        add(mAddCustomerPanel);
        add(mWaiterPane);
        add(mAddWaiterPanel);
        add(mButtonAddMarket);
        add(mButtonPause);
        add(mHackFlakeCB);
//        add(mHackBusyCB);
        add(mHackCustomerCashPanel);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mButtonAddCustomer) {
        	print("actionPerformed: mButtonAddCustomer pressed");
        	String name = mAddCustomerField.getText();
        	HostAgent host = mRestPanel.getHost();
        	CashierAgent cashier = mRestPanel.getCashier();
        	CustomerAgent customer = new CustomerAgent(name, host, cashier, mGUI); 
        	addPerson(customer);
        }
        else if (e.getSource() == mButtonAddWaiter){
        	HostAgent host = mRestPanel.getHost();
        	if (host.getWaiters().size() >= 3) return;
        	
        	String name = mAddWaiterField.getText();
        	CookAgent cook = (CookAgent) mGUI.getRestaurantPanel().getCook();
        	CashierAgent cashier = (CashierAgent) mGUI.getRestaurantPanel().getCashier();
        	WaiterAgent waiter = new WaiterAgent(name, host, cook, cashier, mGUI);
        	addPerson(waiter);
        }
		else if (e.getSource() == mButtonPause) {
			pauseSimulation();
        }
		else if (e.getSource() == mButtonAddMarket){
			mGUI.getRestaurantPanel().getCook().addMarket();
		}
        //OnClick Customer
		else if (e.getSource() instanceof CustomerRowButton){
			CustomerRowButton crb = (CustomerRowButton) e.getSource();
			CustomerAgent customer = crb.getCustomer();
			customer.msgGotHungry();
		}
        //OnClick Waiter
		else if (e.getSource() instanceof WaiterRowButton){
			final WaiterRowButton wrb = (WaiterRowButton) e.getSource();
			WaiterAgent waiter = wrb.getWaiter();
			Host host = waiter.getHost();
			if (host.getNumWorkingWaiters() > 1){ //if the answer will be yes
				wrb.setBackground(Color.GRAY);
				wrb.setEnabled(false);
			}
			mTimer.schedule(new TimerTask() {
				public void run() {
					wrb.setBackground(Color.WHITE);
					wrb.setEnabled(true);
				}
			},
			WaiterAgent.cBREAK_LENGTH * 1000);
//			host.msgWantToGoOnBreak(waiter);
			waiter.msgWantBreak();
		}
    }
    
    public void pauseSimulation(){
    	System.out.println("PAUSING SIMULATION..."); //TODO: remove
    	Vector<Agent> agents = new Vector<Agent>();
    	
		HostAgent host = mRestPanel.getHost();
		agents.add(host);
		agents.add((CookAgent) mGUI.getRestaurantPanel().getCook());
		for (Customer customer : host.getWaitingCustomers()){
			CustomerAgent customeragent = (CustomerAgent) customer;
			agents.add(customeragent);
		}
    	for (Waiter waiter : host.getWaiters()){
    		WaiterAgent waiteragent = (WaiterAgent) waiter;
    		agents.add(waiteragent);
    		for (Order order : waiteragent.getOrders()){
    			agents.add((CustomerAgent) order.mCustomer);
    		}
    	}

    	if (!host.isPaused()){
    		for (Agent agent : agents){
    			agent.pause();
    		}
    	}else{
    		for (Agent agent : agents){
    			agent.restart();
    		}
    	}
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(Agent agent) {
    	
    	if (agent instanceof CustomerAgent){
    		CustomerAgent customer = (CustomerAgent) agent;
    		String name = mAddCustomerField.getText();
    		boolean hungry = mHungryCB.isSelected();
            if (hungry) customer.msgGotHungry();
            
            //Add Row
            CustomerRowButton crb = new CustomerRowButton(customer, name, hungry);
            mCustomerView.add(crb);
            
            
            if (hungry) customer.msgGotHungry();
    		mRestPanel.getCustomers().add(customer);
    	}
    	else if (agent instanceof WaiterAgent){
    		WaiterAgent waiter = (WaiterAgent) agent;
    		String name = waiter.getName();
    		Host host = waiter.getHost();
            host.msgAddWaiter((Waiter)waiter);
            
            WaiterRowButton wrb = new WaiterRowButton(waiter, name);
            mWaiterView.add(wrb);
    	}
    	else{
    		//TODO: 5
    	}
    	validate();
    }
    
    
    
    
    //-------------------------------------------------OTHER CLASSES-------------------------------------------------
    
    public class CustomerRowButton extends JButton{
    	private CustomerAgent mCustomer;
    	private JLabel mPersonLabel;
		private JCheckBox mPersonCB;
		
		public CustomerRowButton(CustomerAgent customer, String label, boolean hungry) {
			setLayout(new GridLayout(1, 2));
			setBackground(Color.WHITE);
			
			Dimension paneSize = mCustomerPane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20, (int) (paneSize.height / 4));
	            setPreferredSize(buttonSize);
	            setMinimumSize(buttonSize);
	            setMaximumSize(buttonSize);
			addActionListener(mGUI.getRestaurantPanel().getAgentPanel());

			
			mCustomer = customer;
			mPersonLabel = new JLabel(label);
				mPersonLabel.setBackground(Color.white);
			mPersonCB = new JCheckBox();
				mPersonCB.setSelected(hungry);
			
	        
	        add(mPersonLabel);
//	        add(mPersonCB);
		}
		
		public CustomerAgent getCustomer(){
			return mCustomer;
		}
		
		//TODO: Add checkbox methods
    }
    
    public class WaiterRowButton extends JButton{
    	private WaiterAgent mWaiter;
    	private JLabel mPersonLabel;
		private JCheckBox mPersonCB;
		
		public WaiterRowButton(WaiterAgent waiter, String label) {
			setLayout(new GridLayout(1, 2));
			setBackground(Color.WHITE);
			
			Dimension paneSize = mWaiterPane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20, (int) (paneSize.height / 4));
	            setPreferredSize(buttonSize);
	            setMinimumSize(buttonSize);
	            setMaximumSize(buttonSize);
			addActionListener(mGUI.getRestaurantPanel().getAgentPanel());

			mWaiter = waiter;
			mPersonLabel = new JLabel(label);
				mPersonLabel.setBackground(Color.white);
			mPersonCB = new JCheckBox();
				mPersonCB.setSelected(false);
			
	        
	        add(mPersonLabel);
//	        add(mPersonCB);
		}
		
		public WaiterAgent getWaiter(){
			return mWaiter;
		}
    }
    
    public String toString() {
		return "[AgentPanel]";
	}
    
    public void print(String string){
    	System.out.println(this + " " + string);
    }
    
    public RestaurantGui getGui(){
    	return mGUI;
    }
    
    public boolean getHackFlake(){
    	return mHackFlakeCB.isSelected();
    }
    
    public boolean getHackBusyStay(){
    	return mHackBusyCB.isSelected();
    }
    
    public int getHackCustomerCash(){
    	try{
    		return Integer.parseInt(mHackCustomerCashText.getText());
    	}catch (Exception e){
    		print("ERROR: Cash number not valid");
    		return -1;
    	}
    }
    
}

