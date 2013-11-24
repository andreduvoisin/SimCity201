package housing.interfaces;

import city.gui.CityHousing;

/*
 * @author David Carr, Maggi Yang
 */

public interface HousingRenter {

	public abstract void msgApplicationAccepted(CityHousing newHouse);

	public abstract void msgApplicationDenied();

	public abstract void msgRentDue(int landLordSSN, double total);

	public abstract void msgOverdueNotice(int landLordSSN, double total);

	public abstract void msgEviction();

	public abstract boolean pickAndExecuteAnAction();

	public abstract void setLandlord(HousingLandlord landlord); 
}