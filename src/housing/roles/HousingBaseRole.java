package housing.roles;

import housing.House;
import housing.gui.HousingPersonGui;
import housing.interfaces.HousingBase;

import java.util.concurrent.Semaphore;

import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.SimCityGui;
import city.gui.trace.AlertTag;

public class HousingBaseRole extends BaseRole implements HousingBase {
	
	public boolean mHungry = false;
	public boolean mTimeToMaintain = false;
	public boolean mTimeToCheckRent = false;

	public Semaphore isAnimating = new Semaphore(0, true);
	public HousingPersonGui gui;
	public House mHouse = null;
	
	public HousingBaseRole(Person person) {
		super(person);
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
		SimCityGui.getInstance().cityview.mCityHousingList.get(mHouse.mHouseNum).mPanel.addGui(gui);
		stateChanged();
	}

	public void msgEatAtHome() {
		mHungry = true;
		SimCityGui.getInstance().cityview.mCityHousingList.get(mHouse.mHouseNum).mPanel.addGui(gui);
		stateChanged();
	}

	public void msgDoneAnimating() {
		isAnimating.release();
		stateChanged();
	}
	
	void EatAtHome() {
		print("EatAtHome");
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
	}

	void Maintain() {
		print("MaintainingHouse"); 
		gui.DoMaintainHouseC1();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.DoMaintainHouseC2();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.DoMaintainHouseC3();
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
	}
	
	public void setHouse(House h) {
		this.mHouse = h;
	}

	@Override
	public Location getLocation() {
		return ContactList.cHOUSE_LOCATIONS.get(mHouse.mHouseNum);
	}

	@Override
	public House getHouse() {
		if (mHouse == null) {
			print("house is null!");
		}
		return this.mHouse;
	}

	public void Do(String msg) {
		super.Do(msg, AlertTag.HOUSING);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.HOUSING);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.HOUSING, e);
	}
}
