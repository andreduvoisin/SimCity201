package restaurant.restaurant_xurex.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import restaurant.restaurant_xurex.RexCashierRole;
import restaurant.restaurant_xurex.RexCookRole;
import restaurant.restaurant_xurex.RexCustomerRole;
import restaurant.restaurant_xurex.RexHostRole;
import restaurant.restaurant_xurex.RexWaiterRole1;
import restaurant.restaurant_xurex.RexWaiterRole2;
import restaurant.restaurant_xurex.interfaces.Customer;
import restaurant.restaurant_xurex.interfaces.Waiter;
import base.BaseRole;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

public class RexAnimationPanel extends CityCard implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	//ANIMATION
	private BufferedImage background;
	
	private static RexAnimationPanel instance;
	public static RexAnimationPanel getInstance(){
		return instance;
	}
	
//	ROLES
    private static Vector<Waiter> waiters = new Vector<Waiter>();
    private static Vector<Customer> customers = new Vector<Customer>();
    //Initial
    public static RexHostRole host; // = new RexHostRole();
    public static RexCookRole cook;
    public static RexCashierRole cashier; // = new RexCashierRole();
    
    private static CookGui cookGui;

//	DIMENSIONS
	static final int TABLEDIM = 25;
	static final int TABLEX = 200;
	static final int TABLEY = 250;
    private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    
    static final int CASHIERX = 0;
    static final int CASHIERY = 50;
    
    Graphics2D g2 = null; 
    //private Image bufferImage;
    //private Dimension bufferSize;
    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    private List<Icon> foodIcons = Collections.synchronizedList(new ArrayList<Icon>());
    
    private class Icon{
    	private int x;
    	private int y;
    	String choice;
    	Icon(String choice, int x, int y){
    		this.x=x; this.y=y; this.choice=choice;
    	}
    };
    
    public RexAnimationPanel(SimCityGui city) {
    	super(city);
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        instance = this;
        
        cookGui = new CookGui(cook);
        cookGui.setAnimationPanel(this);
        guis.add(cookGui);
        //bufferSize = this.getSize();
 
    	Timer timer = new Timer(Time.cSYSCLK/20, this );
    	timer.start();
    	
    	background = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_maggiyan/images/mybg.png");
    	background = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		synchronized(guis) {
			for(Gui gui : guis) {
	        	if (gui.isPresent()) {
	            	gui.updatePosition();
	        	}
	    	}
		}
	}

    public void paint(Graphics g) {
        g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setBackground(Color.WHITE);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.setColor(Color.ORANGE);
        
        if(!SimCityGui.GRADINGVIEW) {
        	if(background != null)
        		g2.drawImage(background,0,0,null);
        	
        }
        //TABLES
        g2.fillRect(TABLEX, TABLEY, TABLEDIM, TABLEDIM);		//table 1 : 200,250 : SW
        g2.fillRect(TABLEX+100, TABLEY, TABLEDIM, TABLEDIM); 	//table 2 : 300,250 : SE
        g2.fillRect(TABLEX+100, TABLEY-100, TABLEDIM, TABLEDIM);//table 3 : 300,150 : NE
        g2.fillRect(TABLEX, TABLEY-100, TABLEDIM, TABLEDIM); 	//table 4 : 200,150 : NW
        
        //CASHIER LOCATION
        g2.fillRect(CASHIERX, CASHIERY, TABLEDIM, TABLEDIM);
        
        //KITCHEN
        g2.fillRect(0 , 150, TABLEDIM, TABLEDIM*5);
        g2.fillRect(75, 150, TABLEDIM, TABLEDIM*5);
        
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(0,  150, TABLEDIM, TABLEDIM);
        g2.drawRect(0,  175, TABLEDIM, TABLEDIM);
        g2.drawRect(0,  200, TABLEDIM, TABLEDIM);
        g2.drawRect(0,  225, TABLEDIM, TABLEDIM);
        g2.drawRect(0,  250, TABLEDIM, TABLEDIM);
        g2.drawRect(75,  150, TABLEDIM, TABLEDIM);
        g2.drawRect(75,  175, TABLEDIM, TABLEDIM);
        g2.drawRect(75,  200, TABLEDIM, TABLEDIM);
        g2.drawRect(75,  225, TABLEDIM, TABLEDIM);
        g2.drawRect(75,  250, TABLEDIM, TABLEDIM);
        
        synchronized(guis) {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	               	gui.draw(g2);
	            }
	       	}
        }
        
		g2.setColor(Color.BLACK);
		drawFood();
    }
    
    public void updateCustomerLine() {
        CustomerGui.LINE_POSITION--;
        synchronized(guis) {
	        for(Gui gui : guis) {
	        	if (gui.isPresent()) {
	        			if(gui instanceof CustomerGui) {
	        				((CustomerGui) gui).moveForwardInLine();
	        			}
	        	}
	        }
        }
    }

    private void drawFood(){
    	synchronized(foodIcons){
    	if(!foodIcons.isEmpty()){
    		for(Icon food:foodIcons){
    			if(!food.choice.equals("removed"))
    				g2.drawString(food.choice, food.x, food.y);
    		}
    	}
    	}
    }
    
    public void addFood(String choice, int x, int y){
    	synchronized(foodIcons) {
    		foodIcons.add(new Icon(choice, x, y));
    	}
    }
    
    public void removeFood(int x, int y){
    	synchronized(foodIcons){
    		for(Icon temp: foodIcons){
    			if(temp.x==x&&temp.y==y){
    				//foodIcons.remove(temp);
    				temp.choice = "removed";
    			}
    		}
    	}
    }

    public void addGui(Gui gui) {
    	synchronized(guis) {
    		guis.add(gui);
    	}
    }

    public void removeGui(Gui gui) {
    	synchronized(guis) {
    		guis.remove(gui);
    	}
    }
    
    public void removeCustomer(Customer customer){
    	customers.remove(customer);
    }
    
    public static void addPerson(BaseRole role) {
    	if (role instanceof RexCustomerRole){
    		RexCustomerRole customer = (RexCustomerRole) role;
    		customer.setHost(host);
    		customer.setCashier(cashier);
    		customers.add(customer);
    		//customer.gotHungry();
    		((RexCustomerRole) role).getGui().setHungry();
    	}
    	else if (role instanceof RexWaiterRole1){
    		((RexWaiterRole1)role).setHost(host);
    		((RexWaiterRole1)role).setCook(cook);
    		((RexWaiterRole1)role).setCashier(cashier);
    		RexWaiterRole1 waiter = (RexWaiterRole1) role;
    	
    		waiters.add(waiter);
            host.addWaiter((RexWaiterRole1)waiter);
    	}
    	else if (role instanceof RexWaiterRole2){
    		((RexWaiterRole2)role).setHost(host);
    		((RexWaiterRole2)role).setCook(cook);
    		((RexWaiterRole2)role).setCashier(cashier);
    		RexWaiterRole2 waiter = (RexWaiterRole2) role;
    		
    		waiters.add(waiter);
    		host.addWaiter((RexWaiterRole2)waiter);
    	}
    	else if (role instanceof RexHostRole){
    		host = (RexHostRole) role;
    	}
    	else if (role instanceof RexCookRole){
    		cook = (RexCookRole) role;
            cook.setGui(cookGui);
            cookGui.setAgent(cook);
    	}
    	else if (role instanceof RexCashierRole){
    		cashier = (RexCashierRole) role;
    	}
    }
    
    public static RexHostRole getHost(){
    	return (RexHostRole) host;
    }
    public static RexCookRole getCook(){
    	return (RexCookRole) cook;
    }
    public static RexCashierRole getCashier(){
    	return (RexCashierRole) cashier;
    }
}
