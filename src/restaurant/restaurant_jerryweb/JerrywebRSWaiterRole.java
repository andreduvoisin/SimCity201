package restaurant.restaurant_jerryweb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import restaurant.restaurant_jerryweb.JerrywebCookRole.OrderState;
import restaurant.restaurant_jerryweb.gui.Menu;
import restaurant.restaurant_jerryweb.gui.WaiterGui;
import restaurant.restaurant_jerryweb.interfaces.Customer;
import restaurant.restaurant_jerryweb.interfaces.Waiter;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;
import possibleAudio.*;

/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class JerrywebRSWaiterRole extends BaseRole implements Waiter {

	private JerrywebHostRole host;
	private JerrywebCookRole cook;
	private JerrywebCashierRole cashier;
	
	/**
	 * hack to establish connection to Host agent,
	 * and cook agent.
	 */
	public void setHost(JerrywebHostRole myHost) {
		//host = new HostAgent(myHost.getName());
		this.host = myHost;

	}

	public void setCook(JerrywebCookRole myCook) {

		this.cook = myCook;

	}

	static final int semaphoreCerts = 0;


	public List<MyCustomer> Customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public Menu m;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	private Semaphore atTable = new Semaphore(semaphoreCerts,true);
	private Semaphore atCook = new Semaphore(semaphoreCerts, true);
	private Semaphore atCashier = new Semaphore(semaphoreCerts, true);
	private Semaphore atCustomer = new Semaphore(semaphoreCerts, true);
	private Semaphore waitingForOrder = new Semaphore(semaphoreCerts,true);
	private Semaphore servingFood = new Semaphore(semaphoreCerts,true);
	public WaiterGui waiterGui = null;

	public JerrywebRSWaiterRole(Person mPerson) {
		super(mPerson);
		this.name = "jerrywaiterrs";
		//print("I am an rs waiter");
		
	}

	public JerrywebRSWaiterRole(String string) {
		super(null);
		this.name = string;
	}

	public class MyCustomer{
		Customer c;
		CustomerState s;
		int table;
		String choice;

		public MyCustomer(Customer cust, int tableNum, CustomerState state) {
			c = cust;
			table = tableNum;
			s = state;
		}

		public Customer getCustomer(){
			return c;
		}

	}

	public enum CustomerState
	{waiting, seated, readyToOrder, askedToOrder, ordered, waitingForFood, customersFoodReady, bringingCustomerFood, gettingFoodFromCook, eating, 
	sentBillToCashier, waitingForBill, billed, payingBill, leaving}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return Customers;
	}

	// Messages

	
	public void msgPleaseSitCustomer(Customer cust, int tablenum) {
		Customers.add(new MyCustomer(cust, tablenum, CustomerState.waiting));
		//print("Adding " + cust.getName() + " to MyCustomer list. The table for this customer is " + tablenum);
		////print("" + Customers.size());
		stateChanged();
	}

	//Finds the customer on the waiter's list of customers, and sets the customer's state to leaving
	//This also notifies the host that the customer is leaving and that said customer's table is free
	public void msgLeavingTable(Customer c) {
		synchronized(Customers){
		for(int i = 0; i <Customers.size(); i++){
			if(Customers.get(i).c.equals(c)){
				findString("", Customers.get(i).table);
				Customers.get(i).s = CustomerState.leaving;
				//Customers.get(i).s = CustomerState.payingBill;
				//print(Customers.get(i).c.getName() + " is in leaving state");
				host.msgCustLeavingTable(c);
				Customers.remove(i);
				////print("" + Customers.size());
				stateChanged();
			}
		}
		}
	}
	
	public void msgCanGoOnBreak(){
		//print("I am going on Break");
	}	
	public void msgOutOfFood(String choice, int t){
		synchronized(Customers){
		for(int i = 0; i <Customers.size(); i++){
			if(Customers.get(i).table == t){
				Customers.get(i).c.msgOrderNotAvailable(choice);
				Customers.get(i).s = CustomerState.readyToOrder;
				stateChanged();
			}
		}
		}
	}

	public void msgAtTable() {//from animation
		////print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}

	public void msgAtCook() {//from animation
		////print("msgAtCook() called");
		atCook.release();// = true;
		stateChanged();
	}
	
	public void msgAtCashier(){//from animation
		//print("here");
		atCashier.release();
		stateChanged();
		
	}
	
	public void msgAtWaitingCustomer(){
		atCustomer.release();
		stateChanged();
	}
	public void msgServedFood(){
		servingFood.release();
		stateChanged();
	}

	public void msgReadyToOrder(Customer customer){
		int x = find(customer);

		Customers.get(x).s = CustomerState.readyToOrder;
		stateChanged();
	}

	public void msgHereIsMyOrder(Customer customer, String order){
		//atTable.release();
		int x = find(customer);
		Customers.get(x).choice = order;
		Customers.get(x).s = CustomerState.askedToOrder;
		//print("Alright so you would like the " + order);
		waitingForOrder.release();

		stateChanged();
	}

	public void msgOrderReady(String meal, int t){
		findCookingString("", t);
		findPlateString(meal, t);
		
		//print("Ok, coming to get " + meal);
		synchronized(Customers){
			for(int i = 0; i <Customers.size(); i++){
				if(Customers.get(i).table == t){

					Customers.get(i).s = CustomerState.customersFoodReady;
					stateChanged();
				}
			}
		}
	}
	
	public void msgAskForBreak(){
		host.msgWantToGoOnBreak(this);
		stateChanged();
	}

	public void msgTakeFood(String meal, int t){
		//print("Taking " + meal + "to table " + t);
	synchronized(Customers){
		for(int i = 0; i <Customers.size(); i++){
			if(Customers.get(i).table == t){
				Customers.get(i).s = CustomerState.bringingCustomerFood;
				stateChanged();
			}
		}
	}
	}

	public void msgRecievedFood (){
		servingFood.release();
		stateChanged();
	}
	
	public void msgHereIsBill(Customer customer, double check){
		//print("thanks for the bill");
		int x = find(customer);
		Customers.get(x).s = CustomerState.waitingForBill;
		stateChanged();
	}
	
	public void msgDonePaying(Customer customer){
		int x = find(customer);
		Customers.get(x).s = CustomerState.leaving;
		//Customers.remove(x);
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	public boolean pickAndExecuteAnAction() {
		try {
		//synchronized(Customers){
			for(int i =0; i < Customers.size(); i++){
			////print("" + Customers.get(i).s );
				if(Customers.get(i).s == CustomerState.waiting){
					seatCustomer(Customers.get(i));
					return true;
				}
			}
		
		
		//synchronized(Customers){
			for(int i =0; i < Customers.size(); i++){
				if(Customers.get(i).s == CustomerState.readyToOrder){
					takeOrder(Customers.get(i));
					return true;
				}
			}
		//}
		
		//synchronized(Customers){
			for(int i =0; i < Customers.size(); i++){
				if(Customers.get(i).s == CustomerState.customersFoodReady){
					pickUpFood(Customers.get(i));
					return true;
				}
			}
		//}
		
		//synchronized(Customers){
			for(int i =0; i < Customers.size(); i++){
				if(Customers.get(i).s == CustomerState.eating){
					GetBill(Customers.get(i));
					return true;
				}
			}
		//}

		
		waiterGui.GoToIdleSpot();
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
		}
		catch (ConcurrentModificationException e) {
			
			return false;
		}
	}

	// Actions

	private void seatCustomer(MyCustomer customer) {
		waiterGui.waiterString = "";
		if(!waiterGui.getIsAtCustomerQue()){
			waiterGui.DoLeaveCustomer();
			try{
				atTable.acquire();
			}
			catch (InterruptedException e) {

				e.printStackTrace();
			}
			host.msgCustomerSeated(this);
		}
		else{
			DoSeatCustomer(customer);
			customer.c.msgSitAtTable(customer.table, m = new Menu(), this);

			try {
				atTable.acquire();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		customer.s = CustomerState.seated;
		stateChanged();
		//host.msgWantToGoOnBreak(this);
		/*waiterGui.DoLeaveCustomer();
		try{
			atTable.acquire();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}*/
		host.msgCustomerSeated(this);
		}
		
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(MyCustomer customer) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer.c + " at " + customer.table);
		waiterGui.DoBringToTable(customer.c, customer.table); 

	}

	public void takeOrder(MyCustomer customer){

		waiterGui.DoGoToTable(customer.c, customer.table);
		try{
			atTable.acquire();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}
		print("What would you like?");
		customer.c.msgWhatDoYouWant();		
		try {
			waitingForOrder.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//waiterGui.GoToIdleSpot();
		
		sendOrder(customer);

	}

	//Stores the customer's order into the choice variable of customer and then sends the information to the cook
	public void sendOrder(MyCustomer customer){
		customer.s = CustomerState.waitingForFood;
		//cook.msgCookThis(this, customer.choice, customer.table);
		cook.addToRevolvingStand(this, customer.choice, customer.table, OrderState.pending);

		print("putting order on revolving stand" + customer.c.getName() + " wants " + customer.choice);

		waiterGui.DoGoToCook();
		try{
			atCook.acquire();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}
		//findCookingString(customer.choice, customer.table);
	}


	public void pickUpFood(MyCustomer customer){
		waiterGui.DoGoToCook();
		try{
			atCook.acquire();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}
		findPlateString("", customer.table);
		customer.s = CustomerState.gettingFoodFromCook;
		cook.msgGiveMeOrder(customer.table);

		waiterGui.waiterString = customer.choice;
		stateChanged();
		serveCustomer(customer);	
	}



	public void serveCustomer(MyCustomer customer){

		waiterGui.DoGoToTable(customer.c , customer.table);
		try{
			atTable.acquire();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}

		//waiterGui.waiterString = "";
		findString(customer.choice, customer.table);
		//print("Here is your " + customer.choice + ", enjoy!");
		customer.c.msgHereIsYourFood();
		customer.s = CustomerState.eating;
		
			
		//waiterGui.DoLeaveCustomer();
		waiterGui.waiterString = "";
		/*try{
			atTable.acquire();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}*/
		stateChanged();

	}
	
	public void GetBill(MyCustomer customer){
		waiterGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//print("" + cashier.getName() + " please compute check for customer " + customer.c.getName());
		cashier.msgComputeBill(this, customer.c, customer.choice);
		
		waiterGui.DoGoToTable(customer.c, customer.table);
		try{
			atTable.acquire();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		customer.c.msgHereIsCheck();
		customer.s = CustomerState.billed;
		
	}

	//utilities
	public void setCashier(JerrywebCashierRole cashier){
		this.cashier = cashier;
	}
	
	public int find(Customer c){
	//synchronized(Customers){
		for(int i = 0; i <Customers.size(); i++){
			if(Customers.get(i).c.equals(c)){
				return i;
				}
			}
		//}
		//print("Can't find customer in waiter find function!");
		return 0;
	}
	
	//This is used to find the number 
	public void findString(String choice, int tableNumber){
		if(tableNumber == 1){
			waiterGui.table1String = choice;
		}
		if(tableNumber == 2){
			waiterGui.table2String = choice;
		}
		if(tableNumber == 3){
			waiterGui.table3String = choice;
		}
	}
	
	public void findCookingString(String choice, int tableNumber){
		if(tableNumber == 1){
			waiterGui.cookingFood1 = choice;
		}
		if(tableNumber == 2){
			waiterGui.cookingFood2 = choice;
		}
		if(tableNumber == 3){
			waiterGui.cookingFood3 = choice;
		}
	}
	
	public void findPlateString(String choice, int tableNumber){
		if(tableNumber == 1){
			waiterGui.plate1 = choice;
		}
		if(tableNumber == 2){
			waiterGui.plate2 = choice;
		}
		if(tableNumber == 3){
			waiterGui.plate3 = choice;
		}
	}


	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public void addCustomerForTesting(Customer customer, int table){
		Customers.add(new MyCustomer(customer, table, CustomerState.waiting));
	}

	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(2);
	}

	public JerrywebHostRole getHost() {
		return host;
	}


	public void Do(String msg) {
		super.Do(msg, AlertTag.R2);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R2);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R2, e);
	}

}