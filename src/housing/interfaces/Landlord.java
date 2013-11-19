package housing.interfaces;

public interface Landlord {

	public abstract void msgIWouldLikeToLiveHere(Renter r, double cash, int SSN);

	public abstract void msgHereIsPayment(int SSN, double paymentAmt);

	public abstract boolean pickAndExecuteAnAction();

}