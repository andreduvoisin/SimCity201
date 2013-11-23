package housing.roles;

import housing.House;
import housing.gui.HousingResidentGui;
import housing.interfaces.HousingLandlord;
import housing.interfaces.HousingRenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.interfaces.Person;

/*
 * @author David Carr, Maggi Yang
 */

public class HousingRenterRole extends HousingBaseRole implements HousingRenter {

	/* Data */

	public HousingLandlord myLandLord;
	List<Bill> mBills = Collections.synchronizedList(new ArrayList<Bill>());
	private HousingResidentGui gui = new HousingResidentGui();

	enum EnumBillState {
		Pending, Paid
	};

	public HousingRenterRole(Person person) {
		mPerson = person;
	}

	public HousingRenterRole() {
	}

	private class Bill {
		int mLandLordSSN;
		double mAmt;
		EnumBillState mStatus;

		public Bill(int lordssn, double rent) {
			mLandLordSSN = lordssn;
			mAmt = rent;
			mStatus = EnumBillState.Pending;
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

	public void msgRentDue(int ssn, double total) {
		print("Message- msgRentDue");
		mBills.add(new Bill(ssn, total));
		stateChanged();
	}

	public void msgOverdueNotice(int ssn, double total) {
		print("Message - msgOverdueNotice");
		mBills.add(new Bill(ssn, total));
		stateChanged();
	}

	public void msgEviction() {
		print("Message - msgEviction");
		mHouse = null;
		// DoLeaveHouse() //Some eviction animation
	}

	/* Scheduler */

	public boolean pickAndExecuteAnAction() {
		// DAVID MAGGI: establish what triggers the RequestHousing() action

		if (mHungry) {
			mHungry = false;
			EatAtHome();
			return true;
		}

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

	void RequestHousing() {
		print("Action - RequestHousing");
		myLandLord.msgIWouldLikeToLiveHere(this, mPerson.getCash(),
				mPerson.getSSN());
	}

	void PayBill(Bill b) {
		print("Action - PayBill");
		// mPerson.getMasterTeller().msgSendPayment(mPerson.getSSN(),
		// b.mLandLordSSN, b.mAmt);
		mBills.remove(b);
	}

	void Maintain() {
		/*gui.DoMaintainHouse();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		print("Action - Maintain");
		// DAVID MAGGI: run timer for some period of time, animate
	}

	/* Utilities */
	public void setLandlord(HousingLandlord landlord) {
		myLandLord = landlord;
	}

	protected void print(String msg) {
		System.out.println("Renter - " + msg);
	}

	public void setGui(HousingResidentGui g) {
		gui = g;
	}
}
