package restaurant.restaurant_maggiyan.roles;

import java.util.ArrayList;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import base.BaseRole;
import base.interfaces.Person;
import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.Menu;
import restaurant.restaurant_maggiyan.MyCustomer;
import restaurant.restaurant_maggiyan.MyCustomer.CustomerState;
import restaurant.restaurant_maggiyan.gui.MaggiyanWaiterGui;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCashier;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanHost;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole.WaiterState;


/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the Host. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class MaggiyanWaiterRole extends BaseRole implements MaggiyanWaiter{
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MaggiyanCustomer> waitingCustomers
	= new ArrayList<MaggiyanCustomer>();
	public List<MyCustomer> customers = new ArrayList<MyCustomer>(); 

	private String name;
	public Menu menu = new Menu(); 
	private Semaphore waitingToOrder = new Semaphore(0, true); 
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore goingToKitchen = new Semaphore(0,true); 
	private Semaphore waiterReady = new Semaphore(0, true);
	private Semaphore animationReady = new Semaphore(0, true); 

	public boolean justGotToWork = false;
	public boolean needToGoToKitchen = true; 
	public boolean alreadyAtTable = false; 
	public boolean waiterIsReady = false; 
	public boolean canGoOnBreak = false; 
	
	private MaggiyanCook cook; 
	private MaggiyanHost host; 
	private MaggiyanCashier cashier; 
	public MaggiyanWaiterGui waiterGui = null;
	
	Timer timer = new Timer();
	private int breakTime = 15; 
	
	public enum AgentPos 
	{atStart};
	
	public enum WaiterState {busy, free, askingToGoOnBreak, waitingForBreakResponse, DoneWithBreak}; 
	public WaiterState wState; 
	
	private boolean reenableBreakButton = false; 
	
	public MaggiyanWaiterRole(Person p){
		super(p);
		
		if (p == null) {
			this.name = "NULL Waiter";
		}
		else {
			this.name = p.getName();
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setCook(MaggiyanCook c){
		cook = c; 
	}
	
	public void setHost(MaggiyanHost h){
		host = h; 
	}
	
	public void setCashier(MaggiyanCashier c){
		cashier = c; 
	}
	
	public int getCustomersSize(){
		return customers.size(); 
	}

	public MyCustomer findCustomer(MaggiyanCustomer cust){
		for(int i = 0; i < customers.size(); i++){
			if(customers.get(i).c == cust){
				return customers.get(i); 
			}
		}
		return null;
	}
	
	// Messages
	
	//From Host 
	public void msgPleaseSeatCustomer(MaggiyanCustomer cust, int table){	
		customers.add(new MyCustomer(cust, table, CustomerState.waiting)); 
		print ("Going to seat customer at table: " + table);
		stateChanged(); 
	}
	
	public void msgCantGoOnBreak(){
		print("Sorry you can't go on break");
		reenableBreakButton = true; 
		stateChanged();
	}
	
	public void msgGoOnBreak(){
		print("msgGoOnBreak");
		canGoOnBreak = true; 
		stateChanged(); 
	}
	
	//From Cook 
	
	public void msgOutOfChoice(String choice, int tableNum){
		for(MyCustomer mc: customers){
			if(mc.table == tableNum){
				mc.s = CustomerState.needsToReOrder;
			}
		}
		stateChanged();
	}
	
	//Lets the waiter know food is done
	public void msgOrderDone(String choice, int tableNum, int orderPos){ 
		for(MyCustomer mc: customers){
			if(mc.table == tableNum){
				print("Order picked up"); 
				mc.s = CustomerState.foodOrderReady; 
				mc.orderPos = orderPos; 
			}
		}
		stateChanged(); 
	}
	//From Cashier
	public void msgHereIsBill(Check check){
		print("Received customer check"); 
		MyCustomer mc = findCustomer(check.customer);
		mc.setCheck(check);
		mc.s = CustomerState.checkReady; 
		stateChanged();
	}
	
	//For JUnit Testing 
	public double getCheckTotal(Check c){
		MyCustomer mc = findCustomer(c.customer);
		return mc.getTotal(c); 
	}
	
	//From Customer
	public void msgReadyToBeSeated(){
		animationReady.release();
		stateChanged(); 
	}
	
	public void msgReadyToOrder(MaggiyanCustomer cust){
		MyCustomer mc = findCustomer(cust); 
		mc.s = CustomerState.readyToOrder; 
		stateChanged(); 
	}
	
	public void msgHereIsMyOrder(String choice, MaggiyanCustomer c){
		MyCustomer mc = findCustomer(c); 
		mc.s = CustomerState.gaveOrder; 
		mc.choice = choice; 
		waitingToOrder.release(); 
		stateChanged(); 
	}
	
	public void msgLeavingTable(MaggiyanCustomer cust) {
		MyCustomer mc = findCustomer(cust); 
		mc.s = CustomerState.done; 
	
		stateChanged();
	}
	
	//From Animation 
	
	public void atWork(){
		justGotToWork = true; 
		stateChanged();
	}
	
	public void msgAnimationReady(){
		print("does release"); 
		animationReady.release();
		stateChanged(); 
	}
	
	public void msgAskToGoOnBreak(){
		print("msgAskToGoOnBreak");
		wState = WaiterState.askingToGoOnBreak;
		stateChanged(); 
	}
	
	public void msgBackFromBreak(){
		wState = WaiterState.DoneWithBreak; 
		stateChanged(); 
	}
	
	public void msgReachedKitchen(){
		if(needToGoToKitchen){
			goingToKitchen.release();
			needToGoToKitchen = false; 
		} 
		stateChanged(); 
	}
	
	public void msgAtTable() {
		if(alreadyAtTable){
			atTable.release();
			print("At Table release"); 
			alreadyAtTable = false; 
		}
		stateChanged();
	}
	
	public void msgWaiterFree(){
		if(waiterIsReady){
			waiterReady.release(); 
			print("waiter ready release");
			waiterIsReady = false; 
		} 
		stateChanged(); 
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try{
			if(justGotToWork){
				justGotToWork = false;
				startWork();
				return true;
			}
			
			if(reenableBreakButton){
				keepWorking(); 
				return true; 
			}
		
			if (!customers.isEmpty()) { 
					for(int i=0; i<customers.size(); i++){
						if(customers.get(i).s == CustomerState.waiting){
							seatCustomer(customers.get(i)); 
							return true; 
						}
					}
					for(int i=0; i<customers.size(); i++){
						if(customers.get(i).s == CustomerState.readyToOrder){
							takeOrder(customers.get(i)); 
							return true; 
						}
					}
					for(int i=0; i<customers.size(); i++){
						if(customers.get(i).s == CustomerState.orderGiven){
							giveOrderToCook(customers.get(i)); 
							return true; 
						}
					}
					for(int i=0; i<customers.size(); i++){
						if(customers.get(i).s == CustomerState.needsToReOrder){
							askCustomerToReorder(customers.get(i)); 
							return true; 
						}
					}
					for(int i=0; i<customers.size(); i++){
						if(customers.get(i).s == CustomerState.foodOrderReady){
							giveCustomerFood(customers.get(i)); 
							return true; 
						}
					}
					for(int i=0; i<customers.size(); i++){
						if(customers.get(i).s == CustomerState.checkReady){
							giveCustomerCheck(customers.get(i)); 
							return true; 
						}
					}
					for(int i=0; i<customers.size(); i++){
						if(customers.get(i).s == CustomerState.done){
							clearTable(customers.get(i));  
							return true; 
						}
					}
			}
			
			if(wState == WaiterState.askingToGoOnBreak){
				askToGoOnBreak();
			}
			
			if(customers.isEmpty()){
				if(canGoOnBreak){
					goOnBreak(); 
					return true; 
				}
			}
			waiterGui.DoLeaveCustomer();
			host.msgWaiterFree(this);
			return false;	
		}catch(ConcurrentModificationException e){
			return true; 
		}
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void askCustomerToReorder(MyCustomer customer){
		print("Asking customer to reorder");
		customer.s = CustomerState.reordering; 
		customer.c.msgOutOfYourChoice();
	}
	
	private void startWork(){
		try{
			animationReady.acquire(); 
		}
		catch(Exception e){
			print("Start Work exception error caught"); 
		}
		host.msgIAmHere(this);
	}
	
	private void askToGoOnBreak(){
		print("Can I go on break?");
		host.msgCanIGoOnBreak(this);
		wState = WaiterState.waitingForBreakResponse;
	}
	
	private void goOnBreak(){
		print("Going on break");
		final restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter w = this; 
		waiterGui.DoGoOnBreak();
		try{
			animationReady.acquire(); 
		}
		catch(Exception e){
			print("goOnBreak exception");
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done with break");
				host.msgDoneWithBreak(w); 
				waiterGui.DoGoOffBreak();
				canGoOnBreak = false;
				stateChanged();
			}
		},
		breakTime * 1000); 
	}
	
	private void keepWorking(){
		reenableBreakButton = false;
		host.msgDoneWithBreak(this); 
		waiterGui.DoGoOffBreak();
//		waiterGui.DoEnableBreakBox(); 
	}
	
	private void seatCustomer(MyCustomer myCust) {
		host.msgWaiterBusy(this); 
		print("Seating Customer at table " + myCust.table); 
		waiterGui.DoGoToSeatCustomer(); 
		try{
			animationReady.acquire(); 
		}
		catch(Exception e){
			print("Animation release exception");
		} 
		myCust.c.msgFollowMe(this, menu, myCust.table);
		try{
			animationReady.acquire(); 
		}
		catch(Exception e){
			print("Animation release exception");
		} 
		DoSeatCustomer(myCust.c, myCust.table);
		myCust.s = CustomerState.seated; 
		alreadyAtTable = true; 
		try{ 
			print("At Table acquire"); 
			atTable.acquire(); 
		}
		catch(Exception e){
			print("giveCustomerFood exception");
		}
		waiterGui.DoLeaveCustomer();
		//Leave customer while customer orders
		try{
			animationReady.acquire();
		}
		catch(Exception e){
			print("Animation release exception");
		} 
		host.msgWaiterFree(this);
//		}
	}
	
	private void takeOrder(MyCustomer cust){
		host.msgWaiterBusy(this);
		alreadyAtTable = true; 
		DoTakeOrder(cust); 
		try{ 
			print("At Table acquire"); 
			atTable.acquire(); 
		}
		catch(Exception e){
			print("giveCustomerFood exception");
		}
		alreadyAtTable = true; 
		cust.c.msgWhatDoYouWant();
		cust.s = CustomerState.askedToOrder; 
		try{
			waitingToOrder.acquire(); 
		}
		catch(Exception e){
			print("takeOrder exception"); 
		}
		//waiterGui.showCustomerChoice(cust.choice);
		cust.s = CustomerState.orderGiven; 
		print("Order Taken");
		
	}
	
	private void giveOrderToCook(MyCustomer cust){
		host.msgWaiterBusy(this); 
		DoGiveOrderToCook();  
		print("Giving order to cook");
		cust.s = CustomerState.foodIsCooking; 
//		try{
//			animationReady.acquire();
//		}
//		catch(Exception e){
//			print("Animation release exception");
//		} 
		try{
			goingToKitchen.acquire();
			needToGoToKitchen = true; 
		}
		catch(Exception e){
			print ("giveOrderToCook exception");
		}
		cook.msgHereIsOrder(this, cust.choice, cust.table); 
	}
	
	private void giveCustomerFood(MyCustomer mc){
		host.msgWaiterBusy(this); 
		waiterGui.DoGoToCook();
		print("Getting customer food");
		
		try{
			goingToKitchen.acquire();
			needToGoToKitchen = true; 
		}
		catch(Exception e){
			print ("giveOrderToCook exception");
		}
		cook.msgPickedUpOrder(mc.orderPos); 
		waiterGui.showCustomerOrder(mc.choice);
		print("Bringing food to customer"); 
		DoBringCustomerFood(mc.table);
		alreadyAtTable = true; 
		try{ 
			atTable.acquire(); 
		}
		catch(Exception e){
			print("giveCustomerFood exception");
		}
		waiterGui.hideCustomerChoice();
		mc.c.msgHereIsYourFood();
		cashier.msgPleaseCalculateBill(this, mc.c, mc.choice); 
		mc.s = CustomerState.eating;


	}
	
	private void giveCustomerCheck(MyCustomer mc){
		waiterGui.DoBringToTable(mc.table);
		mc.c.msgHereIsCheck(mc.check); 
		mc.s = CustomerState.receivedCheck; 
	}
	
	private void clearTable(MyCustomer cust){
		host.msgTableFree(cust.table, this); 
		customers.remove(cust); 
		
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(MaggiyanCustomer customer, int table) {
		print("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(table); 

	}
	
	private void DoTakeOrder(MyCustomer customer){
		waiterGui.DoBringToTable(customer.table); 
	}
	
	private void DoGiveOrderToCook(){
		waiterGui.DoGoToCook(); 
		
	}
	
	private void DoBringCustomerFood(int table){
		waiterGui.DoBringToTable(table);
	}
	

	//utilities

	public void setGui(MaggiyanWaiterGui gui) {
		waiterGui = gui;
	}

	public MaggiyanWaiterGui getGui() {
		return waiterGui;
	}
	
}

