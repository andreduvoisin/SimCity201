package housing.roles;

import housing.House;
import housing.gui.ResidentGui;
import housing.interfaces.Owner;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import base.BaseRole;

/*
 * @author David Carr, Maggi Yang
 */

public class OwnerRole extends BaseRole implements Owner {

	/* Data */

	Boolean mTimeToMaintain = false;
	House mHouse = null;
	private ResidentGui gui = new ResidentGui();
	private Semaphore isAnimating = new Semaphore(0, true);
	boolean isHungry = false;
	Timer mMintenanceTimer;
	TimerTask mMintenanceTimerTask = new TimerTask() {
		public void run() {
			mTimeToMaintain = true;
		}
	};

	/* Messages */

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
		// TODO: establish what triggers the RequestHousing() action

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
		print("Action - Eat at Home");
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void Maintain() {
		print("Action - Maintain");
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// TODO: run timer for some period of time, animate
	}

	/* Utilities */
	protected void print(String msg) {
		System.out.println("Renter - " + msg);
	}
}
