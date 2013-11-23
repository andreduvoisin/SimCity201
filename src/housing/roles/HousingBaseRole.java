package housing.roles;

import housing.House;

import java.util.concurrent.Semaphore;

import base.BaseRole;

public class HousingBaseRole extends BaseRole {
	
	boolean mHungry = false;
	boolean mTimeToMaintain = false;
	public boolean mTimeToCheckRent = false;
	Semaphore isAnimating = new Semaphore(0, true);
	House mHouse = null;
	
	public void msgTimeToMaintain() {
		mTimeToMaintain = true;
		stateChanged();
	}

	public void msgEatAtHome() {
		mHungry = true;
		stateChanged();
	}
	
	public void msgTimeToCheckRent() {
		stateChanged();
	}

	public void msgDoneAnimating() {
		isAnimating.release();
		stateChanged();
	}

}
