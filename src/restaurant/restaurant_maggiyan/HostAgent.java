package restaurant_maggiyan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import restaurant_maggiyan.gui.WaiterGui;
import restaurant_maggiyan.interfaces.Customer;
import restaurant_maggiyan.interfaces.Host;
import restaurant_maggiyan.interfaces.Waiter;
import agent.Agent;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent implements Host{
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<Customer> waitingCustomers
	= new ArrayList<Customer>();
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public List<MyWaiter> waiters = new ArrayList<MyWaiter>(); 
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	public boolean isWaiter = true; 
	public boolean amReady = true; 
	public enum WaiterState {busy, free, askedToGoOnBreak, onBreak};
	private WaiterGui hostGui = null;
	private CookAgent cook; 
	private int minWaiters = 1; 
	private int minCustomer = Integer.MAX_VALUE; 
	private MyWaiter leastBusyWaiter = null; 
	private int occupiedTableCounter = 0; 
	
	private boolean askedToGoOnBreak = false; 
	
	public void addWaiter(Waiter waiter){
		MyWaiter w = new MyWaiter(waiter); 
		waiters.add(w); 
	}
	
	public enum AgentPos 
	{atStart}; 
	
	public HostAgent(String name) {
		super(); 
		this.name = name;
		
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix)); //how you add to a collections
		}
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public MyWaiter findMyWaiter(Waiter w){
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
	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}
	
	public void msgLeaving(Customer customer){
		waitingCustomers.remove(customer); 
		stateChanged(); 
	}
	//From Waiter
	public void msgIAmHere(Waiter waiter){
		print("I am here");
		addWaiter(waiter); 
		stateChanged(); 
	}
	
	public void msgWaiterFree(Waiter w){
		for(MyWaiter waiter:waiters){
			if(waiter.w == w){
				waiter.s = WaiterState.free; 
			}
		}
		stateChanged();
	}
	
	public void msgWaiterBusy(Waiter w){
		for(MyWaiter waiter:waiters){
			if(waiter.w == w){
				waiter.s = WaiterState.busy; 
			}
		}
		stateChanged();
	}
	
	
	public void msgCanIGoOnBreak(Waiter w){
		print("msgCanIGoOnBreak");
		for(MyWaiter waiter:waiters){
			if(waiter.w.equals(w)){
				waiter.askedToGoOnBreak = true;
				//askedToGoOnBreak = true;  
			}
		}
		stateChanged(); 
	}
	
	public void msgDoneWithBreak(Waiter w){
		findMyWaiter(w).onBreak = false;
		stateChanged();
	}
	
	public void msgTableFree(int tableNum, Waiter w){
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
	protected boolean pickAndExecuteAnAction() {
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
							for(Customer customer: waitingCustomers){
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
	private void NotifyCustomer(Customer customer){
		print("Restaurant is full, please wait"); 
		customer.msgRestaurantFull(); 
	}
	
	private void removeImpatientCustomer(){
		for(Customer c: waitingCustomers){
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
	
	private void callWaiter(MyWaiter waiter, Customer cust, Table table){
		print("Calling Waiter");
		table.setOccupant(cust);
		waitingCustomers.remove(0);
		waiter.w.msgPleaseSeatCustomer(cust, table.tableNumber);
		System.out.println("add cust table " + table.tableNumber);
		waiter.s = WaiterState.busy; 
		
	}

	//utilities

	public void setGui(WaiterGui gui) {
		hostGui = gui;
	}

	public WaiterGui getGui() {
		return hostGui;
	}

	private class Table {
		Customer occupiedBy;
		int tableNumber; 

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
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
		MyWaiter(Waiter waiter){
			w = waiter; 
			s = WaiterState.free; 
			customerNum = 0; 
			askedToGoOnBreak = false; 
			onBreak = false;
			
		}
		
		Waiter w;  
		WaiterState s; 
		boolean askedToGoOnBreak;
		boolean onBreak; 
		int customerNum; 
	}
}

