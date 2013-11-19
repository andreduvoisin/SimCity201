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

public class LandlordRole extends Role implements Landlord {

	/* Data */

	Person me;
	Timer mRentTimer;
	TimerTask mRentTimerTask = new TimerTask() {
		public void run() {
			mTimeToCheckRent = true;
		}
	};
	List<MyRenter> mRenterList = Collections
			.synchronizedList(new ArrayList<MyRenter>());
	List<House> mHousesList = Collections
			.synchronizedList(new ArrayList<House>());
	int mMinCreditScoreRequirement;

	enum RenterState {
		Initial, ApplyingForHousing, RentPaid, OwesRent, RentOverdue
	};

	boolean mTimeToCheckRent = false;

	private class MyRenter {
		Renter mRenter;
		RenterState mState;
		double mCreditscore;
		House mHouse;

		public MyRenter(Renter renter, double score) {
			mRenter = renter;
			mState = RenterState.Initial;
			mCreditscore = score;
			mHouse = null;
		}
	}

	/* Messages */

	public void msgIWouldLikeToLiveHere(Renter r, double creditScore) {
		print("Message - I would like to live here recieved");
		mRenterList.add(new MyRenter(r, creditScore));
		stateChanged();
	}

	public void msgHereIsBankStatement(int SSN, double paymentAmt) {
		print("Message - Here is bank statement recieved");
		MyRenter r = FindRenter(SSN);
		r.mState = RenterState.RentPaid;
		stateChanged();
	}

	/* Scheduler */

	public boolean pickAndExecuteAnAction() {

		synchronized (mRenterList) {
			for (MyRenter r : mRenterList) {
				if (r.mState == RenterState.ApplyingForHousing) {
					ReviewApplicant(r);
					return true;
				}
			}
		}

		if (mTimeToCheckRent && mRenterList.size() > 0) {
			mTimeToCheckRent = false;
			mRentTimer.schedule(mRentTimerTask, 1000000); // TODO: establish
															// schedule for rent
			synchronized (mRenterList) {
				for (MyRenter r : mRenterList) {
					if (r.mState == RenterState.RentOverdue) {
						GiveEvictionNotice(r);
						return true;
					}
				}
			}
			synchronized (mRenterList) {
				for (MyRenter r : mRenterList) {
					if (r.mState == RenterState.OwesRent) {
						GiveRentOverdueNotice(r);
						return true;
					}
				}
			}
			synchronized (mRenterList) {
				for (MyRenter r : mRenterList) {
					if (r.mState == RenterState.RentPaid) {
						GiveRentDueNotice(r);
						return true;
					}
				}
			}
		}
		return false;
	}

	/* Actions */

	private void GiveRentDueNotice(MyRenter r) {
		print("Action - GiveRentDueNotice");
		r.mState = RenterState.OwesRent;
		r.mRenter.msgRentDue(this, r.mHouse.mRent);
	}

	private void GiveRentOverdueNotice(MyRenter r) {
		print("Action - GiveRentOverdueNotice");
		r.mState = RenterState.RentOverdue;
		r.mRenter.msgRentDue(this, r.mHouse.mRent);
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
		synchronized (mRenterList) {
			mRenterList.remove(r);
		}
	}

	void ReviewApplicant(MyRenter r) {
		print("Action - ReviewApplicant");
		if (r.mCreditscore >= mMinCreditScoreRequirement) {
			r.mHouse = mHousesList.get(0);
			r.mHouse.mOccupant = r.mRenter;
			r.mRenter.msgApplicationAccepted(r.mHouse);
			r.mState = RenterState.RentPaid;
		} else {
			r.mRenter.msgApplicationDenied();
			synchronized (mRenterList) {
				mRenterList.remove(r);
			}
		}
	}

	/* Utilities */

	public void setPerson(Person p){
		me = p; 
	}
	
	MyRenter FindRenter(int SSN) {
		// TODO: Implement renter lookup
		return null;
	}
	
	protected void print(String msg) {
		System.out.println("Landlord - "+msg);
	}

}
