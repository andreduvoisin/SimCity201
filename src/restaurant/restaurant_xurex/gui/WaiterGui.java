package restaurant.restaurant_xurex.gui;

//import java.awt.Color;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import restaurant.restaurant_xurex.interfaces.Waiter;
import restaurant.restaurant_xurex.interfaces.WaiterGui_;

public class WaiterGui implements Gui, WaiterGui_ {
	
	private boolean onFire = false;
	private BufferedImage fireImage;

    private Waiter role = null;
    RexAnimationPanel animationPanel;
    
    //Animation Image
    @SuppressWarnings("unused")
	private BufferedImage image;
    
    private static int sNum = 0;
    private int mNum;
    
    private boolean msgSent = true;
    private static final int waiterDim = 10;
    
    public Map<Integer, Point> places = new HashMap<Integer, Point>();
    
    private final int home  =  20;
    private int xBase = 0;
    private final int yBase = 210;

    private int xPos = home, yPos = home;//default waiter position
    private int xDestination = home, yDestination = home;//default start position

    public WaiterGui(Waiter agent, RexAnimationPanel animationPanel) {
        this.role = agent;
        this.animationPanel = animationPanel;
        image = null;
//    	try {
//    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/person.png");
//    	image = ImageIO.read(imageURL);
//    	}
//    	catch (IOException e) {
//    		System.out.println(e.getMessage());
//    	}
    	fireImage = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/fire.png");
    		fireImage = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
        //TABLES
        places.put(new Integer(1), new Point(200,150));
		places.put(new Integer(2), new Point(300,150));
		places.put(new Integer(3), new Point(200,250));
		places.put(new Integer(4), new Point(300,250));
		//COOK
		places.put(new Integer(5), new Point(100,300));
		//SERVICE LINE
		places.put(new Integer(6), new Point(100,150)); 
		places.put(new Integer(7), new Point(100,175)); 
		places.put(new Integer(8), new Point(100,200)); 
		places.put(new Integer(9), new Point(100,225)); 
		places.put(new Integer(10), new Point(100,250));
		//CASHIER
		places.put(new Integer(11), new Point(25,50)); 
		
		mNum = sNum++;
		//Creates unique position for WaiterGui
		xBase = 200 + mNum*25;
		DoGoBase();
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
           role.msgAtLocation(); msgSent=true;
        }
    }

    public void draw(Graphics2D g) {
//    	g.drawImage(image, xPos, yPos, null);
    	if(onFire)
			g.drawImage(fireImage, xPos, yPos, null);
		else{
			g.setColor(Color.MAGENTA);
        	g.fillRect(xPos, yPos, waiterDim, waiterDim);
		}
    }
    
    public void DoServeFood(String choice){
    	animationPanel.addFood(choice.substring(0,2), xDestination-2, yDestination+35);
    }
    
    public void DoCleanFood(){
    	animationPanel.removeFood(xDestination-2,  yDestination+35);
    }

    public void DoDisplayOrder(String choice, int table){
    	animationPanel.addFood(choice.substring(0,2)+"?", places.get(table).getX(), places.get(table).getY());
    }
    
    public void DoRemoveOrder(int table){
    	animationPanel.removeFood(places.get(table).getX(), places.get(table).getY());
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
    	role.wantBreak();
    }
    
    public void backToWork(){
    	role.backToWork();
    }
    
    public void setWaiterEnabled(){
    	//gui.setWaiterEnabled(agent);
    }
    public void setRole(Waiter role){
    	this.role = role;
    }
    
    public void setWaiterEnabled(String name){
    	//gui.setWaiterEnabled(agent, name);
    }

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
