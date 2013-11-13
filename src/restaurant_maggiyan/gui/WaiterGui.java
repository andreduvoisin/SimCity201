package restaurant_maggiyan.gui;


import restaurant_maggiyan.HostAgent;
import restaurant_maggiyan.WaiterAgent;
import restaurant_maggiyan.interfaces.Customer;

import java.awt.*;

import javax.swing.*;

public class WaiterGui implements Gui {
    
    private WaiterAgent agent = null; 
  	public String customerOrder = " "; 

    public int xHome;
    public static final int yHome = 75; 
  	
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = xHome, yDestination = yHome;//default start position
    private int prevXPos, prevYPos; 
    
    public static final int xTable = 200;
    public static final int yTable = 250;
    
    public final int xKitchen = 540;
    public final int yKitchen = 300; 
    
    public final int xBreak = -30;
    public final int yBreak = -40; 
    
    
    public static final int tableNum = 3; 
    public int currentTable = 1; 
    
    private int[] tableXCoord = new int[tableNum];
    private int[] tableYCoord = new int[tableNum];
    
    private boolean atStart = true; 
    private boolean offBreak = false;
    private boolean atHomeChange = true; 
    
    RestaurantGui gui;
    
    public void tablePositions(){
    	for(int i = 0; i<tableNum; i++){
    		tableXCoord[i] = xTable + (100*(i));
    		tableYCoord[i] = yTable;
    	}
    }
    
    public WaiterGui(WaiterAgent agent, RestaurantGui gui) {
        this.agent = agent;
        this.gui = gui;
        tablePositions();
    }
    
    public void updatePosition() { 
    	
    	if (xPos < xDestination){
    		prevXPos = xPos; 
    		xPos++;
    	}	//xPos = xPos + 2; 
        else if (xPos > xDestination){
        	prevXPos = xPos;
        	xPos--;
        }	//xPos = xPos - 2;
        if (yPos < yDestination){
        	prevYPos = yPos;
        	yPos++;
        }	//yPos = yPos + 2;
        else if (yPos > yDestination){
        	prevYPos = yPos; 
        	yPos--; 
        	//yPos = yPos - 2;
        }
        if (xPos == xDestination && yPos == yDestination){
        		if(xDestination == xHome && yDestination == yHome){
        			if(offBreak){
                		offBreak = false;
        				agent.msgBackFromBreak(); 
        				gui.setWaiterEnabled(agent);
        			}
        			if(xPos != prevXPos && yPos != prevYPos){
        				prevXPos = xPos;
        				prevYPos = yPos;
                		System.out.println("Release animation"); 
                		agent.msgAnimationReady(); 
                		
                	}
        		}
        		else if(xDestination == 50 && yDestination == 50){
        			if(xPos != prevXPos){
        				prevXPos = xPos;
        				prevYPos = yPos;
                		System.out.println("Release animation"); 
                		agent.msgAnimationReady(); 
                	}
        		}
        		else if((xDestination == tableXCoord[currentTable] + 20) & (yDestination == yTable - 20)) 
        		{
        			agent.msgAtTable();
        			//agent.msgReady();
        		}
        		else if((xDestination == xKitchen) & (yDestination == yKitchen)){	
        			agent.msgReachedKitchen(); 
        		}
        		else if((xDestination == xBreak) & (yDestination == yBreak)){
        			if(xPos != prevXPos){
        				prevXPos = xPos;
        				prevYPos = yPos;
                		System.out.println("Release animation"); 
                		agent.msgAnimationReady(); 
                	}
        		}
        }
        if (xPos == -20 && yPos == -20 && atStart == false)
        {
//        	if(xPos != prevXPos && yPos != prevYPos){
//        		System.out.println("Release animation"); 
//        		agent.msgAnimationReady(); 
//        	}
        	atStart = true;
        	agent.msgWaiterFree(); 
      
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        g.drawString(customerOrder, xPos, yPos-15);
        updatePosition();
        updatePosition();
        
    }

    public boolean isAtStart(){
    	return atStart; 
    }
    
    public boolean isPresent() {
        return true;
    }
    
    public void atWork(int pos){
    	if(atHomeChange){
    		atHomeChange = false; 
    		xHome = 55*pos; 
        	xDestination = xHome; 
    	}
    	agent.atWork();
    }
    
    public void setBreak(){
    	System.out.println("setBreak");
    	agent.msgAskToGoOnBreak(); 
    }
    
    public void DoneWithBreak(WaiterAgent waiter){
    	//gui.setWaiterEnabled(agent);
    }

    public void DoBringToTable(int tableNumber) {
    	atStart = false;
        xDestination = tableXCoord[tableNumber - 1] + 20;
        yDestination = yTable - 20;
        currentTable = tableNumber - 1; 
    }
    
    public void DoGoToCook(){
    	atStart = false;
    	xDestination = xKitchen;
    	yDestination = yKitchen; 
    }

    public void DoLeaveCustomer() {
    	atStart = false; 
        xDestination = xHome;
        yDestination = yHome;
    }
    
    public void DoGoOnBreak(){
    	atStart = false; 
    	xDestination = xBreak; 
    	yDestination = yBreak; 
    }
    
    public void DoGoOffBreak(){
    	offBreak = true; 
    	xDestination = xHome;
        yDestination = yHome;
    }
    
    public void DoEnableBreakBox(){
    	gui.setWaiterEnabled(agent);
    }
    
	public void showCustomerChoice(String choice){
		customerOrder = choice + " ?"; 
	}
	
	public void showCustomerOrder(String choice){
		customerOrder = choice; 
	}
	
	public void hideCustomerChoice ()
	{
		customerOrder = " "; 
	}

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void DoGoToSeatCustomer() {
		xDestination = 50;
        yDestination = 50;
		
	}
    
}
