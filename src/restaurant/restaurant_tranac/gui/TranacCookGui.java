package restaurant.restaurant_tranac.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import restaurant.restaurant_tranac.roles.TranacCookRole;
import city.gui.SimCityGui;

public class TranacCookGui implements Gui {

    private TranacCookRole agent;

    private int xPos = 414, yPos = 60;					//default cook position
    private int xDestination = 414, yDestination = 60;	//default cook position
    
    private int xHome = 414, yHome = 60;				//cook home position

    private Map<Integer, Coordinates> grillLocations = new HashMap<Integer, Coordinates>();
    private Map<Integer, Coordinates> grillingLocations = new HashMap<Integer, Coordinates>();
    private Map<Integer, Coordinates> plateLocations = new HashMap<Integer, Coordinates>();
    private Map<Integer, Coordinates> platingLocations = new HashMap<Integer, Coordinates>();
    private java.util.List<Food> foodItems = Collections.synchronizedList(new ArrayList<Food>());

    private enum FoodState {Cooking, Plating};
    
    private enum Command {noCommand, goToGrill, goToPlate};
    private Command command = Command.noCommand;

    private BufferedImage image;
    
    public TranacCookGui(TranacCookRole agent) {
        this.agent = agent;
        
        //plating locations for food
        plateLocations.put(1, new Coordinates(322,50));
        plateLocations.put(2, new Coordinates(322,110));
        plateLocations.put(3, new Coordinates(383,145));
        plateLocations.put(4, new Coordinates(382,145));
        
        //plating locations for cook
        platingLocations.put(1, new Coordinates(356,30));
        platingLocations.put(2, new Coordinates(356,80));
        platingLocations.put(3, new Coordinates(382,85));
        platingLocations.put(4, new Coordinates(442,85));
        
        //grill locations for food
        grillLocations.put(1, new Coordinates(374,30));
        grillLocations.put(2, new Coordinates(422,30));
        grillLocations.put(3, new Coordinates(464,30));
        
        //grilling locations for cook
        grillingLocations.put(1, new Coordinates(374,40));
        grillingLocations.put(2, new Coordinates(424,40));
        grillingLocations.put(3, new Coordinates(464,40));
        

    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/green-requiem.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
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
        
        if(xPos == xDestination && yPos == yDestination) {
        	if(command != Command.noCommand) {
        		agent.msgAnimationDone();
        		command = Command.noCommand;
        	}
        	
        }
	}
	
    public void draw(Graphics2D g) {
    	if(SimCityGui.GRADINGVIEW) {
    		g.setColor(Color.WHITE);
    		g.drawString("Cook",xPos,yPos-10);
    	}
    	else {
    		g.setColor(Color.BLACK);
    		g.drawImage(image, xPos, yPos, null);
    	}
    	
    	synchronized(foodItems) {
    	for(Food f : foodItems) {
    		g.drawString(f.getFood(), f.getX(), f.getY());
    	}
    	}
    }

    public boolean isPresent() {
        return true;
    } 
    
    /** Messages. These set the destination coordinates. */
   
    public void DoGoToHome() {
    	command = Command.noCommand;
    	
    	xDestination = xHome;
    	yDestination = yHome;
    }
    
    public void DoAddFoodItem(String f, int n) {
    	String s = getString(f);
    	
    	Coordinates grillPos = grillLocations.get(n);
    	synchronized(foodItems) {
    		foodItems.add(new Food(s, new Coordinates(grillPos)));
    	}
    }
    
    public void DoGoToGrill(int n) {
    	command = Command.goToGrill;
    	
    	Coordinates grillPos = grillingLocations.get(n);
    	xDestination = grillPos.getX();
    	yDestination = grillPos.getY();
    }
    
    public void DoGoToPlate(int n, String c) {
    	command = Command.goToPlate;
    	
    	Coordinates platePos = plateLocations.get(n);
    	Coordinates cookPos = platingLocations.get(n);
    	xDestination = cookPos.getX();
    	yDestination = cookPos.getY();
    	
    	String s = getString(c);
    	synchronized(foodItems) {
    	for(Food f : foodItems) {
    		if(f.f.equals(s) && f.s == FoodState.Cooking) {
    			f.s = FoodState.Plating;
    			f.n = n;
    			f.setX(platePos.getX());
    			f.setY(platePos.getY());
    			break;
    		}
    	}
    	}
    }
    
    public void FoodPickedUp(int n, String c) {

    	String s = getString(c);
    	synchronized(foodItems) {
    	for(Food f : foodItems) {
    		if(f.f.equals(s) && f.s == FoodState.Plating) {
    			foodItems.remove(f);
    			break;
    		}
    	}
    	}
    }
    
    /** Utilities */

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public String getString(String f) {					//sets the correct food icon for delivery
    	if(f.equalsIgnoreCase("Steak"))
    		return "STK";
    	else if(f.equalsIgnoreCase("Chicken"))
    		return "CHK";
    	else if(f.equalsIgnoreCase("Salad"))
    		return "SLD";
    	else
    		return "PIZ";
    }
    
    /** Class */
    class Food {
    	String f;
    	Coordinates c;
    	FoodState s;
    	int n;
    	
    	Food(String f, Coordinates c) {
    		this.f = f;
    		this.c = c;
    		n = 0;
    		s = FoodState.Cooking;
    	}
    	
    	String getFood() {
    		return f;
    	}
    	
    	int getX() {
    		return c.getX();
    	}
    	
    	int getY() {
    		return c.getY();
    	}
    	
    	void setX(int x) {
    		c.setX(x);
    	}
    	
    	void setY(int y) {
    		c.setY(y);
    	}
    }
}
