package restaurant_xurex.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import restaurant_xurex.gui.RestaurantGui;
import restaurant_xurex.interfaces.Waiter;

public class WaiterGui implements Gui {

    private Waiter agent = null;
    RestaurantGui gui;
    
    private boolean msgSent = true;
    private static final int waiterDim = 10;
    
    public Map<Integer, Point> places = new HashMap<Integer, Point>();
    
    private final int home  =  20;
    private int xBase = 0;
    private final int yBase = 210;

    private int xPos = home, yPos = home;//default waiter position
    private int xDestination = home, yDestination = home;//default start position

    public WaiterGui(Waiter agent, RestaurantGui gui) {
        this.agent = agent;
        this.gui = gui;
        places.put(new Integer(1), new Point(200,150));
		places.put(new Integer(2), new Point(300,150));
		places.put(new Integer(3), new Point(200,250));
		places.put(new Integer(4), new Point(300,250));
		places.put(new Integer(5), new Point(100,300)); //Cook 
		//SERVICE LINE//
		places.put(new Integer(6), new Point(100,150)); //serve1
		places.put(new Integer(7), new Point(100,175)); //serve2
		places.put(new Integer(8), new Point(100,200)); //serve3
		places.put(new Integer(9), new Point(100,225)); //serve4
		places.put(new Integer(10), new Point(100,250));//serve5
		places.put(new Integer(11), new Point(25,50));   ///cashier
		
		//Creates unique position for WaiterGui
		xBase = 200 + agent.getNumber()*30;
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
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, waiterDim, waiterDim);
    }
    
    public void DoServeFood(String choice){
    	gui.animationPanel.addFood(choice.substring(0,2), xDestination-2, yDestination+35);
    }
    
    public void DoCleanFood(){
    	gui.animationPanel.removeFood(xDestination-2,  yDestination+35);
    }

    public void DoDisplayOrder(String choice, int table){
    	gui.animationPanel.addFood(choice.substring(0,2)+"?", places.get(table).getX(), places.get(table).getY());
    }
    
    public void DoRemoveOrder(int table){
    	gui.animationPanel.removeFood(places.get(table).getX(), places.get(table).getY());
    }
    
    public boolean isPresent() {
        return true;
    }
    
    public void DoGoToTable(int table) {
    	xDestination=places.get(table).getX();
    	yDestination=places.get(table).getY();

    	//Puts the Waiter in the upper right
    	xDestination+=15;
    	yDestination-=15;
    	
    	msgSent = false;
    }

    public void DoGoHome(){
    	xDestination = home;
    	yDestination = home;
    	msgSent = false;
    }
    
    public void DoGoBase(){
    	xDestination = xBase;
    	yDestination = yBase;
    	msgSent = false;
    }
    
    public boolean atHome(){
    	if(xPos==home && yPos==home)
    		return true;
    	return false;
    }
    
    public boolean atBase(){
    	if(xPos==xBase && yPos==yBase)
    		return true;
    	return false;
    }
    
    public boolean atTable(int table){
    	if(xPos==places.get(table).getX()+15 && yPos==places.get(table).getY()-15)
    		return true;
    	return false;
    }
    
    public void setBreak(){
    	agent.wantBreak();
    }
    
    public void backToWork(){
    	agent.backToWork();
    }
    
    public void setWaiterEnabled(){
    	gui.setWaiterEnabled(agent);
    }
    
    public void setWaiterEnabled(String name){
    	gui.setWaiterEnabled(agent, name);
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
