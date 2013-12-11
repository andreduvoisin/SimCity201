package restaurant.restaurant_maggiyan.roles;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.MaggiyanRestaurant;
import restaurant.restaurant_maggiyan.Menu;
import restaurant.restaurant_maggiyan.gui.MaggiyanAnimationPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanCustomerGui;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCashier;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanHost;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * Restaurant customer agent.
 */
public class MaggiyanCustomerRole extends BaseRole implements MaggiyanCustomer{
	//Customer data
	private String name;
	private int tableNumber;
	private int choiceIndex; 
	private int hungerLevel = 10;        // determines length of meal
	private double cash;
	
	//GUI 
	private MaggiyanCustomerGui customerGui;
	@SuppressWarnings("unused")
	private MaggiyanAnimationPanel mAnimationPanel; 
	
	private boolean reordering = false; 
	public static int waitTime = 5000; 
	Timer timer = new Timer();

	//For test cases
	@SuppressWarnings("unused")
	private double minCashAmt = 5.99;
	private double normCashAmt = 15.99; 
	@SuppressWarnings("unused")
	private boolean isImpatient = false;
	
	// Agent Correspondents
	private MaggiyanCustomer me; 
	private MaggiyanHost host;
	private MaggiyanWaiter waiter; 
	private MaggiyanCashier cashier;
	private Menu menu; 
	private Check check; 
	public String choice; 

	private AgentState state = AgentState.DoingNothing;//The start state
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, Eating, DoneEating, Paying, Leaving, ReadingMenu};

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, eating, doneEating, doneLeaving, donePaying, choosingOrder, needToReOrder, impatient};
	AgentEvent event = AgentEvent.none;

	private Semaphore animationReady = new Semaphore(0, true); 

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public MaggiyanCustomerRole(Person p){
		super(p);
		this.name = p.getName();
		
		cash = normCashAmt; 
	
	}
	
	//-----UTILITIES-----
	
//	public void print(String msg){
//		//System.out.println("[" + name + "]: " + msg );
//	}
	
	public void setHost(MaggiyanHostRole host) {
		this.host = host;
	}
	
	public void setCashier(MaggiyanCashierRole cashier){
		this.cashier = cashier;
	}

	public String getCustomerName() {
		return name;
	}
	
	public AgentState getState(){
		return state; 
	}
	
	// Messages
	//From host
	public void msgRestaurantFull(){
		if(name.equals("Impatient")){
			event = AgentEvent.impatient;
			isImpatient = true; 
		}
		stateChanged(); 
	}
	
	//From waiter 
	public void msgFollowMe(MaggiyanWaiter w, Menu m, int tableNumber){
		print("Received msgFollowMe");
		event = AgentEvent.followHost;
		this.tableNumber = tableNumber;
		waiter = w; 
		menu = m; 
		
		stateChanged();
	}
	
	public void msgWhatDoYouWant(){
		print ("Choosing Order"); 
		event = AgentEvent.choosingOrder;  
		stateChanged(); 
	}
	
	public void msgHereIsYourFood(){
		print("Order received"); 
		event = AgentEvent.eating; 
		stateChanged(); 
	}
	
	public void msgHereIsCheck(Check c){
		print("Received check"); 
		check = c;
		stateChanged();
	}

	public void msgOutOfYourChoice(){
		print("Received out of food notice");
		event = AgentEvent.needToReOrder;
		stateChanged();
	}
	
	//Messages for Animation
	
	public void msgAnimationReady(){
		animationReady.release();
		stateChanged(); 
	}
	
	public void gotHungry() {
		print("I'm hungry");
		customerGui.setPresent(true);
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		
		//Non-Normative
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.impatient ){
			state = AgentState.Leaving; 
			StopWaiting(); 
			return true;
		}
		
		//Normative 
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			print("here");
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.ReadingMenu;
			ReadMenu(); 
			return true;
		}
		
		if (state == AgentState.ReadingMenu && event == AgentEvent.choosingOrder){
			state = AgentState.Ordering;
			GiveOrder(); 
			return true;
		}
		
		if (state == AgentState.Ordering && event == AgentEvent.needToReOrder){
			state = AgentState.ReadingMenu;
			print("Re-Ordering");
			reordering = true; 
			ReadMenu(); 
		}
		
		if(state == AgentState.Ordering && event == AgentEvent.eating){
			state = AgentState.Eating;
			EatFood(); 
			return true; 
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Paying;
			PayForMeal();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.donePaying){
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

	// Actions
	private void StopWaiting(){
		Do("Leaving restaurant"); 
		host.msgLeaving(this); 
	}

	private void goToRestaurant() {
		print("Maggi Going to restaurant");
		customerGui.setHungry(MaggiyanRestaurant.customerPosCounter);
		MaggiyanRestaurant.customerPosCounter++; 
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		customerGui.DoGoToFrontOfLine(); 
		try{
			animationReady.acquire(); 
		}
		catch(Exception e){
			print("Animation release exception");
		} 
		waiter.msgReadyToBeSeated(); 
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(tableNumber);
	}
	
	private void ReadMenu(){
		Do("Reading menu");
		choiceIndex = (int)(Math.random()*4);
		//Non-normative scenarios
		//If out of cheapest item and cheap customer, then customer leaves
		if(reordering){
			reordering = false; 
			if(name.equals("Cheapskate")){
				print("Cheapskate alert. Can't afford food. Leaving"); 
				waiter.msgLeavingTable(this);
				customerGui.DoExitRestaurant();
				return; 
			}
			else{
				if(choiceIndex == (menu.MenuOptions.size()-1)){
					choiceIndex = 0; 
				}
				else{
					choiceIndex++; 
				}
			}
		}
		
		me = this;  
		//choice = this.getName();
		choice = menu.get(choiceIndex); 
		if(name.equals("Cheapskate")){
			print("Cheapskate alert. Ordering the cheapest item"); 
			choice = menu.getCheapestFood(); 
		}
		timer.schedule(new TimerTask() {
			public void run() {
				if(cash == 0.0){
					if(name.equals("Smartpoor")){
						print("No money. Leaving Restaurant");
						waiter.msgLeavingTable(me);
						customerGui.DoExitRestaurant();
						return; 
					}
				}
				customerGui.showChoice(choice);
				waiter.msgReadyToOrder(me); 
			}
		},
		hungerLevel * 250); 
	}
	
	private void GiveOrder(){
		hideChoice();
		waiter.msgHereIsMyOrder(choice, this);
		
	}
	
	private void EatFood() {
		customerGui.showOrder(choice);
		Do("Eating Food");
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating");
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void PayForMeal(){
		//nonnormative scenario
		if(name.equals("Dumbpoor")){
			print("No money. Dined and dashed.");
			waiter.msgLeavingTable(this);
			customerGui.DoExitRestaurant();
			return;
		}
		//normative scenario
		//print("Paying for meal: " + check.getCheckTotal()); 
		cashier.msgHereIsPayment(this, check.getCheckTotal());
		event = AgentEvent.donePaying; 
	}

	private void leaveTable() {
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		hideChoice();
		customerGui.DoExitRestaurant();
		
		mPerson.msgRoleFinished();
		mPerson.assignNextEvent();
	}

	// Accessors

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(MaggiyanCustomerGui g) {
		customerGui = g;
	}

	public MaggiyanCustomerGui getGui() {
		return customerGui;
	}
	
	public void hideChoice()
	{
		customerGui.hideChoice();
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(3);
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.R3);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R3);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R3, e);
	}
}

