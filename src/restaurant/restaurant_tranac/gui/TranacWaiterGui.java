package restaurant.restaurant_tranac.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.roles.TranacWaiterBase;
import city.gui.SimCityGui;

public class TranacWaiterGui implements Gui {

    private TranacWaiterBase agent = null;
    private int mNum;
    
    private int xPos = -20, yPos = -20;						//default waiter position
    private int xDestination = -20, yDestination = -20;		//default start position
    
    private static final int xHost = 20, yHost = 40;
    private final int xHome, yHome;
    private static final int xBase = 100, yBase = 5;
    private static final int xWaitingArea = 40, yWaitingArea = 60;
    private static final int xPlaceOrder = 455, yPlaceOrder = 125;
    private static final int xCashier = 230, yCashier = 180;
    private static final int xStand = 455, yStand = 180;
    private Map<Integer, Coordinates> tableLocations = new HashMap<Integer, Coordinates>();
    private Map<Integer, Coordinates> plateLocations = new HashMap<Integer, Coordinates>();
    
    private boolean onFire = false;
	private BufferedImage fireImage;

    private String food;
    private BufferedImage image;
    private BufferedImage check;
    private BufferedImage askingBubble;
    
    private enum Command {noCommand, goToHome, goToHost, goToWaitingArea, goToTable, goToPlaceOrder, goToGetOrder, goToCashier, goToStand};
    private Command command = Command.goToHome;
    
    private enum State {noState, asking, deliveringFood, deliveringCheck};
    private State state = State.noState;
    
    @SuppressWarnings("unused")
	private TranacAnimationPanel gui;
    
    public TranacWaiterGui(TranacWaiterBase agent, TranacAnimationPanel gui, int i) {
        this.agent = agent;
        this.gui = gui;
        mNum = i;
        
        //home coordinates based off of what num waiter is
        xHome = xBase + 30*(i % 10);
        yHome = yBase + 40*(int)(i/10);
        
        //table coordinates are matched to the animationPanel table coordinates
        tableLocations.put(1, new Coordinates(100,325));
        tableLocations.put(2, new Coordinates(220,320));
        tableLocations.put(3, new Coordinates(400,320));
        tableLocations.put(4, new Coordinates(528,320));
        
        //plate coordinates
        plateLocations.put(1, new Coordinates(410,20));
        plateLocations.put(2, new Coordinates(410,80));
        plateLocations.put(3, new Coordinates(508,165));
        plateLocations.put(4, new Coordinates(568,165));
        
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/blue-rhapsody.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	check = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant_tranac/gui/images/check.png");
    	check = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	
    	askingBubble = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant_tranac/gui/images/speech-question.png");
    	askingBubble = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	fireImage = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/fire.png");
    		fireImage = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }

    public TranacWaiterGui(TranacWaiterBase agent, int i) {
        this.agent = agent;
        this.gui = TranacAnimationPanel.getInstance();
        
        //home coordinates based off of what num waiter is
        xHome = xBase + 30*(i % 10);
        yHome = yBase + 40*(int)(i/10);
        
        //table coordinates are matched to the animationPanel table coordinates
        tableLocations.put(1, new Coordinates(100,325));
        tableLocations.put(2, new Coordinates(220,320));
        tableLocations.put(3, new Coordinates(400,320));
        tableLocations.put(4, new Coordinates(528,320));
        
        //plate coordinates
        plateLocations.put(1, new Coordinates(410,20));
        plateLocations.put(2, new Coordinates(410,80));
        plateLocations.put(3, new Coordinates(508,165));
        plateLocations.put(4, new Coordinates(568,165));
        
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/blue-rhapsody.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	check = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/check.png");
    	check = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	
    	askingBubble = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/speech-question.png");
    	askingBubble = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        //sends the correct message to the waiter about the final destination
        if (xPos == xDestination && yPos == yDestination) {
        	switch(command) {
        		case goToPlaceOrder: {
        			agent.msgAnimationAtCook();
        			command = Command.noCommand;
        			break;
        		}
        		case goToGetOrder: {
        			agent.msgAnimationAtOrderPickup();
        			command = Command.noCommand;
        			break;
        		}
        		case goToWaitingArea: {
        			agent.msgAnimationAtWaitingArea();
        			command = Command.noCommand;
        			break;
        		}
        		case goToTable: {
        			agent.msgAnimationAtTable();
        			command = Command.noCommand;
        			break;
        		}
        		case goToHost: {
        			agent.msgAnimationAtHost();
        			command = Command.noCommand;
        			break;
        		}
        		case goToCashier: {
        			agent.msgAnimationAtCashier();
        			command = Command.noCommand;
        		}
        		case goToStand: {
        			agent.msgAnimationAtCook();
        			command = Command.noCommand;
        		}
        		default:
        			command = Command.noCommand;
        			break;
        	}
//        	if(command == Command.goToHome) {
//        		command = Command.noCommand;
//        	}
//        	else if(command != Command.noCommand){
//        		agent.msgAnimationDone();
//        		command = Command.noCommand;
//        	}
        }     
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        if(onFire)
			g.drawImage(fireImage, xPos, yPos, null);
		else if(SimCityGui.GRADINGVIEW) {
    		g.drawString("W"+mNum, xPos+10, yPos);
    	}
        else {
    	g.drawImage(image, xPos, yPos, null);
        }
        
    	switch(state) {
    		case deliveringFood: {
            	g.drawString(food, xPos-2, yPos-5);
            	break;
    		}
    		case deliveringCheck: {
    			if(!SimCityGui.GRADINGVIEW) {
    				g.drawImage(check, xPos-14, yPos+10, null);
    			}
    			break;
    		}
    		case asking: {
    			if(!SimCityGui.GRADINGVIEW) {
    				g.drawImage(askingBubble, xPos-14, yPos+10, null);
    			}
    			break;
    		}
    		default:
    			break;
    	}
    }
    
    public void setFood(String f) {					//sets the correct food icon for delivery
    	if(f.equals("Steak"))
    		food = "STK";
    	else if(f.equals("Chicken"))
    		food = "CHK";
    	else if(f.equals("Salad"))
    		food = "SLD";
    	else
    		food = "PIZ";
    }
    /*
    public void setBreak() {
    	//set off if enabled 	
    	gui.setEnabled(agent);
    }
    
    public void setWantToGoOnBreak() {
    	agent.msgWantToGoOnBreak();
    }
 */   
    public boolean isPresent() {
        return true;
    }
    
    public void setAsking() {
    	state = State.asking;
    }
    
    public void setDeliveringCheck() {
    	state = State.deliveringCheck;
    }
    
    public void setDeliveringFood() {
    	state = State.deliveringFood;
    }
    
    public void setClear() {
    	state = State.noState;
    }
    
    /** Messages. These set the destination coordinates. */
    
    public void DoGoToHome() {
    	command = Command.goToHome;
    	
    	xDestination = xHome;
    	yDestination = yHome;
    }

    public void DoGoToHost() {
    	command = Command.goToHost;
    	
    	xDestination = xHost;
    	yDestination = yHost;
    }
   
    public void DoGoToWaitingArea(int n) {
    	command = Command.goToWaitingArea;
    	
    	xDestination = xWaitingArea;
    	yDestination = yWaitingArea + 35*n;
    }
      
    public void DoBringToTable(TranacCustomer c, int table) {
    	DoGoToTable(table);
    	
    	Coordinates tablePos = tableLocations.get(table);
    	c.getGui().DoGoToSeat(tablePos.getX(), tablePos.getY());
    }

    public void DoGoToTable(int table) {
    	command = Command.goToTable;
    	
        Coordinates tablePos = tableLocations.get(table);
        xDestination = tablePos.getX() + 20;
        yDestination = tablePos.getY() - 20;
    }
 
    public void DoGoToCook() {
    	command = Command.goToPlaceOrder;
    	
    	xDestination = xPlaceOrder;
    	yDestination = yPlaceOrder;
    }
    
    public void DoGoToGetOrder(int n) {
    	command = Command.goToGetOrder;
    	
    	Coordinates pickupPos = plateLocations.get(n);
    	xDestination = pickupPos.getX();
    	yDestination = pickupPos.getY();
    }

    public void DoGoToCashier() {
    	command = Command.goToCashier;
    	
    	xDestination = xCashier;
    	yDestination = yCashier;
    }
    
    public void DoGoToRevolvingStand() {
    	command = Command.goToStand;
    	
    	xDestination = xStand;
    	yDestination = yStand;
    }

    /** Utilities */
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setFired(boolean state){
    	onFire = state;
    }
}
