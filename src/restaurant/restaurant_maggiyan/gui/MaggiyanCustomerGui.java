package restaurant.restaurant_maggiyan.gui;

import javax.swing.*;

import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanHostRole;

import java.awt.*;

public class MaggiyanCustomerGui implements MaggiyanGui{

	private MaggiyanCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean showChoiceLabel =  true; 
	private int posCounter = 1; 
	public JLabel myChoice = new JLabel(); 
	private String nameOfChoice = " "; 

	//private HostAgent host;
	MaggiyanRestaurantGui gui;

	private int xPos, yPos, prevXPos, prevYPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable = 200;
	public static final int yTable = 250;
	public static final int tableNum = 3; 
	    
	private int[] tableXCoord = new int[tableNum];
    private int[] tableYCoord = new int[tableNum];
    
    public int getXPos(){
    	return xPos; 
    }
    
    public int getYPos(){
    	return yPos; 
    }
    
    public void tablePositions(){
	    for(int i = 0; i<tableNum; i++){
	    		tableXCoord[i] = xTable + (100*(i));
	    		tableYCoord[i] = yTable; 
	    }
	}

	public MaggiyanCustomerGui(MaggiyanCustomerRole c, MaggiyanRestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		tablePositions();
		//maitreD = m;
		this.gui = gui;
	}
	
	public int[] getXCoord(){
		return tableXCoord; 
	}
	
	public int[] getYCoord(){
		return tableYCoord; 
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
		if (xPos == xDestination && yPos == yDestination) {
			if(xDestination == 50 && yDestination == 30){
    			if(xPos != prevXPos && yPos != prevYPos){
    				prevXPos = xPos;
    				prevYPos = yPos;
            		System.out.println("Release animation"); 
            		agent.msgAnimationReady(); 
            	}
    		}
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		g.drawString(nameOfChoice, xPos, yPos-15);
		updatePosition();
		updatePosition();

	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry(int pos) {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
		xDestination = 50*pos;
		yDestination = (int) (Math.random()*(20)); 
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void showChoice(String choice){
		nameOfChoice = choice + " ?"; 
	}
	
	public void showOrder(String choice){
		nameOfChoice = choice; 
	}
	
	public void hideChoice ()
	{
		nameOfChoice = " "; 
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		xDestination = tableXCoord[seatnumber-1];
		yDestination = yTable;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}

	public void DoGoToFrontOfLine() {
		xDestination = 50;
        yDestination = 30;
		
	}
}
