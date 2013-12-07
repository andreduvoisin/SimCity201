package housing.interfaces;

import housing.House;
import base.Location;

public interface HousingBase {

	public abstract void msgTimeToCheckRent();

	public abstract void msgTimeToMaintain();

	public abstract void msgEatAtHome();

	public abstract void msgDoneAnimating();

	public abstract void setHouse(House h);
	
	public abstract House getHouse();

	public abstract Location getLocation();

}