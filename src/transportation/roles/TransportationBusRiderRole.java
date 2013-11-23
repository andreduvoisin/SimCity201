package transportation.roles;

import base.BaseRole;
import transportation.*;
import transportation.gui.*;
import transportation.interfaces.*;

public class TransportationBusRiderRole extends BaseRole implements TransportationRider {

	public TransportationBusRiderRole() {
		state = enumState.none;
	}


	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	private TransportationBusDispatch mBusDispatch;
	private TransportationBusRiderGui mGui = new TransportationBusRiderGui();

	private int mCurrentLocation;
	private int mDestination;
	
	private enum enumState { none, askForRide, waiting, toldToBoard, boarding, boarded,
								riding, atDestination, exiting }
	private enumState state;
	

	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

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
		mBusDispatch.msgNeedARide(this, mCurrentLocation);
		state = enumState.waiting;
	}

	private void BoardBus() {
		mGui.DoBoardBus();
		state = enumState.boarding;
	}

	private void TellBusBoarded() {
		mBusDispatch.msgImOn(this);
	}

	private void ExitBus() {
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
