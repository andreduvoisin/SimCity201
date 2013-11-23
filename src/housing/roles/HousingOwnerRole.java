package housing.roles;

import housing.gui.HousingResidentGui;
import housing.interfaces.HousingOwner;
import base.interfaces.Person;

/*
 * @author David Carr, Maggi Yang
 */

public class HousingOwnerRole extends HousingBaseRole implements HousingOwner {

	/* Data */

	private HousingResidentGui gui = new HousingResidentGui();

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

	void EatAtHome() {
		/*gui.DoCookAndEatFood();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		print("Action - Eat at Home");
	}

	void Maintain() {
		/*gui.DoMaintainHouse();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		print("Action - Maintain");
	}

	/* Utilities */
	protected void print(String msg) {
		System.out.println("Renter - " + msg);
	}
}
