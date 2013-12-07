package housing;

import housing.interfaces.HousingRenter;
import city.gui.CityHousing;

public class House {
	
	public double mRent;
	public int mHouseNum;
	public HousingRenter mOccupant;
	public enum HousingType{Apartment, House}; 
	public HousingType type; 
	public final double maxAptRent = 450.00;  
	public CityHousing cityhouse;
	
	public House(int ID, double rent) {
		mHouseNum = ID;
		mOccupant = null;
		mRent = rent;
		if(rent <= maxAptRent){
			type = HousingType.Apartment; 
		}
		else{
			type = HousingType.House; 
		}	
	}
}
