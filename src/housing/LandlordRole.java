package housing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import interfaces.Person;
import base.Role;

public class LandlordRole extends Role {

	/* Data */

	Person me;
	Timer mRentTimer;
	TimerTask mRentTimerTask = new TimerTask() {
		public void run() {
			mTimeToCheckRent = true;
		}
	};
	List<Renter> mRenterList = Collections
			.synchronizedList(new ArrayList<Renter>());
	List<House> mHousesList = Collections
			.synchronizedList(new ArrayList<House>());
	int mMinCreditScoreRequirement;

	enum RenterState {
		Initial, ApplyingForHousing, RentPaid, OwesRent, RentOverdue
	};

	boolean mTimeToCheckRent = false;

	private class Renter {
		Person p;
		RenterState mState;
		int mCreditscore;
		House mHouse;

		public Renter(Person person, int score) {
			p = person;
			mState = RenterState.Initial;
			mCreditscore = score;
			mHouse = null;
		}
	}

	/* Messages */

	public void msgIWouldLikeToLiveHere(Person p, int creditScore) {
		mRenterList.add(new Renter(p, creditScore));
		stateChanged();
	}

	public void msgHereIsBankStatement(int SSN, double paymentAmt) {
		Renter r = FindRenter(SSN);
		r.mState = RenterState.RentPaid;
		stateChanged();
	}

	/* Scheduler */

	public boolean pickAndExecuteAnAction() {

		synchronized (mRenterList) {
			for (Renter r : mRenterList) {
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
				for (Renter r : mRenterList) {
					if (r.mState == RenterState.RentOverdue) {
						GiveEvictionNotice(r);
						return true;
					}
				}
			}
			synchronized (mRenterList) {
				for (Renter r : mRenterList) {
					if (r.mState == RenterState.OwesRent) {
						GiveRentOverdueNotice(r);
						return true;
					}
				}
			}
			synchronized (mRenterList) {
				for (Renter r : mRenterList) {
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

	private void GiveRentDueNotice(Renter r) {
		r.mState = RenterState.OwesRent;
		r.p.msgRentDue(this, r.mHouse.mRent);
	}

	private void GiveRentOverdueNotice(Renter r) {
		r.mState = RenterState.RentOverdue;
		r.p.msgRentDue(this, r.mHouse.mRent);
	}

	private void GiveEvictionNotice(Renter r) {
		r.p.msgEviction();
		mHousesList.get(r.mHouse).mOccupant = null;
		synchronized (mRenterList) {
			mRenterList.remove(r);
		}
	}

	void ReviewApplicant(Renter r) {
		if (r.mCreditscore >= mMinCreditScoreRequirement) {
			r.mHouse = mHousesList.get(0);
			r.mHouse.mOccupant = r.p;
			r.p.msgApplicationAccepted(r.mHouse);
			r.mState = RenterState.RentPaid;
		} else {
			r.p.msgApplicationDenied();
			synchronized (mRenterList) {
				mRenterList.remove(r);
			}
		}
	}

	/* Utilities */

	Renter FindRenter(int SSN) {
		// TODO: Implement renter lookup
		return null;
	}
}
