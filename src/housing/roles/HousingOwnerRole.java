package housing.roles;

import housing.House;
import housing.gui.HousingResidentGui;
import housing.interfaces.HousingOwner;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import base.BaseRole;
import base.interfaces.Person;

/*
 * @author David Carr, Maggi Yang
 */

public class HousingOwnerRole extends BaseRole implements HousingOwner {

	/* Data */

	Boolean mTimeToMaintain = false;
	House mHouse = null;
	private HousingResidentGui gui = new HousingResidentGui();
	private Semaphore isAnimating = new Semaphore(0, true);
	boolean isHungry = false;
	Timer mMintenanceTimer;
	TimerTask mMintenanceTimerTask = new TimerTask() {
		public void run() {
			mTimeToMaintain = true;
		}
	};

	/* Messages */

	public HousingOwnerRole(Person person) {
		mPerson = person;
	}
	
	public HousingOwnerRole() {
	}

	public void msgEatAtHome() {
		isHungry = true;
		stateChanged();
	}

	public void msgDoneAnimating() {
		isAnimating.release();
		stateChanged();
	}


	/* Scheduler */

	public boolean pickAndExecuteAnAction() {
		// DAVID MAGGI: establish what triggers the RequestHousing() action

		if (isHungry) {
			isHungry = false;
			EatAtHome();
			return true;
		}


		if (mTimeToMaintain) {
			mTimeToMaintain = false;
			mMintenanceTimer.schedule(mMintenanceTimerTask, 10000);
			Maintain();
			return true;
		}
		return false;
	}

	/* Actions */

	void EatAtHome() {
		/*gui.DoCookAndEatFood();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		print("Action - Eat at Home");
	}

	void Maintain() {
		/*gui.DoMaintainHouse();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		print("Action - Maintain");
	}

	/* Utilities */
	protected void print(String msg) {
		System.out.println("Renter - " + msg);
	}
}
