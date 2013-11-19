package housing.roles;

import housing.House;
import housing.gui.RenterGui;
import housing.interfaces.Landlord;
import housing.interfaces.Renter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import base.Role;
import base.interfaces.Person;

public class RenterRole extends Role implements Renter {

	/* Data */

	Person me;
	Landlord myLandLord;
	Boolean mTimeToMaintain = false;
	List<Bill> mBills = Collections.synchronizedList(new ArrayList<Bill>());
	House mHouse = null;
	private RenterGui gui = new RenterGui();
	private Semaphore isAnimating = new Semaphore(0, true);
	Timer mMintenanceTimer;
	TimerTask mMintenanceTimerTask = new TimerTask() {
		public void run() {
			mTimeToMaintain = true;
		}
	};

	enum EnumBillState {
		Pending, Paid
	};

	private class Bill {
		Landlord mLandLord;
		double mAmt;
		EnumBillState mStatus;

		public Bill(Landlord lord, double rent) {
			mLandLord = lord;
			mAmt = rent;
			mStatus = EnumBillState.Pending;
		}
	}

	/* Messages */

	public void msgDoneAnimating() {
		isAnimating.release();
		stateChanged();
	}

	public void msgApplicationAccepted(House newHouse) {
		print("Message - msgApplicationAccepted");
		mHouse = newHouse;
		stateChanged();
	}

	public void msgApplicationDenied() {
		print("Message - msgApplicationDenied");
		mHouse = null;
		stateChanged();
	}

	public void msgRentDue(Landlord lord, double total) {
		print("Message- msgRentDue");
		mBills.add(new Bill(lord, total));
		stateChanged();
	}

	public void msgOverdueNotice(Landlord lord, double total) {
		print("Message - msgOverdueNotice");
		mBills.add(new Bill(lord, total));
		stateChanged();
	}

	public void msgEviction() {
		print("Message - msgEviction");
		mHouse = null;
		// DoLeaveHouse() //Some eviction animation
	}

	/* Scheduler */

	public boolean pickAndExecuteAnAction() {
		// TODO: establish what triggers the RequestHousing() action

		if (mHouse != null) {
			synchronized (mBills) {
				for (Bill b : mBills) {
					if (b.mStatus == EnumBillState.Pending) {
						PayBill(b);
						return true;
					}
				}
			}
		}

		if (mTimeToMaintain) {
			mTimeToMaintain = false;
			mMintenanceTimer.schedule(mMintenanceTimerTask, 10000000); // TODO:
																		// establish
																		// maintenance
																		// schedule
			Maintain();
			return true;
		}
		return false;
	}

	/* Actions */

	void RequestHousing() {
		print("Action - RequestHousing");
		myLandLord.msgIWouldLikeToLiveHere(this, me.getCash(), me.getSSN());
	}

	void PayBill(Bill b) {
		print("Action - PayBill");
		// me.bank.msgSendPayment(this, b.mLandLord, b.amt); //TODO: establish
		// payment mechanism
		mBills.remove(b);
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

	
	public void setPerson(Person p){
		me = p; 
	}

	protected void print(String msg) {
		System.out.println("Renter - " + msg);
	}

}
