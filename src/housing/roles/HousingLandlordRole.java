package housing.roles;

import housing.House;
import housing.gui.HousingLandlordGui;
import housing.interfaces.HousingLandlord;
import housing.interfaces.HousingRenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import base.BaseRole;
import base.interfaces.Role;

/*
 * @author David Carr, Maggi Yang
 */

public class HousingLandlordRole extends BaseRole implements HousingLandlord {

	/* Data */

	Timer mRentTimer = new Timer();
	TimerTask mRentTimerTask = new TimerTask() {
		public void run() {
			mTimeToCheckRent = true;
		}
	};
	public List<MyRenter> mRenterList = Collections
			.synchronizedList(new ArrayList<MyRenter>());
	public List<House> mHousesList = Collections
			.synchronizedList(new ArrayList<House>());
	int mMinCash = 50;
	int mMinSSN = 0;
	private HousingLandlordGui gui = new HousingLandlordGui();
	private Semaphore isAnimating = new Semaphore(0, true);

	enum EnumRenterState {
		Initial, ApplyingForHousing, RentPaid, OwesRent, RentOverdue
	};

	public boolean mTimeToCheckRent = false;

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
	
	public HousingLandlordRole(){
		
		//DAVID MAGGI: remove after finishing config file, for testing purposes only 
		mHousesList.add(new House(20, 20, 100.00)); 
		mHousesList.add(new House(30, 30, 250.00));
	}

	/* Messages */

	public void msgDoneAnimating() {
		isAnimating.release();
		stateChanged();
	}

	public void msgIWouldLikeToLiveHere(HousingRenter r, double cash, int SSN) {
		print("Message - I would like to live here recieved");
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

		synchronized (mRenterList) {
			for (MyRenter r : mRenterList) {
				if (r.mState == EnumRenterState.ApplyingForHousing) {
					ReviewApplicant(r);
					return true;
				}
			}
		}

		if (mTimeToCheckRent && mRenterList.size() > 0) {
			mTimeToCheckRent = false;
			mRentTimer.schedule(mRentTimerTask, 1000000); //DAVID MAGGI: establish schedule for rent
			synchronized (mRenterList) {
				for (MyRenter r : mRenterList) {
					if (r.mState == EnumRenterState.RentOverdue) {
						GiveEvictionNotice(r);
						return true;
					}
				}
			}
			synchronized (mRenterList) {
				for (MyRenter r : mRenterList) {
					if (r.mState == EnumRenterState.OwesRent) {
						GiveRentOverdueNotice(r);
						return true;
					}
				}
			}
			synchronized (mRenterList) {
				for (MyRenter r : mRenterList) {
					if (r.mState == EnumRenterState.RentPaid) {
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
		r.mState = EnumRenterState.OwesRent;
		r.mRenter.msgRentDue(mPerson.getSSN(), r.mHouse.mRent);
	}

	private void GiveRentOverdueNotice(MyRenter r) {
		print("Action - GiveRentOverdueNotice");
		r.mState = EnumRenterState.RentOverdue;
		r.mRenter.msgRentDue(mPerson.getSSN(), r.mHouse.mRent);
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
		if (r.mCash >= mMinCash && r.SSN >= mMinSSN) {
			for(House h: mHousesList){
				if(h.mOccupant == null){
					r.mHouse = h; 
				}
			}
			r.mHouse.mOccupant = r.mRenter;
			r.mRenter.msgApplicationAccepted(r.mHouse);
			r.mState = EnumRenterState.RentPaid;
		} else {
			r.mRenter.msgApplicationDenied();
			synchronized (mRenterList) {
				mRenterList.remove(r);
			}
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

	protected void print(String msg) {
		System.out.println("Landlord - " + msg);
	}

	public static Role getNextRole() {
		// DAVID: ADD GETNEXTROLE METHODS
		HousingLandlordRole landlord = new HousingLandlordRole();
		for (int i=0; i<4; i++) {
			landlord.mHousesList.add(new House(5, 5, 60));				
		}
		
		return landlord;
	}

}
