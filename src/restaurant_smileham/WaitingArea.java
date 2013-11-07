package restaurant_smileham;

import java.awt.Color;

public class WaitingArea {
	public static final Color cWAITINGAREA_COLOR = Color.lightGray;
	public static final int cWAITINGAREA_X = 100;
	public static final int cWAITINGAREA_Y = 50;
	public static final int cWAITINGAREA_WIDTH = 250;
	public static final int cWAITINGAREA_HEIGHT = 60;
	public static final int cWAITINGAREA_BUFFER = 10;
	public static final int cWAITINGAREA_SEATDIM = 30;
	
	public static final int cWAITINGAREA_CAPACITY = 8;
	
	private static int waitingPos = 0;
	private static int numWaiting = 0;
	
	public static void addWaitingCustomer(){
		waitingPos = (waitingPos + 1) % cWAITINGAREA_CAPACITY;
		numWaiting++;
	}
	
	public static void removeWaitingCustomer(){
		numWaiting--;
	}
	
	public static boolean waitingAreaFull(){
		return (numWaiting >= cWAITINGAREA_CAPACITY);
	}
	
	public static int getX(){
		return cWAITINGAREA_X + cWAITINGAREA_SEATDIM*waitingPos + cWAITINGAREA_BUFFER;
	}
	
	public static int getY(){
		return cWAITINGAREA_Y + cWAITINGAREA_BUFFER;
	}
	
	public static int getCustomerPickupX(){
		return cWAITINGAREA_X + cWAITINGAREA_BUFFER;
	}
	
	public static int getCustomerPickupY(){
		return cWAITINGAREA_Y + cWAITINGAREA_HEIGHT - 20;
	}
	
	public static int getWaiterPickupX(){
		return cWAITINGAREA_X + cWAITINGAREA_BUFFER + 20;
	}
	
	public static int getWaiterPickupY(){
		return cWAITINGAREA_Y + cWAITINGAREA_HEIGHT;
	}
}
