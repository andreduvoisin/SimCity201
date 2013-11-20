package restaurant_xurex;

import base.Agent;

import java.util.*;

//import java.util.concurrent.Semaphore;
import restaurant_xurex.interfaces.Customer;
import restaurant_xurex.interfaces.Host;
import restaurant_xurex.interfaces.Waiter;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent implements Host {
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> customers
	= new ArrayList<MyCustomer>();
	public List<Waiter> waiters
	= new ArrayList<Waiter>();
	public Collection<Table> tables;

	private enum CustomerState
	{informed, waiting, informedWaiting, ignore};
	
	private class MyCustomer{
		Customer customer;
		CustomerState state;
		MyCustomer(Customer customer){
			this.customer = customer;
		}
	}

	private String name;
	private boolean fullRestaurant = true;

	public HostAgent(String name) {
		super();

		this.name = name;
		tables = new ArrayList<Table>(NTABLES);
		tables.add(new Table(1));//nw
		tables.add(new Table(2));//ne
		tables.add(new Table(3));//sw 
		tables.add(new Table(4));//se
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	// MESSAGES //

	/* (non-Javadoc)
	 * @see restaurant.Host#IWantFood(restaurant.CustomerAgent)
	 */
	@Override
	public void IWantFood(Customer c) {
		MyCustomer customer = new MyCustomer(c);
		customer.state = CustomerState.waiting;
		customers.add(customer);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#IWillWait(restaurant.CustomerAgent)
	 */
	@Override
	public void IWillWait(Customer c){
		for(MyCustomer customer : customers){
			if(customer.customer == c){
				customer.state = CustomerState.informedWaiting;
			}
		}
		Do("I Will Wait signal received");
		stateChanged();
	}
	public void IWillNotWait(Customer c){
		for(MyCustomer customer : customers){
			if(customer.customer == c){
				Do("CUSTOMER "+c.getName()+"IS NOT WAITING");
				customer.state = CustomerState.ignore;
				return;
			}
		}
		//stateChanged() unneeded
	}
	public void IWantBreak(Waiter w) {
		int availableWaiters = 0;
		for (int i = 0; i < waiters.size(); i++) {
			if(waiters.get(i).isAvailable())
				availableWaiters++;
		}
		if(availableWaiters>1)
			w.okForBreak();
		else
			w.noBreak();
	}
	public void IAmFree() {
		stateChanged();
	}
	public void TableIsFree(int t) {
		for (Table table : tables) {
			if (table.tableNumber == t) {
				table.setUnoccupied();
				stateChanged();
			}
		}
	}
	public void addWaiter(Waiter w){
		waiters.add(w); stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(customers.isEmpty()){
			return false;
		}
		fullRestaurant = true;
		for (Table table : tables) {
			if (!table.isOccupied()){
				fullRestaurant = false;
			}
		}
		if(fullRestaurant){
			informCustomers();
		}
		else{
		for (Table table : tables) {
			if (!table.isOccupied()) {
				for(Waiter waiter:waiters){
					if(waiter.isGood()){
						for(MyCustomer mc : customers){
							if(mc.state == CustomerState.waiting || mc.state == CustomerState.informedWaiting){
								seatCustomer(mc, waiter, table.tableNumber);
								table.setOccupied();
								return true;
							}
						}
					}
				}
			}
		}
		}
		return false;
	}

	// ACTIONS //

	private void seatCustomer(MyCustomer customer, Waiter waiter, int table) {
		waiter.PleaseSeatCustomer(customer.customer, table);
		customers.remove(customer);
	}
	private void informCustomers(){
		for(MyCustomer customer : customers){
			if(customer.state == CustomerState.waiting){
				customer.state = CustomerState.informed;
				customer.customer.FullRestaurant();
				Do("Messaged customer about full restaurant");
			}
		}
	}

	// UTILITIES //
	
	public class Table {
		boolean occupied;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupied() {
			occupied = true;
		}

		void setUnoccupied() {
			occupied = false;
		}

		boolean isOccupied() {
			return occupied;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
		int getTableNumber(){
			return tableNumber;
		}
		
	}

	public int getWaiterNumber() {
		return waiters.size();
	}
}

