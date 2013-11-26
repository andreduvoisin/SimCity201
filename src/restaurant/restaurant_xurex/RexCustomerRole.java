package restaurant.restaurant_xurex;

import restaurant.restaurant_xurex.gui.CustomerGui;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import restaurant.restaurant_xurex.interfaces.Cashier;
import restaurant.restaurant_xurex.interfaces.Customer;
import restaurant.restaurant_xurex.interfaces.Host;
import restaurant.restaurant_xurex.interfaces.Waiter;
import base.BaseRole;
import base.interfaces.Person;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.util.Random;

/**
 * Restaurant customer agent.
 */
public class RexCustomerRole extends BaseRole implements Customer{
	
	private String name;
	private int hungerLevel = 10;
	private Timer timer = new Timer();
	private CustomerGui customerGui;
	private int table;
	
	private Map<String, Integer> menu;
	private String choice;
	
	private float cash;
	private float bill;
	
	//Random # Generator for Menu Choice
	private Date date = new Date();
	private Random generator = new Random(date.getTime());
	
	//Agent Correspondents
	private Host host;
	private Waiter waiter;
	private Cashier cashier;

	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, deciding, decidingAgain,
	askedToOrder, askedToReorder, ordering, served, Eating, DoneEating, paying, Leaving};
	private AgentState state = AgentState.DoingNothing;

	public enum AgentEvent 
	{none, gotHungry, longWait, followWaiter, seated, doneEating, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public RexCustomerRole(String name, Person person){
		super(person);
		this.name = name;
		if(IsInt(name)){
			this.cash = (float) Integer.parseInt(name);
		}
		else{
			this.cash = generator.nextInt(20)+10;
		}
		Do(this.getName()+" created with $"+cash);
		
	}
	
	public RexCustomerRole(){
		super();
	}
	public RexCustomerRole(RexAnimationPanel animationPanel){
		super();
		CustomerGui gui = new CustomerGui(this, animationPanel);
		gui.setRole(this);
		this.setGui(gui);
		animationPanel.addGui(gui);
	}
	
	public RexCustomerRole(RexAnimationPanel animationPanel, RexHostRole host){
		super();
		this.host = host;
		CustomerGui gui = new CustomerGui(this, animationPanel);
		gui.setRole(this);
		this.setGui(gui);
		animationPanel.addGui(gui);
	}

	/**
	 * Hack to establish connection to initial agents
	 */
	public void setHost(Host host) {
		this.host = host;
	}
	
	public void setCashier(Cashier cashier){
		this.cashier = cashier;
	}
	
	// HOST MESSAGES //
	public void FullRestaurant(){
		event = AgentEvent.longWait;
		Do("Recieved full restaurant emssage");
		stateChanged();
	}
	
	// WAITER MESSAGES //
	public void FollowMe(Waiter waiter, Map<String, Integer> menu, int table) {
		this.waiter=waiter;
		this.table=table;
		this.menu = menu;
		event = AgentEvent.followWaiter;
		stateChanged();
	}
	
	public void WhatWouldYouLike(){
		state = AgentState.askedToOrder;
		stateChanged();
	}
	
	public void PleaseReorder(String choice){
		state = AgentState.askedToReorder;
		menu.remove(choice); Do("New menu size is "+menu.size());
		stateChanged();
	}
	
	public void HereIsFoodAndBill(float bill){
		this.bill = bill;
		state = AgentState.served;
		stateChanged();
	}
	
	// CASHIER MESSAGES //
	public void HereIsChange(float change){
		cash += change;
		bill = 0;
		event = AgentEvent.donePaying;
		stateChanged();
	}
	
	// ANIMATION MESSAGES //
	public void gotHungry() {
		event = AgentEvent.gotHungry;
		Do("Received got Hungry");
		stateChanged();
	}
	
	//GUI MESSAGES
	public void msgAnimationFinishedGoToSeat() {
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.longWait){
			event = AgentEvent.none;
			StayOrNot();
			return true;
		}
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.deciding;
			DecideOnFood();
			return true;
		}
		
		if(state == AgentState.askedToOrder && event == AgentEvent.seated){
			state = AgentState.ordering;
			MakeChoice();
			return true;
		}
		
		if(state == AgentState.askedToReorder && event == AgentEvent.seated){
			state = AgentState.decidingAgain;
			DecideOnFood();
			return true;
		}
		
		if (state == AgentState.served && event == AgentEvent.seated){
			state = AgentState.Eating;
			EatFood();
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.paying;
			GoToCashier();
			return true;
		}
		
		if (state == AgentState.paying && event == AgentEvent.seated){
			event = AgentEvent.none;
			PayBill();
			return true;
		}
		
		if (state == AgentState.paying && event == AgentEvent.donePaying){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// ACTIONS //
	private void goToRestaurant() {
		customerGui.DoGoWaitInLine();
		host.IWantFood(this);
		Do("goToRestaurant called");
	}
	private void StayOrNot() {
		host.IWillWait(this);
		/*
		int stay = generator.nextInt(2);
		if(stay==1){
			host.IWillWait(this);
		}
		else{
			host.IWillNotWait(this);
			customerGui.DoExitRestaurant();
			customerGui.animationPanel.updateCustomerLine();
			timer.schedule(new TimerTask(){
				public void run(){
					customerGui.SetCustomerEnabled();
				}
			}, 1000);
		}
		*/
	}
	private void SitDown() {
		customerGui.DoGoToSeat(table);
	}
	private void DecideOnFood() {
		if(state == AgentState.decidingAgain && cash>5 && cash<9){
			leaveTable();
		}
		if((name.equals("Steak")||name.equals("Chicken")||name.equals("Salad")||name.equals("Pizza")) && !(state==AgentState.decidingAgain))
			choice = name;
		else if (cash>5 && cash<9){
			// NON NORM: Poor Customer //
			choice = "Salad";
		}	
		else if (cash<6){
			int stay = generator.nextInt(2);
			if(stay == 0){
				leaveTable(); return;
			}
			else{
				RandomChoice();
			}
		}
		else {
			RandomChoice();
		}
		
		final RexCustomerRole temp = this;
		timer.schedule(new TimerTask() {
			public void run() {
				waiter.ReadyToOrder(temp);
			}
		}, generator.nextInt(6)+1*1000);
	}	
	private void MakeChoice() {
		waiter.HereIsChoice(this, choice);
		state = AgentState.DoingNothing;
	}
	private void EatFood() {
		Do("Enjoying a nice "+choice);
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
		getHungerLevel()*1000);
	}
	private void PayBill(){
		float payment;
		if(bill>=cash){
			payment = cash;
		}
		else{
			if(bill<=10 && cash>=10){
				payment = 10;
			}
			else if(bill<=20 && cash>=20){
				payment = 20;
			}
			else{
				payment = cash;
			}
		}
		cash -= payment;
		cashier.IWantToPay(this, payment);
	}
	private void GoToCashier(){
		customerGui.DoGoToSeat(5);
	}
	private void leaveTable() {
		Do("Leaving."); cash+=15;
		waiter.Leaving(this);
		customerGui.DoExitRestaurant();
	}
	
	// ACCESSORS //

	public String getName() {
		return name;
	}	
	public int getHungerLevel() {
		return hungerLevel;
	}
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}
	public void setGui(CustomerGui g) {
		customerGui = g;
	}
	public CustomerGui getGui() {
		return customerGui;
	}
	public String getChoice(){
		return choice;
	}
	
	// UTILITIES //
	private void RandomChoice(){
		//Random Choice Generator//
		int option = generator.nextInt(menu.size());
		ArrayList<String> stringMenu = new ArrayList<String>();
		for(String key : menu.keySet()){
			stringMenu.add(key);
		}
		choice = stringMenu.get(option);
	}
	private boolean IsInt(String name){
		try
		  { Integer.parseInt(name); return true; }
		 catch(NumberFormatException er)
		  { return false; }
	}
	public void SetChoice(String choice){
		this.choice = choice;
	}
	public void setHost(RexHostRole host){
		this.host = host;
	}

	public void setName(String name) {
		this.name = name;
	}
}

