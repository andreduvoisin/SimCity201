package restaurant.restaurant_maggiyan.gui;

import javax.swing.*;

import restaurant.restaurant_maggiyan.roles.MaggiyanCookRole;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CookGui implements Gui{

	private MaggiyanCookRole agent = null;
	private boolean isPresent = true;
	
	//Cook Positions
	private final int initialxPos = 625; 
	private final int initialyPos = 340; 
	private int cookingXPos = 680; 
	private int cookingYPos = 275; 
	private int platingXPos = 600; 
	private int platingYPos = 275; 
	
	//Grill Positions
	private int gPosition1 = 715;
	private int gYPos = 280; 
	
	//Plated food positions
	private int pXPos = 570;
	private int pYPos = 275; 
	
	//Cooking food 
	private List<String> CookingFoods = Arrays.asList(" ", " ", " "); 
	
	//Plated food
	private List<String> PlatedFoods = Arrays.asList(" ", " ", " "); 
	
	//private HostAgent host;
	RestaurantGui gui;
	
	private int xPos, yPos, prevXPos, prevYPos;
	private int xDestination, yDestination;

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

	public CookGui(MaggiyanCookRole cook){ //HostAgent m) {
		agent = cook; 
		xPos = initialxPos;
		yPos = initialyPos;
		xDestination = initialxPos;
		yDestination = initialyPos;

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
			if(xDestination == cookingXPos){
    			if(xPos != prevXPos && yPos != prevYPos){
    				prevXPos = xPos;
    				prevYPos = yPos;
            		agent.msgAnimationReady(); 
            	}
    		}

		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.WHITE);
		g.drawString(CookingFoods.get(0), gPosition1, gYPos); 
		g.drawString(CookingFoods.get(1), gPosition1, gYPos+75); 
		g.drawString(CookingFoods.get(2), gPosition1, gYPos+150); 
		g.setColor(Color.BLACK);
		g.drawString(PlatedFoods.get(0),  pXPos,  pYPos);
		g.drawString(PlatedFoods.get(1),  pXPos,  pYPos+75);
		g.drawString(PlatedFoods.get(2),  pXPos,  pYPos+150);
		updatePosition();
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void DoCookFood(String choice, int cPos){
		String choiceAbreviation = " "; 
		
		switch(choice){
			case "Steak": choiceAbreviation = "ST"; 
				break;
			case "Chicken": choiceAbreviation = "CH";
				break;
			case "Salad": choiceAbreviation = "SA";
				break;
			case "Pizza": choiceAbreviation = "PI"; 
				break;
		}
		
		CookingFoods.set(cPos, choiceAbreviation);  
		
	}
	
	public void DoRemoveFoodFromGrill(int grillPos){
		CookingFoods.set(grillPos,  " "); 
	}
	
	public void DoGoToGrill(int grillPos){
		xDestination = cookingXPos; 
		yDestination = cookingYPos + 75*grillPos; 
	}
	
	public void DoGoToPlatingArea(int pos){
		xDestination = platingXPos;
		yDestination = platingYPos + 75*pos; 
	}
	
	public void DoPlateFood(String choice, int pos){
		String choiceAbreviation = " "; 
		
		switch(choice){
			case "Steak": choiceAbreviation = "ST"; 
				break;
			case "Chicken": choiceAbreviation = "CH";
				break;
			case "Salad": choiceAbreviation = "SA";
				break;
			case "Pizza": choiceAbreviation = "PI"; 
				break;
		}
		
		PlatedFoods.set(pos, choiceAbreviation); 
	}
	
	public void DoRemoveFood(int pos){
		PlatedFoods.set(pos,  " "); 
	}
	
	public void GoToHomePosition(){
		xDestination = initialxPos;
		yDestination = initialyPos; 
	}
	
}
