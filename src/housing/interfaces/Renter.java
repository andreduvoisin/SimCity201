package housing.interfaces;

import housing.House;

public interface Renter {

	public abstract void msgApplicationAccepted(House newHouse);

	public abstract void msgApplicationDenied();

	public abstract void msgRentDue(Landlord lord, double total);

	public abstract void msgOverdueNotice(Landlord lord, double total);

	public abstract void msgEviction();

	public abstract boolean pickAndExecuteAnAction();

}