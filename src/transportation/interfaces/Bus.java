package transportation.interfaces;

public interface Bus {
	public void msgGuiArrivedAtStop();
	public void msgNeedARide(TransportationRider r, int riderCurrentStop);
	public void  msgImOn(TransportationRider r);
	public void msgImOff(TransportationRider r);
}
