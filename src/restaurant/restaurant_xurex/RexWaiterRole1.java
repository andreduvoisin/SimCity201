package restaurant.restaurant_xurex;

import base.BaseRole;
import base.interfaces.Person;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import restaurant.restaurant_xurex.gui.WaiterGui;
import restaurant.restaurant_xurex.interfaces.Cashier;
import restaurant.restaurant_xurex.interfaces.Cook;
import restaurant.restaurant_xurex.interfaces.Customer;
import restaurant.restaurant_xurex.interfaces.Host;
import restaurant.restaurant_xurex.interfaces.Waiter;
import restaurant.restaurant_xurex.interfaces.WaiterGui_;
import restaurant.restaurant_xurex.utilities.*;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */
//We have 4 types of agents in V2 (Cook, Customer, Host, Waiter)
//Waiter Agent seats customers, takes orders, and sends them to the cooks.
//Waiter Agent also serves food once notified by cook that order is ready.


public class RexWaiterRole1 extends BaseRole implements Waiter{
	private Semaphore atLocation = new Semaphore(0,true);
	private String name;
	private int number = -1;
	private static final int breakDuration = 10000;
	List<MyCustomer> customers = new ArrayList<MyCustomer>();
	List<Order> orders = new ArrayList<Order>();
	Cook cook;
	Host host;
	Cashier cashier;
	Timer breakTime = new Timer();
	
	static Map<String, Integer> menu = new HashMap<String, Integer>();
	
	WaiterState state = WaiterState.good;
	
	private WaiterGui_ waiterGui = null;
	
	private void initializeMenu(){
		menu.put("Steak", new Integer(16));
		menu.put("Chicken", new Integer(11));
		menu.put("Salad", new Integer(6));
		menu.put("Pizza", new Integer(9));
	}
	public RexWaiterRole1(){
		super();
		initializeMenu();
	}

	public RexWaiterRole1(String name, Person person) {
		super(person);
		this.name = name;
		initializeMenu();
	}
	public RexWaiterRole1(String name, Host host, Cook cook, Person person){
		super(person);
		this.name = name;
		this.cook = cook;
		this.host = host;
		initializeMenu();
	}
	public RexWaiterRole1(RexAnimationPanel animationPanel){
		super();
		WaiterGui gui = new WaiterGui(this, animationPanel);
		gui.setRole(this);
		this.setGui(gui);
		animationPanel.addGui(gui);
	}
	public String getName() {
		return name;
	}

	//CUSTOMER MESSAGES
	public void ReadyToOrder(Customer c){
		for(MyCustomer customer:customers){
			if(customer.c.getName().equals(c.getName())){
				customer.s=CustomerState.readyToOrder;
			}
		}
		Do("ReadyToOrder received");
		stateChanged();
	}
	public void HereIsChoice(Customer c, String choice){
		for(MyCustomer customer:customers){
			if(customer.c.getName()==c.getName()){
				customer.choice=choice;
				customer.s=CustomerState.ordered;
			}
		}
		Do("HereIsChoice received");
		stateChanged();
	}
	public void Leaving(Customer c){
		for(MyCustomer customer:customers){
			if(customer.c.getName()==c.getName()){
				customer.s=CustomerState.done;
			}
		}
		Do("Customer has left, going to message host.");
		stateChanged();
	}
	//HOST MESSAGES
	public void PleaseSeatCustomer(Customer c, int table){
		customers.add(new MyCustomer(c,table));
		Do("Seating customer "+c.getName()+" at "+table);
		state = WaiterState.working;
		stateChanged();
	}
	public void okForBreak(){
		state=WaiterState.okForBreak;
		Do("I'm ready to take my break");
		stateChanged();
	}
	public void noBreak(){
		state=WaiterState.good;
		Do("Waiter "+this.getName()+" can't go on break");
		stateChanged();
		breakTime.schedule(new TimerTask(){
			public void run(){
				waiterGui.setWaiterEnabled(name);
			}
		}, 1000);
	}
	//COOK MESSAGES
	public void OrderIsReady(String choice, int table, int kitchen){
		for(Order order:orders){
			if(order.choice==choice&&order.table==table){
				order.kitchen = kitchen;
				order.s=OrderState.readyToServe;
			}
		}
		Do("OrderIsReady received");
		stateChanged();
	}
	public void OutOfFood(int table, String choice){
		Do("Out of Food received");
		for(Order o: orders){
			if(o.table==table){
				o.s=OrderState.denied;
			}
		}
		menu.remove(choice);
		stateChanged();
	}
	//CASHIER MESSAGES
	public void HereIsBill(Customer customer, float bill){
		for(MyCustomer mc : customers){
			if(mc.c.getName().equals(customer.getName())){
				mc.bill = bill;
			}
		}
		stateChanged();
	}
	
	//TIMER MESSAGES
	private void TimerDone(){
		waiterGui.setWaiterEnabled();
		Do("Timer Done");
	}
	public void msgAtLocation() {//from animation
		atLocation.release();// = true;
		stateChanged();
	}
	public void wantBreak(){
		state = WaiterState.wantBreak;
		host.IWantBreak(this);
	}
	public void backToWork(){
		state = WaiterState.good; host.IAmFree();
		stateChanged();
	}
	
	/**
	 * Scheduler: the brains of the operation
	 */
	public boolean pickAndExecuteAnAction() {
		for(MyCustomer customer:customers){
			if(customer.s==CustomerState.askedToOrder){
				StayStill(); return true;
			}
		}
		for(MyCustomer customer:customers){
			if(customer.s==CustomerState.waiting){
				SeatCustomer(customer); state=WaiterState.working; return true;
			}
		}
		for(Order order:orders){
			if(order.s==OrderState.denied){
				AskToReorder(order); state=WaiterState.working; return true;
			}
		}
		for(Order order:orders){
			if(order.s==OrderState.readyToServe){
				ServeFood(order); order.s=OrderState.served;
				state=WaiterState.working; return true;
			}
		}
		for(MyCustomer customer:customers){
			if(customer.s==CustomerState.readyToOrder){
				TakeOrder(customer); state=WaiterState.working; return true;
			}
		}
		for(MyCustomer customer:customers){
			if(customer.s==CustomerState.ordered){
				SendOrder(customer); 
				customer.s = CustomerState.ignore;
				state=WaiterState.working; 
				return true;
			}
		}
		for(MyCustomer customer:customers){
			if(customer.s==CustomerState.done){
				CleanTable(customer); state=WaiterState.working; return true;
			}
		}
		
		if(state==WaiterState.working){ //just finished something, can message waiter
			state=WaiterState.good;
			host.IAmFree();
			return true;
		}
		
		if(state==WaiterState.okForBreak && customers.isEmpty()){
			goOnBreak(); return false;
		}
		
		if(!waiterGui.atBase()&&state!=WaiterState.onBreak){
			waiterGui.DoGoBase();
			return false;
		}
		
		return false;
		//We have tried all our rules and found nothing
		//Return false to main loop of abstract agent and wait
	}

	// ACTIONS
	public void StayStill(){
		//Busy Wait to keep Agent still
	}
	public void ServeFood(Order o){
		DoGoToTable(o.kitchen);
		while(!waiterGui.atTable(o.kitchen)){
			//Busy Wait
		}
		cook.PickedUp(o.kitchen);
		DoGoToTable(o.table);
		while(!waiterGui.atTable(o.table)){
			//Busy Wait
		}
		for(MyCustomer customer:customers){
			if(customer.choice==o.choice&&customer.table==o.table){
				customer.c.HereIsFoodAndBill(customer.bill);
				customer.s=CustomerState.ignore;
				DoServeCustomer(o.choice, o.table);
			}
		}
	}
	public void TakeOrder(MyCustomer c){
		while(!waiterGui.atTable(c.table)){
			DoGoToTable(c.table); return;
		}
		c.c.WhatWouldYouLike();
		c.s = CustomerState.askedToOrder;
	}
	public void SendOrder(MyCustomer c){
		orders.add(new Order(c.choice, c.table));
		DoDisplayOrder(c.choice, c.table);
		DoGoToTable(5);
		while(!waiterGui.atTable(5)){
			//Busy Wait//
		}
		cook.HereIsOrder(this, c.choice, c.table);
		GetBill(c);
	}
	private void GetBill(MyCustomer c){
		DoGoToTable(11);
		Do("Waiter is getting bill");
		while(!waiterGui.atTable(11)){
			//Busy Wait
		}
		cashier.ComputeBill(this, c.c);
		c.s = CustomerState.ignore;
	}
	public void CleanTable(MyCustomer c){
		while(!waiterGui.atTable(c.table)){
			DoGoToTable(c.table); return;
		}
		DoCleanFood();
		host.TableIsFree(c.table); 
		customers.remove(c);
		Do("Table has been cleaned. Customer is dead to me now.");
	}
	public void SeatCustomer(MyCustomer c){
		if(!waiterGui.atHome()){
			DoGoHome(); return;
			//Return statement forces waiter to go home
		}
		c.c.FollowMe(this, menu, c.table);
		DoGoToTable(c.table);
		c.s = CustomerState.ignore;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#goOnBreak()
	 */
	@Override
	public void goOnBreak(){
		state = WaiterState.onBreak;
		waiterGui.DoGoHome();
		breakTime.schedule(new TimerTask(){
			public void run(){
				TimerDone();
			}
		}, breakDuration);
	}
	public void AskToReorder(Order o){
		while(!waiterGui.atTable(o.table)){
			waiterGui.DoGoToTable(o.table); return;
		}
		waiterGui.DoRemoveOrder(o.table);
		for(MyCustomer c: customers){
			Do("in the loop");
			if(c.table==o.table){
				c.c.PleaseReorder(o.choice); Do("Customer asked to reorder");
				c.s=CustomerState.askedToOrder;
			}
		}
		orders.remove(o);
	}
	
	// ANIMATIONS
	/* (non-Javadoc)
	 * @see restaurant.Waiter#DoGoToTable(int)
	 */
	@Override
	public void DoGoToTable(int table){
		waiterGui.DoGoToTable(table);
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#DoGoHome()
	 */
	@Override
	public void DoGoHome(){
		waiterGui.DoGoHome();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#DoServeCustomer(java.lang.String, int)
	 */
	@Override
	public void DoServeCustomer(String choice, int table){
		waiterGui.DoServeFood(choice);
		waiterGui.DoRemoveOrder(table);
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#DoCleanFood()
	 */
	@Override
	public void DoCleanFood(){
		waiterGui.DoCleanFood();
	}

	@Override
	public void DoDisplayOrder(String choice, int table){
		waiterGui.DoDisplayOrder(choice, table);
	}
	
	//UTILITIES
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setHost(restaurant.interfaces.Host)
	 */
	@Override
	public void setHost (Host host){
		this.host = host;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setCook(restaurant.interfaces.Cook)
	 */
	@Override
	public void setCook (Cook cook){
		this.cook = cook;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setCashier(restaurant.interfaces.Cashier)
	 */
	@Override
	public void setCashier (Cashier cashier){
		this.cashier = cashier;
	}
	public void setGui(WaiterGui_ gui) {
		waiterGui = gui;
	}
	public WaiterGui_ getGui() {
		return waiterGui;
	}
	public boolean isAvailable() {
		if(this.state!=WaiterState.onBreak && this.state!=WaiterState.okForBreak)
			return true;
		return false;
	}
	public boolean isGood() {
		if(this.state==WaiterState.good)
			return true;
		return false;
	}
	public void setNumber(int number){
		this.number = number;
	}
	public int getNumber(){
		return number;
	}
}

