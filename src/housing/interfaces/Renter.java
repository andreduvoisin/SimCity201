package housing.interfaces;

import housing.House;

/*
 * @author David Carr, Maggi Yang
 */

public interface Renter {

	public abstract void msgApplicationAccepted(House newHouse);

	public abstract void msgApplicationDenied();

	public abstract void msgRentDue(int landLordSSN, double total);

	public abstract void msgOverdueNotice(int landLordSSN, double total);

	public abstract void msgEviction();

	public abstract boolean pickAndExecuteAnAction();

}