package housing.interfaces;

import city.gui.CityHousing;

public interface HousingBase {

	public abstract void msgTimeToCheckRent();

	public abstract void msgTimeToMaintain();

	public abstract void msgEatAtHome();

	public abstract void msgDoneAnimating();

	public abstract void setHouse(CityHousing h);
	
	public abstract CityHousing getHouse();

}