package restaurant_xurex.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import restaurant_xurex.gui.RestaurantGui;
import restaurant_xurex.interfaces.Cook;

public class CookGui implements Gui {

    private Cook agent = null;
    RestaurantGui gui;
    
    private boolean msgSent = true;
    private static final int cookDim = 10;
    
    private final int homex = 45;
    private final int homey = 280;
    private final int refX = 45;
    private final int refY = 330;

    private int xPos = homex, yPos = homey;					//default cook  position
    private int xDestination = homex, yDestination = homey;	//default start position
    
    public Map<Integer, Point> places = new HashMap<Integer, Point>();

    public CookGui(Cook agent) {
        this.agent = agent;
        places.put(new Integer(1), new Point(25,150)); //cook1
		places.put(new Integer(2), new Point(25,175)); //cook2
		places.put(new Integer(3), new Point(25,200)); //cook3
		places.put(new Integer(4), new Point(25,225)); //cook4
		places.put(new Integer(5), new Point(25,250)); //cook5
		places.put(new Integer(6), new Point(65,150)); //serve1
		places.put(new Integer(7), new Point(65,175)); //serve2
		places.put(new Integer(8), new Point(65,200)); //serve3
		places.put(new Integer(9), new Point(65,225)); //serve4
		places.put(new Integer(10), new Point(65,250));//serve5
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
        
        if (xPos == xDestination && yPos == yDestination && !msgSent) {
           agent.msgAtLocation(); msgSent=true;
        }
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(xPos, yPos, cookDim, cookDim);
    }

    public void DoDisplayOrder(String choice, int table){
    	gui.animationPanel.addFood(choice.substring(0,2), places.get(table).getX()-20, places.get(table).getY()+17);
    }
    
    public void DoRemoveOrder(int table){
    	gui.animationPanel.removeFood(places.get(table).getX()-20, places.get(table).getY()+17);
    }
    
    public void DoDisplayServe(String choice, int table){
    	gui.animationPanel.addFood(choice.substring(0,2), places.get(table).getX()+17, places.get(table).getY()+17);
    }
    
    public void DoRemoveServe(int table){
    	gui.animationPanel.removeFood(places.get(table).getX()+17, places.get(table).getY()+17);
    }
    
    public boolean isPresent() {
        return true;
    }
    
    public void DoGoToTable(int table) {
    	xDestination=places.get(table).getX();
    	yDestination=places.get(table).getY();

    	msgSent = false;
    }

    public void DoGoHome(){
    	xDestination = homex;
    	yDestination = homey;
    	msgSent = false;
    }
    
    public void DoGoRef(){
    	xDestination = refX;
    	yDestination = refY;
    	msgSent = false;
    }
    
    public boolean atRef(){
    	if(xPos==refX && yPos==refY)
    		return true;
    	return false;
    }
    
    public boolean atHome(){
    	if(xPos==homex && yPos==homey)
    		return true;
    	return false;
    }
    
    public boolean atTable(int table){
    	if(xPos==places.get(table).getX()+15 && yPos==places.get(table).getY()-15)
    		return true;
    	return false;
    }
    
    public void setGui(RestaurantGui gui){
    	this.gui=gui;
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
