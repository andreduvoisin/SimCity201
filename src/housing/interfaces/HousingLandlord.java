package housing.interfaces;

/*
 * @author David Carr, Maggi Yang
 */

public interface HousingLandlord {

	public abstract void msgIWouldLikeToLiveHere(HousingRenter r, double cash, int SSN);

	public abstract void msgHereIsPayment(int SSN, double paymentAmt);

	public abstract boolean pickAndExecuteAnAction();
	
	public abstract int getRenterListSize(); 
	

}