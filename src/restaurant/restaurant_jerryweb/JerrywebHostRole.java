package restaurant.restaurant_jerryweb;

import restaurant.restaurant_jerryweb.agent.Agent;
import restaurant.restaurant_jerryweb.JerrywebWaiterRole.CustomerState;
import restaurant.restaurant_jerryweb.gui.HostGui;
import restaurant.restaurant_jerryweb.interfaces.Customer;
import restaurant.restaurant_jerryweb.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Role;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class JerrywebHostRole extends BaseRole {
	

	
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement Customers using ArrayList, but type it
	//with List semantics.
	static final int semaphoreCerts = 0;
	//public List<Customer> Customers= new ArrayList<Customer>();
	public List<MyCustomer> theCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	//public List<MyCustomer> 
	//Allows the host to keep track of all of the waiters in the 
	public List<MyWaiter> Waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	int selection = 0;
	
	
	/*In order to distribute customers across the waiters, I created a MyWaiter class, which is modeled off of the MyCustomers class.
	 *The MyWaiters class only holds the two variables needed to discern which waiter is available to seat customers. As long as the waiter
	 *is not already seating customers, he/she will be in the 'free' state, and available to seat any addtional customers. 
	 */
	public class MyWaiter{
		
		Waiter w;
		WaiterState ws;
		
		public MyWaiter(Waiter waiter, WaiterState state) {
			w = waiter;
			ws = state;		
		}
		
		public Waiter getWaiter(){
			return w;
		}


	}

	public enum WaiterState
	{free, seatingCustomer, wantToGoOnBreak, onBreak}

	public class MyCustomer{
		
		Customer c;
		CustomerState s;
		
		public MyCustomer(Customer customer, CustomerState state) {
			c = customer;
			s = state;		
		}


	}

	public enum CustomerState
	{waiting, seated, doneEating}
	
	private String name;
	private Semaphore atTable = new Semaphore(semaphoreCerts,true);

	public HostGui hostGui = null;

	public JerrywebHostRole(String name) {
		super(null);
		
		this.name = "jerryhost";
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void addWaiter(Waiter w){
		Waiters.add(new MyWaiter(w, WaiterState.free));
//		print("Added waiter " + w.getName());
//		print("" + Waiters.size());
		stateChanged();
	}

	public Collection getTables() {
		return tables;
	}
	// Messages

	public void msgIWantFood(Customer customer) {
		//Customers.add(customer);
		theCustomers.add(new MyCustomer(customer, CustomerState.waiting) );
		int x = CustomerQue(customer);
		customer.msgWaitInQue(x);
		stateChanged();
	}
	
	public void msgWantToGoOnBreak(Waiter Waiter){
		int x = find(Waiter);
		print("Waiter " + Waiters.get(x).w.getName() + " wants to go on break");
		Waiters.get(x).ws = WaiterState.wantToGoOnBreak;
		stateChanged();
	}

	
	public void msgBackFromBreak(Waiter waiter){
		int x = find(waiter);
		Waiters.get(x).ws = WaiterState.free;
		stateChanged();
	}
	
	
	
	public void msgCustLeavingTable(Customer customer) {
		synchronized(tables){
		for (Table table : tables) {
			if (table.getOccupant() == customer) {
				print(customer + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
		}
	}
	
	public void msgCustomerSeated(Waiter waiter){
		synchronized(Waiters){	
		for(int i = 0; i < Waiters.size(); i++){
			if(Waiters.get(i).w.equals(waiter)){
				Waiters.get(i).ws = WaiterState.free;
				
				stateChanged();
			}
		}
		}
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

		try{
		for(int j = 0; j < Waiters.size(); j++){
			//print("" +Waiters.get(j).ws);
			if(Waiters.get(j).ws == WaiterState.wantToGoOnBreak){
				answerWaiter(Waiters.get(j));			
	
				return true;
			}
		}
		
		for (Table table : tables) {
			
			if (!table.isOccupied()) {
				for(int i = 0; i < theCustomers.size(); i++){
					if(theCustomers.get(i).s == CustomerState.waiting){
						
						AssignWaiter(theCustomers.get(i), table);//the action
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}	
				
			}
		}	
		

		return false;
		}
		
		catch(ConcurrentModificationException e){
			return false;
		}
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	/*This is where the host will distribute the customers evenly amongst the waiters. Selection is an integer in that begins at one, and is incremented 
	 * everytime a customer is assigned. The actual value of selection will determine which waiter is selected by picking th location of the waiter 
	 * within the waiter list that matches the selection - 1 value. When selection reaches the maxium size of the waiter list, it will reset to one and the process will repeat.
	 */
	public void answerWaiter(MyWaiter waiter){
		if(Waiters.size() >1){
			print("" + waiter.w.getName() + " can go on break.");
			waiter.w.msgCanGoOnBreak();
			waiter.ws = WaiterState.onBreak;
			stateChanged();
		}
		else{
			print("" + waiter.w.getName() + " cannot go on break.");
			waiter.ws = WaiterState.free;
			stateChanged();
		}
		
	}
	
	private void AssignWaiter(MyCustomer customer, Table table) {
		
		if(Waiters.size() == 0){
				//print("There are no waiters here... Where are my waiters?!?! I hate my job.");
			return;
		}
		else {
			if(Waiters.get(selection).ws == WaiterState.onBreak){
				//AssignWaiter(customer, table);
			}
			else{
			Waiters.get(selection).w.msgPleaseSitCustomer(customer.c, table.tableNumber);
			Waiters.get(selection).ws  = WaiterState.onBreak;
			customer.s = CustomerState.seated;
			table.setOccupant(customer.c);
			synchronized(theCustomers){
			for(int j = 0; j <theCustomers.size(); j++){
				if(theCustomers.get(j).c.equals(customer.c)){
					theCustomers.remove(j);
				}
			}}
			}
			selection++;
			if(selection == Waiters.size()){
				selection = 0;
			}
			
			
		}
		
		/*
		for(int i = 0; i < Waiters.size(); i++){
			if(Waiters.get(i).ws == WaiterState.free){
				print("" +	Waiters.get(i).w.getName() + " please seat customer.");
				Waiters.get(i).w.msgPleaseSitCustomer(customer.c, table.tableNumber);
				customer.s = CustomerState.seated;
				table.setOccupant(customer.c);
				
				Waiters.get(i).ws = WaiterState.seatingCustomer;
				for(int j = 0; j <theCustomers.size(); j++){
					if(theCustomers.get(j).c.equals(customer.c)){
						theCustomers.remove(j);
					}
				}

				
				return;
			}
		}*/

	}
	
	public void pausePrint(){
		print("called");
	}
	// The animation DoXYZ() routines

	//utilities

	public int find(Waiter Waiter){
		synchronized(Waiters){
		for(int i = 0; i <Waiters.size(); i++){
			if(Waiters.get(i).w.equals(Waiter)){
				return i;
			}
		}
		}
		print("Can't find waiter in host find function!");
		return 0;
	}
	

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	public int CustomerQue(Customer customer){
		if(theCustomers.size() == 0){
			return 0;
		}
		else{
			return theCustomers.size() - 1;
		}
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
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(2);
	}
}

