package housing.interfaces;

import base.reference.House;

public interface HousingBase {

	public abstract void msgTimeToCheckRent();

	public abstract void msgTimeToMaintain();

	public abstract void msgEatAtHome();

	public abstract void msgDoneAnimating();

	public abstract void setHouse(House h);
	
	public abstract House getHouse();

}