package housing;

import interfaces.Person;

public class House {
    int xLocation, yLocation;
    double mRent;
    Person mOccupant; 
    
    public House(int x, int y, double rent) {
    	xLocation = x;
    	yLocation = y;
    	mRent = rent;
    	mOccupant = null;
    }
    
} 