package transportation.interfaces;

import transportation.TransportationBusInstance;

public interface TransportationBus {
	public abstract void msgGuiArrivedAtStop(int busNum);
	public abstract void msgNeedARide(TransportationRider r, int riderCurrentStop);
	public abstract void msgImOn(TransportationRider r);
	public abstract void msgImOff(TransportationRider r);
	public abstract boolean pickAndExecuteAnAction();
	public abstract void TellRidersToGetOff();
	public abstract void TellRidersToBoard();
	public abstract void AdvanceToNextStop();
	public abstract boolean NoBusesBusy();
	public abstract void addBus(TransportationBusInstance tbi);
}
