package transportation.roles;

import base.BaseRole;
import base.interfaces.Person;
import transportation.*;
import transportation.gui.*;
import transportation.interfaces.*;

public class TransportationBusRiderRole extends BaseRole implements TransportationRider {

	public TransportationBusRiderRole() {
		state = enumState.none;
		mGui = new TransportationBusRiderGui(this);
	}

	public TransportationBusRiderRole(Person person) {
		mPerson = person;
		state = enumState.none;
		mGui = new TransportationBusRiderGui(this);
	}


	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	public TransportationBusDispatch mBusDispatch;
	public TransportationBusRiderGui mGui;

	public int mCurrentLocation;
	public int mDestination;
	
	private enum enumState { none, askForRide, waiting, toldToBoard, boarding, boarded,
								riding, atDestination, exiting }
	private enumState state;
	

	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

	public void msgReset(int currentStop, int destinationStop) {
		state = enumState.askForRide;
		stateChanged();
	}

	/**
	 * From GUI
	 * Sent when rider has completed boarding or exiting the bus
	 */
	public void msgGuiDone() {
		if (state.equals(enumState.boarding)) {
			state = enumState.boarded;
		}
		else if (state.equals(enumState.exiting)) {
			state = enumState.none;
		}
		stateChanged();
	}

	/**
	 * From BusDispatch
	 * Sent when a BusInstance is at this Rider's current stop
	 */
	public void msgBoardBus() {
		state = enumState.toldToBoard;
		stateChanged();
	}

	/**
	 * From BusDispatch
	 * Sent when this Rider's BusInstance has reached this Rider's destination
	 */
	public void msgAtYourStop() {
		state = enumState.atDestination;
		stateChanged();
	}


	// ==================================================================================
	// ----------------------------------- SCHEDULER ------------------------------------
	// ==================================================================================

	public boolean pickAndExecuteAnAction() {

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

		return false;
	}


	// ==================================================================================
	// ----------------------------------- ACTIONS --------------------------------------
	// ==================================================================================

	private void AskForRide() {
		System.out.println("AskForRide from " + mCurrentLocation);
		mBusDispatch.msgNeedARide(this, mCurrentLocation);
		state = enumState.waiting;
	}

	private void BoardBus() {
		System.out.println("BoardBus()");
		mGui.DoBoardBus();
		state = enumState.boarding;
	}

	private void TellBusBoarded() {
		System.out.println("TellBusBoarded()");
		mBusDispatch.msgImOn(this);
	}

	private void ExitBus() {
		System.out.println("ExitBus()");
		mGui.DoExitBus();
		state = enumState.exiting;
	}


	// ==================================================================================
	// ---------------------------------- ACCESSORS -------------------------------------
	// ==================================================================================

	public void setLocation(int loc) {
		mCurrentLocation = loc;
	}

	public int getLocation() {
		return mCurrentLocation;
	}

	public void setDestination(int dest) {
		mDestination = dest;
	}

	public int getDestination() {
		return mDestination;
	}
}
