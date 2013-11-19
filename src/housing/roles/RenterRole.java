package housing.roles;

import housing.House;
import housing.interfaces.Landlord;
import housing.interfaces.Renter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import base.Role;
import base.interfaces.Person;

public class RenterRole extends Role implements Renter {

	/* Data */

	Person me;
	Landlord myLandLord;
	Boolean mTimeToMaintain = false;
	List<Bill> mBills = Collections.synchronizedList(new ArrayList<Bill>());
	House mHouse = null;

	Timer mMintenanceTimer;
	TimerTask mMintenanceTimerTask = new TimerTask() {
		public void run() {
			mTimeToMaintain = true;
		}
	};

	enum BillState {
		Pending, Paid
	};

	private class Bill {
		Landlord mLandLord;
		double mAmt;
		BillState mStatus;

		public Bill(Landlord lord, double rent) {
			mLandLord = lord;
			mAmt = rent;
			mStatus = BillState.Pending;
		}
	}

	/* Messages */

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
		//TODO: establish what triggers the RequestHousing() action
		
		if (mHouse != null) {
			synchronized (mBills) {
				for (Bill b: mBills) {
					if (b.mStatus == BillState.Pending) {
						PayBill(b);
						return true;
					}
				}
			}
		}
		
		if (mTimeToMaintain) {
			mTimeToMaintain = false;
			mMintenanceTimer.schedule(mMintenanceTimerTask, 10000000); // TODO: establish maintenance schedule
			Maintain();
			return true;
		}
		return false;
	}

	/* Actions */
	
	void RequestHousing() {
		print("Action - RequestHousing");
		myLandLord.msgIWouldLikeToLiveHere(this, me.getCash());
	}

	void PayBill(Bill b) {
		print("Action - PayBill");
		//me.bank.msgSendPayment(this, b.mLandLord, b.amt); //TODO: establish payment mechanism
		mBills.remove(b);
	}
	
	void Maintain() {
		print("Action - Maintain");
		//TODO: run timer for some period of time, animate
	}
	
	/* Utilities */
	
	protected void print(String msg) {
		System.out.println("Renter - "+msg);
	}
}
