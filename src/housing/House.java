package housing;

import housing.interfaces.Renter;

public class House {
	int xLocation, yLocation;
	double mRent;
	Renter mOccupant;

	public House(int x, int y, double rent) {
		xLocation = x;
		yLocation = y;
		mRent = rent;
		mOccupant = null;
	}

}