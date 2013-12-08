package housing.roles;

import housing.House;
import housing.interfaces.HousingLandlord;
import housing.interfaces.HousingRenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import base.interfaces.Person;

/*
 * @author David Carr, Maggi Yang
 */

public class HousingLandlordRole extends HousingBaseRole implements HousingLandlord {

	/* Data */

	public List<MyRenter> mRenterList = Collections
			.synchronizedList(new ArrayList<MyRenter>());
	public List<House> mHousesList = Collections
			.synchronizedList(new ArrayList<House>());
	int mMinCash = 50;
	int mMinSSN = 0;
	
	enum EnumRenterState {
		Initial, ApplyingForHousing, RentPaid, OwesRent, RentOverdue
	};

	private class MyRenter {
		HousingRenter mRenter;
		EnumRenterState mState;
		double mCash;
		House mHouse;
		int SSN;

		public MyRenter(HousingRenter renter, double cash, int mySSN) {
			mRenter = renter;
			mState = EnumRenterState.Initial;
			mCash = cash;
			mHouse = null;
			SSN = mySSN;
		}
	}
	
	/* Constructor */
	
	public HousingLandlordRole(Person person){
		super(person);
	}

	/* Messages */
	
	public void msgIWouldLikeToLiveHere(HousingRenter r, double cash, int SSN) {
		print("Message - I would like to live here received");
		MyRenter newRenter = new MyRenter(r, cash, SSN);
		newRenter.mState = EnumRenterState.ApplyingForHousing;
		mRenterList.add(newRenter);
		stateChanged();
	}

	public void msgHereIsPayment(int SSN, double paymentAmt) {
		print("Message - Here is bank statement recieved");
		mPerson.setCash(mPerson.getCash() + paymentAmt);
		MyRenter r = FindRenter(SSN);
		r.mState = EnumRenterState.RentPaid;
		stateChanged();
	}

	/* Scheduler */

	public boolean pickAndExecuteAnAction() {
		
		if(mTimeToCheckRent && mRenterList.size() > 0){
			mTimeToCheckRent = false;
			CollectRent(); 
			return true; 
		}
		
		synchronized (mRenterList) {
			for (MyRenter r : mRenterList) {
				if (r.mState == EnumRenterState.ApplyingForHousing) {
					ReviewApplicant(r);
					return true;
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
		
		return false;
	}

	/* Actions */
	private void CollectRent(){
		synchronized (mRenterList) {
			Iterator<MyRenter> itr = mRenterList.iterator();
			while (itr.hasNext()) {
				MyRenter renter = itr.next();
				if (renter.mState == EnumRenterState.RentOverdue) {
					GiveEvictionNotice(renter);
					itr.remove();
				}
			}
		}
		synchronized (mRenterList) {
			for (MyRenter r : mRenterList) {
				if (r.mState == EnumRenterState.OwesRent) {
					GiveRentOverdueNotice(r);
				}
			}
		}
		synchronized (mRenterList) {
			for (MyRenter r : mRenterList) {
				if (r.mState == EnumRenterState.RentPaid) {
					GiveRentDueNotice(r);
				}
			}
		}
	}
	
	private void GiveRentDueNotice(MyRenter r) {
		print("Action - GiveRentDueNotice");
		r.mState = EnumRenterState.OwesRent;
		r.mRenter.msgRentDue(mPerson.getSSN(), r.mHouse.mRent);
	}

	private void GiveRentOverdueNotice(MyRenter r) {
		print("Action - GiveRentOverdueNotice");
		r.mState = EnumRenterState.RentOverdue;
		r.mRenter.msgOverdueNotice(mPerson.getSSN(), r.mHouse.mRent);
	}

	private void GiveEvictionNotice(MyRenter r) {
		print("Action - GiveEvictionNotice");
		r.mRenter.msgEviction();
		synchronized (mHousesList) {
			for (House h : mHousesList) {
				if (h.mOccupant == r) {
					h.mOccupant = null;
				}
			}
		}
	}

	void ReviewApplicant(MyRenter r) {
		print("Action - ReviewApplicant");
		if (r.mCash >= mMinCash && r.SSN >= mMinSSN) {
			for(House h: mHousesList){
				if(h.mOccupant == null){
					r.mHouse = h; 
					r.mHouse.mOccupant = r.mRenter;
					r.mRenter.msgApplicationAccepted(r.mHouse);
					r.mState = EnumRenterState.RentPaid;
					return;
				}
			}
			r.mRenter.msgApplicationDenied();
			synchronized (mRenterList) {
				mRenterList.remove(r);
			}
			return;

		} else {
			r.mRenter.msgApplicationDenied();
			synchronized (mRenterList) {
				mRenterList.remove(r);
			}
			return;
		}
	}

	/* Utilities */

	MyRenter FindRenter(int SSN) {
		synchronized (mRenterList) {
			for (MyRenter r : mRenterList) {
				if (r.SSN == SSN) {
					return r;
				}
			}
		}
		return null;
	}
	
	public int getRenterListSize(){
		return mRenterList.size(); 
	}
	
	public int getNumAvailableHouses() {
		int numAvailableHouses = 0;
		for(House h: mHousesList){
			if(h.mOccupant == null){
				numAvailableHouses++;
			}
		}
		return numAvailableHouses;
	}

}
