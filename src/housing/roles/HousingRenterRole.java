package housing.roles;

import housing.interfaces.HousingLandlord;
import housing.interfaces.HousingRenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.interfaces.Person;
import base.reference.ContactList;
import city.gui.CityHousing;

/*
 * @author David Carr, Maggi Yang
 */

public class HousingRenterRole extends HousingBaseRole implements HousingRenter {

	/* Data */

	public HousingLandlord myLandLord;
	public List<Bill> mBills = Collections
			.synchronizedList(new ArrayList<Bill>());

	enum EnumBillState {
		Pending, Paid
	};

	public HousingRenterRole(Person person) {
		mPerson = person;
	}

	public HousingRenterRole() {
		super();
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

	public void msgApplicationAccepted(CityHousing newHouse) {
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

		if (mHouse != null) {
			if(!mBills.isEmpty()){
				synchronized (mBills) {
					for (Bill b : mBills) {
						if (b.mStatus == EnumBillState.Pending) {
							PayBill(b);
							return true;
						}
					}
				}
			}
			
			if (mHungry) {
				mHungry = false;
				EatAtHome();
				return true;
			}

			if (mTimeToMaintain) {
				mTimeToMaintain = false;
				Maintain();
				return true;
			}
		}
		
		return false;
	}

	/* Actions */

	void RequestHousing() {
		print("Action - RequestHousing");
		myLandLord.msgIWouldLikeToLiveHere(this, mPerson.getCash(),
				mPerson.getSSN());
	}

	void PayBill(Bill b) {
		print("Action - PayBill");
		ContactList.SendPayment(mPerson.getSSN(), b.mLandLordSSN, b.mAmt);
		mBills.remove(b);
	}

	/* Utilities */
	public void setLandlord(HousingLandlord landlord) {
		myLandLord = landlord;
	}

}
