package restaurant.restaurant_xurex.gui;

import javax.swing.*;

import base.BaseRole;
import city.gui.CityCard;
import city.gui.SimCityGui;
import restaurant.restaurant_xurex.RexCashierRole;
import restaurant.restaurant_xurex.RexCookRole;
import restaurant.restaurant_xurex.RexCustomerRole;
import restaurant.restaurant_xurex.RexHostRole;
import restaurant.restaurant_xurex.RexWaiterRole1;
import restaurant.restaurant_xurex.RexWaiterRole2;
import restaurant.restaurant_xurex.interfaces.Cashier;
import restaurant.restaurant_xurex.interfaces.Cook;
import restaurant.restaurant_xurex.interfaces.Customer;
import restaurant.restaurant_xurex.interfaces.Host;
import restaurant.restaurant_xurex.interfaces.Waiter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

public class RexAnimationPanel extends CityCard implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	static RexAnimationPanel instance;
	
	public static RexAnimationPanel getInstance(){
		return instance;
	}
	
//	ROLES
    private static Vector<Waiter> waiters = new Vector<Waiter>();
    private static Vector<Customer> customers = new Vector<Customer>();
    //Initial
    private static Host host = new RexHostRole();
    private static Cook cook = new RexCookRole(); 
    private static Cashier cashier = new RexCashierRole();
    
    private CookGui cookGui = new CookGui(cook);

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
    private List<Gui> guis = new ArrayList<Gui>();
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
        
        cook.setGui(cookGui);
        guis.add(cookGui);
        //bufferSize = this.getSize();
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paint(Graphics g) {
        g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setBackground(Color.WHITE);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.setColor(Color.ORANGE);
        
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
        
		g2.setColor(Color.BLACK);
		drawFood();
    }
    
    public void updateCustomerLine() {
        CustomerGui.LINE_POSITION--;
        for(Gui gui : guis) {
        	if (gui.isPresent()) {
        			if(gui instanceof CustomerGui) {
        				((CustomerGui) gui).moveForwardInLine();
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
    	foodIcons.add(new Icon(choice, x, y));
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
        guis.add(gui);
    }

    public void removeGui(Gui gui) {
    	guis.remove(gui);
    }
    
    public static void addPerson(BaseRole role) {
    	if (role instanceof RexCustomerRole){
    		RexCustomerRole customer = (RexCustomerRole) role;
    		customers.add(customer);
    		customer.gotHungry();
    	}
    	else if (role instanceof RexWaiterRole1){
    		RexWaiterRole1 waiter = (RexWaiterRole1) role;
    		
    		waiters.add(waiter);
            host.addWaiter((RexWaiterRole1)waiter);
    	}
    	else if (role instanceof RexWaiterRole2){
    		RexWaiterRole2 waiter = (RexWaiterRole2) role;
    		
    		waiters.add(waiter);
    		host.addWaiter((RexWaiterRole2)waiter);
    	}
    	else if (role instanceof RexHostRole){
    		host = (RexHostRole) role;
    	}
    	else if (role instanceof RexCookRole){
    		cook = (RexCookRole) role;
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
