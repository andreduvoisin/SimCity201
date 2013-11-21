package transportation.roles;

import base.BaseRole;
import transportation.*;
import transportation.gui.*;
import transportation.interfaces.*;

public class BusRiderRole extends BaseRole implements Rider {

	public BusRiderRole() {
		state = enumState.none;
	}


	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	private BusDispatch mBusDispatch;
	private BusRiderGui mGui = new BusRiderGui();

	private int mCurrentLocation;
	private int mDestination;
	
	private enum enumState { none, askForRide, waiting, toldToBoard, boarding,
								riding, atDestination, exiting }
	private enumState state;
	



	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

	public void msgBoardBus() {
		state = enumState.toldToBoard;
		stateChanged();
	}

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

		// TODO Chase: message BusDispatch msgGoingTo

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

	public void ExitBus() {
		mGui.DoExitBus();
		state = enumState.exiting;
	}


	// ==================================================================================
	// ---------------------------------- ACCESSORS -------------------------------------
	// ==================================================================================

	public void setDestination(int dest) {
		mDestination = dest;
	}

	public int getDestination() {
		return mDestination;
	}
}
