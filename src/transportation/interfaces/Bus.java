package transportation.interfaces;

import transportation.TransportationBus;
import city.gui.CityBus;

public interface Bus {
	public void msgGuiArrivedAtStop();
	public void msgNeedARide(TransportationRider r, int riderCurrentStop);
	
	public void  msgImOn(TransportationRider r);
	public void msgImOff(TransportationRider r);
	public boolean pickAndExecuteAnAction();
	public String getName();
	public CityBus getBusGui();
	public TransportationBus getInstance();
	
}
