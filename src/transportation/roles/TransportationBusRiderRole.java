package transportation.roles;

import java.util.concurrent.Semaphore;

import city.gui.trace.AlertTag;
import transportation.TransportationBusDispatch;
import transportation.gui.TransportationBusRiderGui;
import transportation.interfaces.TransportationRider;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;

public class TransportationBusRiderRole extends BaseRole implements TransportationRider {

	public TransportationBusRiderRole(Person person) {
		super(person);
		mPerson = person;
		state = enumState.goToStop;
		mGui = new TransportationBusRiderGui(this);
	}


	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	public TransportationBusDispatch mBusDispatch;
	public TransportationBusRiderGui mGui;
	public Semaphore semGuiMoving = new Semaphore(0, true);

	public int mCurrentStop;
	public int mDestinationStop;
	
	private enum enumState { none, goToStop, going, atStop, askForRide, waiting, toldToBoard, boarding, boarded,
								riding, atDestination, exiting, off }
	private enumState state;
	

	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

	public void msgReset() {
		print("Received msgReset");
		state = enumState.askForRide;
		stateChanged();
	}

	/**
	 * From GUI
	 * Sent when rider has completed boarding or exiting the bus, or arrived at bus stop
	 */
	public void msgGuiDone() {
		print("Received msgGuiDone");
		if (state.equals(enumState.going)) {
			semGuiMoving.release();
			state = enumState.askForRide;
		}
		else if (state.equals(enumState.boarding)) {
			state = enumState.boarded;
		}
		else if (state.equals(enumState.exiting)) {
			state = enumState.off;
		}
		else return;
		// Only stateChanged() if correct state
		stateChanged();
	}

	/**
	 * From BusDispatch
	 * Sent when a BusInstance is at this Rider's current stop
	 */
	public void msgBoardBus() {
		print("Received msgBoardBus");
		state = enumState.toldToBoard;
		stateChanged();
	}

	/**
	 * From BusDispatch
	 * Sent when this Rider's BusInstance has reached this Rider's destination
	 */
	public void msgAtYourStop() {
		print("Received msgAtYourStop");
		state = enumState.atDestination;
		stateChanged();
	}


	// ==================================================================================
	// ----------------------------------- SCHEDULER ------------------------------------
	// ==================================================================================

	public boolean pickAndExecuteAnAction() {
		print("busrider pAEA called w/ state = " + state.toString());

		if (state.equals(enumState.goToStop)) {
			GoToNearestStop();
			return true;
		}

		if (state.equals(enumState.askForRide)) {
			AskForRide();
			return true;
		}

		if (state.equals(enumState.toldToBoard)) {
			BoardBus();
			return true;
		}

		if (state.equals(enumState.boarded)) {
			TellBusBoarded();
			return true;
		}

		if (state.equals(enumState.atDestination)) {
			ExitBus();
			return true;
		}

		if (state.equals(enumState.off)) {
			TellBusUnloaded();
			return true;
		}

		return false;
	}


	// ==================================================================================
	// ----------------------------------- ACTIONS --------------------------------------
	// ==================================================================================

	private void GoToNearestStop() {
		print("GoToNearestStop()");
		mGui.DoGoToStop(0);
		state = enumState.going;
		acquire(semGuiMoving);
	}

	private void AskForRide() {
		print("AskForRide from " + mCurrentStop);
		mBusDispatch.msgNeedARide(this, mCurrentStop);
		state = enumState.waiting;
	}

	private void BoardBus() {
		print("BoardBus()");
		mGui.DoBoardBus();
		state = enumState.boarding;
	}

	private void TellBusBoarded() {
		print("TellBusBoarded()");
		mBusDispatch.msgImOn(this);
	}

	private void ExitBus() {
		print("ExitBus()");
		mGui.DoExitBus();
		state = enumState.exiting;
	}

	private void TellBusUnloaded() {
		print("TellBusUnloaded()");
		mBusDispatch.msgImOff(this);
		state = enumState.none;
	}


	// ==================================================================================
	// ---------------------------------- ACCESSORS -------------------------------------
	// ==================================================================================

	public void setLocation(int loc) {
		print("setLocation(" + loc + ")");
		mCurrentStop = loc;
	}

	public int getStop() {
		return mCurrentStop;
	}

	public void setDestination(int dest) {
		mDestinationStop = dest;
	}

	public int getDestination() {
		return mDestinationStop;
	}

	private void acquire(Semaphore s) {
		try { s.acquire(); } catch (Exception e) {}
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void Do(String msg) {
		super.Do(msg, AlertTag.TRANSPORTATION);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.TRANSPORTATION);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.TRANSPORTATION, e);
	}
}
