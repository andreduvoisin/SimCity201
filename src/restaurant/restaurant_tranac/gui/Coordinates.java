package restaurant.restaurant_tranac.gui;

public class Coordinates {		//coordinate class is used for table coordinates
    int xCoord, yCoord;
    
    Coordinates(int x, int y) {
    	xCoord = x;
    	yCoord = y;
    }
    
    Coordinates(Coordinates c) {
    	xCoord = c.getX();
    	yCoord = c.getY();
    }
    
    int getX() {
    	return xCoord;
    }
    	
    int getY() {
    	return yCoord;
    }
    
    void setX(int x) {
    	xCoord = x;
    }
    
    void setY(int y) {
    	yCoord = y;
    }
}
