package housing.roles;

import housing.House;
import housing.gui.HousingPersonGui;

import java.util.concurrent.Semaphore;

import base.BaseRole;

public class HousingBaseRole extends BaseRole {
	
	public boolean mHungry = false;
	public boolean mTimeToMaintain = false;
	
	Semaphore isAnimating = new Semaphore(0, true);
	HousingPersonGui gui;
	public House mHouse = null;
	
	public void msgTimeToMaintain() {
		mTimeToMaintain = true;
		stateChanged();
	}

	public void msgEatAtHome() {
		mHungry = true;
		stateChanged();
	}

	public void msgDoneAnimating() {
		isAnimating.release();
		stateChanged();
	}
	
	void EatAtHome() {
		gui.DoCookAndEatFood();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		print("Action - Eat at Home");
	}

	void Maintain() {
		gui.DoMaintainHouse();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		print("Action - Maintain");
	}
	
	public void setGui(HousingPersonGui g) {
		gui = g;
	}

}
