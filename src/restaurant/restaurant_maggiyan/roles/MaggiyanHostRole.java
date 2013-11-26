package restaurant.restaurant_maggiyan.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import base.BaseRole;
import base.interfaces.Person;
import restaurant.restaurant_maggiyan.gui.MaggiyanWaiterGui;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanHost;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class MaggiyanHostRole extends BaseRole implements MaggiyanHost{
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MaggiyanCustomer> waitingCustomers
	= new ArrayList<MaggiyanCustomer>();
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public List<MyWaiter> waiters = new ArrayList<MyWaiter>(); 
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	public boolean isWaiter = true; 
	public boolean amReady = true; 
	public enum WaiterState {busy, free, askedToGoOnBreak, onBreak};
	private MaggiyanWaiterGui hostGui = null;
	private MaggiyanCookRole cook; 
	private int minWaiters = 1; 
	private int minCustomer = Integer.MAX_VALUE; 
	private MyWaiter leastBusyWaiter = null; 
	private int occupiedTableCounter = 0; 
	
	private boolean askedToGoOnBreak = false; 
	
	public void addWaiter(MaggiyanWaiter waiter){
		MyWaiter w = new MyWaiter(waiter); 
		waiters.add(w); 
	}
	
	public enum AgentPos 
	{atStart}; 
	
	public MaggiyanHostRole(Person p) {
		super(p); 
		this.name = p.getName();
		
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix)); //how you add to a collections
		}
	}
	
	public MaggiyanHostRole(String n){
		
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public MyWaiter findMyWaiter(MaggiyanWaiter w){
		for(MyWaiter waiter: waiters){
			if(waiter.w.getName() == w.getName()){
				return waiter; 
			}
		}
		return null; 
	}
	
//	public MyWaiter findLeastBusyWaiter(){
//		for(MyWaiter waiter: waiters){
////			if(waiter.onBreak = true){
////				//Skip waiter
////			}
//			if(waiter.w.getCustomersSize() < minCustomer){
//				leastBusyWaiter = waiter; 
//				minCustomer = waiter.w.getCustomersSize();
//			}
//		}
//		return leastBusyWaiter;
//	}
	
	// Messages
	
	//From Customer 
	public void msgIWantFood(MaggiyanCustomer cust) {
		print("Hello Customer"); 
		waitingCustomers.add(cust);
		stateChanged();
	}
	
	public void msgLeaving(MaggiyanCustomer customer){
		waitingCustomers.remove(customer); 
		stateChanged(); 
	}
	//From Waiter
	public void msgIAmHere(MaggiyanWaiter waiter){
		print("Hello " + waiter.getName());
		addWaiter(waiter); 
		stateChanged(); 
	}
	
	public void msgWaiterFree(MaggiyanWaiter w){
		for(MyWaiter waiter:waiters){
			if(waiter.w == w){
				waiter.s = WaiterState.free; 
			}
		}
		stateChanged();
	}
	
	public void msgWaiterBusy(MaggiyanWaiter w){
		for(MyWaiter waiter:waiters){
			if(waiter.w == w){
				waiter.s = WaiterState.busy; 
			}
		}
		stateChanged();
	}
	
	
	public void msgCanIGoOnBreak(MaggiyanWaiter w){
		print("msgCanIGoOnBreak");
		for(MyWaiter waiter:waiters){
			if(waiter.w.equals(w)){
				waiter.askedToGoOnBreak = true;
				//askedToGoOnBreak = true;  
			}
		}
		stateChanged(); 
	}
	
	public void msgDoneWithBreak(MaggiyanWaiter w){
		findMyWaiter(w).onBreak = false;
		stateChanged();
	}
	
	public void msgTableFree(int tableNum, MaggiyanWaiter w){
		for (Table table : tables) {
			if(table.tableNumber == tableNum){
				print("Table " + tableNum + " is free");
				table.setUnoccupied();
				stateChanged();
			}
		}
		stateChanged();
	}
	
	public void msgAtTable() {//from animation
		print("msgAtTable() called");
		amReady = false;
		atTable.release(); 
		stateChanged();
	}
	
	public void msgReady(){
		print("msgReady");
		amReady = true;
		stateChanged(); 
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		try{
			if(!waiters.isEmpty()){
				for(MyWaiter waiter: waiters){
					if(waiter.s != WaiterState.busy){
						if(!waitingCustomers.isEmpty()){
							for(MaggiyanCustomer customer: waitingCustomers){
								for(Table table: tables){
									if(!table.isOccupied()){
										if(!waiter.onBreak){
											callWaiter(waiter, customer, table);
											minCustomer++;
											return true;
										}
									}
								}
								for(Table table: tables){
									if(table.isOccupied()){
										occupiedTableCounter++;
									}
								}
								if(occupiedTableCounter == tables.size()){
									NotifyCustomer(customer);  
									return true; 
								}
							}
							
						}
						if(waiter.askedToGoOnBreak){
							handleWaiterBreaks(waiter);
							waiter.askedToGoOnBreak = false;
							return true; 
						}
					}
				}
			}
		
		
			return false;
		}catch(ConcurrentModificationException e){
			return true; 
		}
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void NotifyCustomer(MaggiyanCustomer customer){
		print("Restaurant is full, please wait"); 
		customer.msgRestaurantFull(); 
	}
	
	private void removeImpatientCustomer(){
		for(MaggiyanCustomer c: waitingCustomers){
			if(c.getName().equals("Impatient")){
				waitingCustomers.remove(c); 
			}
		}
	}
	
	private void handleWaiterBreaks(MyWaiter waiter){
		print("handle breaks");
		if(waiters.size() > minWaiters){
			waiter.w.msgGoOnBreak(); 
			waiter.onBreak = true; 
			waiter.s = WaiterState.onBreak;  
		}
		else{
			waiter.w.msgCantGoOnBreak();
			//waiter.s = WaiterState.busy; 
		}
	}
	
	private void callWaiter(MyWaiter waiter, MaggiyanCustomer cust, Table table){
		print("Calling Waiter");
		table.setOccupant(cust);
		waitingCustomers.remove(0);
		waiter.w.msgPleaseSeatCustomer(cust, table.tableNumber);
		System.out.println("add cust table " + table.tableNumber);
		waiter.s = WaiterState.busy; 
		
	}

	//Utilities
	public void print(String msg){
		System.out.println("[" + name + "]: " + msg );
	}

	public void setGui(MaggiyanWaiterGui gui) {
		hostGui = gui;
	}

	public MaggiyanWaiterGui getGui() {
		return hostGui;
	}

	private class Table {
		MaggiyanCustomer occupiedBy;
		int tableNumber; 

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(MaggiyanCustomer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		MaggiyanCustomer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
	}
	private class MyWaiter{
		MyWaiter(MaggiyanWaiter waiter){
			w = waiter; 
			s = WaiterState.free; 
			customerNum = 0; 
			askedToGoOnBreak = false; 
			onBreak = false;
			
		}
		
		MaggiyanWaiter w;  
		WaiterState s; 
		boolean askedToGoOnBreak;
		boolean onBreak; 
		int customerNum; 
	}
}

