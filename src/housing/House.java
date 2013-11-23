package housing;

import housing.interfaces.HousingRenter;

/*
 * House class
 * @author David Carr
 */

public class House {
	public int xLocation, yLocation;
	public double mRent;
	public HousingRenter mOccupant;
	public enum HousingType{Apartment, House}; 
	public HousingType type; 
	public final double maxAptRent = 450.00;  
	
	public House(int x, int y, double rent) {
		xLocation = x;
		yLocation = y;
		mRent = rent;
		mOccupant = null;
		
		if(rent <= maxAptRent){
			type = HousingType.Apartment; 
		}
		else{
			type = HousingType.House; 
		}	
	}

}