package housing.roles;

import housing.interfaces.HousingOwner;
import base.interfaces.Person;

/*
 * @author David Carr, Maggi Yang
 */

public class HousingOwnerRole extends HousingBaseRole implements HousingOwner {

	/* Data */

	public HousingOwnerRole(Person person) {
		mPerson = person;
	}
	
	public HousingOwnerRole() {
	}

	/* Scheduler */

	public boolean pickAndExecuteAnAction() {
		// DAVID MAGGI: establish what triggers the RequestHousing() action

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

	/* Utilities */
	protected void print(String msg) {
		System.out.println("Owner - " + msg);
	}
}
