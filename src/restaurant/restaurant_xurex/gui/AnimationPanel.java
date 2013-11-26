package restaurant.restaurant_xurex.gui;

import javax.swing.*;

import restaurant.restaurant_xurex.RexCashierRole;
import restaurant.restaurant_xurex.RexCookRole;
import restaurant.restaurant_xurex.RexHostRole;
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

public class AnimationPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
//	ROLES
    private Vector<Waiter> waiters = new Vector<Waiter>();
    private Vector<Customer> customers = new Vector<Customer>();
    //Initial
    private Host host = new RexHostRole();
    private Cook cook = new RexCookRole(); 
    private Cashier cashier = new RexCashierRole();
    
    private CookGui cookGui = new CookGui(cook);

//	DIMENSIONS
	static final int TABLEDIM = 25;
	static final int TABLEX = 200;
	static final int TABLEY = 250;
    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    
    static final int CASHIERX = 0;
    static final int CASHIERY = 50;
    
    Graphics2D g2 = null; 
    private boolean p = false;
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
    
    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        cook.setGui(cookGui);
        //bufferSize = this.getSize();
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
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

        if(!p){
        	for(Gui gui : guis) {
            	if (gui.isPresent()) {
                	gui.updatePosition();
            	}
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

    public void pause(){
    	p=true;
    }
    
    public void restart(){
    	p=false;
    }
    
    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
}
