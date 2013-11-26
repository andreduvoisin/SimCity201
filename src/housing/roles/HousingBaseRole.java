package housing.roles;

import housing.gui.HousingPersonGui;

import java.util.concurrent.Semaphore;

import base.BaseRole;
import city.gui.CityHousing;

public class HousingBaseRole extends BaseRole {
	
	public boolean mHungry = false;
	public boolean mTimeToMaintain = false;
	public boolean mTimeToCheckRent = false;
	
	public Semaphore isAnimating = new Semaphore(0, true);
	HousingPersonGui gui;
	public CityHousing mHouse = null;
	
	public HousingBaseRole() {
		super(null);
		gui = new HousingPersonGui();
		gui.housingrole = this;
		gui.setPresent(true);
	}
	
	public void msgTimeToCheckRent() {
		mTimeToCheckRent = true;
		stateChanged();
	}
	
	public void msgTimeToMaintain() {
		print("msgTimeToMaintain recieved");
		mTimeToMaintain = true;
		System.out.println(mPerson.getName());
		mHouse.mPanel.addGui(gui);
		stateChanged();
	}

	public void msgEatAtHome() {
		mHungry = true;
		System.out.println(mHouse.mHouseNum);
		mHouse.mPanel.addGui(gui);
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
		gui.DoGoRelax(); 
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		print("Action - Eat at Home");
	}

	void Maintain() {
		print("MaintainingHouse"); 
		gui.DoMaintainHouse();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.DoGoRelax(); 
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		print("Action - Maintain");
	}
	
	public void setHouse(CityHousing h) {
		print("set house");
		this.mHouse = h;
	}

}
