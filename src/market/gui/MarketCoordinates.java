package market.gui;

public class MarketCoordinates {		//coordinate class is used for table coordinates
    int xCoord, yCoord;
    
    MarketCoordinates(int x, int y) {
    	xCoord = x;
    	yCoord = y;
    }
    
    MarketCoordinates(MarketCoordinates c) {
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
