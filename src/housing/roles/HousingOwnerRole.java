package housing.roles;

import housing.interfaces.HousingOwner;
import base.interfaces.Person;

/*
 * @author David Carr, Maggi Yang
 */

public class HousingOwnerRole extends HousingBaseRole implements HousingOwner {

	/* Data */

	public HousingOwnerRole(Person person) {
		super(person);
	}

	/* Scheduler */

	public boolean pickAndExecuteAnAction() {

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

}
