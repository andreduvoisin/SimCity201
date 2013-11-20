package housing;

import housing.interfaces.Renter;

/*
 * House class
 * @author David Carr
 */

public class House {
	public int xLocation, yLocation;
	public double mRent;
	public Renter mOccupant;

	public House(int x, int y, double rent) {
		xLocation = x;
		yLocation = y;
		mRent = rent;
		mOccupant = null;
	}

}