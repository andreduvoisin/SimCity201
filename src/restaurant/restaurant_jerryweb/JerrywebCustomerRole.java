package restaurant.restaurant_jerryweb;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.restaurant_jerryweb.gui.CustomerGui;
import restaurant.restaurant_jerryweb.gui.Menu;
import restaurant.restaurant_jerryweb.interfaces.Customer;
import restaurant.restaurant_jerryweb.interfaces.Waiter;
import base.BaseRole;
import base.ContactList;
import base.Event;
import base.Event.EnumEventType;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * Restaurant customer agent.
 */
public class JerrywebCustomerRole extends BaseRole implements Customer {
	
	int tableN;
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	public double cash = 0;
	public Menu m = new Menu();
	// agent correspondents
	private JerrywebHostRole host;
	private Waiter waiter;
	private JerrywebCashierRole cashier;
	private boolean hasCheck = false;
	
	public int QuePosX = 0;
	public int QuePosY = 0;
	
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, readingMenu, readyToOrder, waitingForFood, readyToEat, preparingToEat, Eating, DoneEating, goingToPaying, paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, reading, ordering, outOfChoice, ordered, recievedFood, doneEating,  recievedChange, payCashier,  doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public JerrywebCustomerRole(Person person){
		super(person);
		this.name = person.getName();
		if(!name.equals("flake")){
			if(name.equals("5")){
				cash = 5;
			}
			else if(name.equals("10")){
				cash = 10;
			}
			else if(name.equals("15")){
				cash = 15;
			}
			else{
				cash = 20;}
		}
		else{ cash = 0;}
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(JerrywebHostRole host) {
		
		this.host = host;
	}
	
	public void setCashier(JerrywebCashierRole cashier){
		this.cashier = cashier;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		////print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgWaitInQue(int positionNumber){
		if(positionNumber == 0){
			customerGui.xDestination = 20;
			customerGui.yDestination = 120;
		}
		
		if(positionNumber == 1){
			customerGui.xDestination = 20;
			customerGui.yDestination = 98;
		}
		
		if(positionNumber == 2){
			customerGui.xDestination = 20;
			customerGui.yDestination = 86;
		}
		
		if(positionNumber == 3){
			customerGui.xDestination = 20;
			customerGui.yDestination = 64;
		}
		
		if(positionNumber == 4){
			customerGui.xDestination = 20;
			customerGui.yDestination = 42;
		}
		
		if(positionNumber == 5){
			customerGui.xDestination = 20;
			customerGui.yDestination = 20;
		}
	}

	public void msgSitAtTable(int tableNumber, Menu m, Waiter w) {
		customerGui.customerString = "";
		////print("I have " + cash + " dollars.");
		this.waiter = w;
		tableN = tableNumber;
		////print("Received msgSitAtTable");
		
		event = AgentEvent.followWaiter;
		stateChanged();
	}
	
	public void msgWhatDoYouWant(){
		event = AgentEvent.ordering;
		stateChanged();
		
	}
	
	public void msgOrderNotAvailable(String choice){
		event = AgentEvent.outOfChoice;
		//print("My state is: " + this.state);		
		//print("Ok I won't order " + choice);
		m.menuItems.remove(choice);
		
		stateChanged();
	}
	
	public void msgHereIsYourFood(){
		//print("Thank You");
		event = AgentEvent.recievedFood;
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
	
	public void msgHereIsCheck(){
		//print("thanks for check");
		hasCheck = true;
		stateChanged();
	}
	public void msgAnimationFinishedGoToCashier(){
			event = AgentEvent.payCashier;
			stateChanged();
	}
	public void msgHereIsChange(double change){
		//print("Thank you for the change");
		cash = change;
		event = AgentEvent.recievedChange;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown(m);
			return true;
		}
		
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.readingMenu;
			readMenu();
			return true;
		}
		if (state == AgentState.preparingToEat && event == AgentEvent.outOfChoice){
			state = AgentState.readyToOrder;
			callWaiter();
			return true;
		}
		if (state == AgentState.readingMenu && event == AgentEvent.reading){
			state = AgentState.readyToOrder;
			callWaiter();
			return true;
		}
		
		if (state == AgentState.readyToOrder && event == AgentEvent.ordering){
			state = AgentState.waitingForFood;
			pickMeal(m);
			return true;
		}
		
		if (state == AgentState.waitingForFood && event == AgentEvent.ordered){
			state = AgentState.readyToEat;
			preparingToEat();
			return true;
		}
		
		if (state == AgentState.preparingToEat && event == AgentEvent.recievedFood){
			state = AgentState.Eating;
			ThankWaiter();
			EatFood();
			return true;
		}

		
		 if(state == AgentState.Eating && event == AgentEvent.doneEating && hasCheck){
		  	state = AgentState.goingToPaying;
		  	goToCashier();
		  	return true;
		 }
		
		if (state == AgentState.goingToPaying && event == AgentEvent.payCashier){
			state = AgentState.paying;
			pay();
			return true;
		}
			
		
		 if(state == AgentState.paying && event == AgentEvent.recievedChange){
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
	/*public void ThankWaiter(){
		waiter.msgRecievedFood();
	}*/
	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
		//waiter.msgIWantFood(this);
	}

	private void SitDown(Menu m) {
		Do("Being seated. Going to table");
		
		customerGui.DoGoToSeat(tableN);
	}
	public void goToCashier(){
		Do("PayForMeal");
		customerGui.DoPayForMeal();
	}
	
	public void pay(){
		//print("paying");
		timer.schedule(new TimerTask() {
			public void run() {
				////print("What do I want ");
				
				Paid();
			}
		},
		1000);
	
	}
	
	public void Paid(){
		cashier.msgPayment(this, cash);
		hasCheck = false;
	}
	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			
			public void run() {
				////print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
		mPerson.msgAddEvent(new Event(EnumEventType.MAINTAIN_HOUSE, 0));
		mPerson.setJobFalse();
		mPerson.msgRoleFinished();
	}
	
	public void readMenu(){
		timer.schedule(new TimerTask() {
			public void run() {
				////print("What do I want ");
				event = AgentEvent.reading;
				
				stateChanged();
			}
		},
		5000);
		
	}
	
	public void callWaiter(){
		//print("WAITER!");
		customerGui.customerString = "?";
		waiter.msgReadyToOrder(this);
		
	}
	
	//This notifies the waiter that 
	public void ThankWaiter(){
		waiter.msgRecievedFood();
	}
	
	public void pickMeal(Menu m){
		
		timer.schedule(new TimerTask() {
			public void run() {
				givingOrder();
			}
		},
		1000);
		//givingOrder();
	}
	//This method gives the allusion that the customer is telling his order to the waiter within a time period that would 
	//otherwise be instantaneous 
	public void givingOrder(){
		if(this.getName().equals("steak") && cash >= 15.99){
			 waiter.msgHereIsMyOrder(this, m.menuItems.get(3));
			 //print("Give me the " + m.menuItems.get(3));
			 event = AgentEvent.ordered;
			 stateChanged();
		}
		else if(this.getName().equals("chicken") && cash >= 10.99){
			 waiter.msgHereIsMyOrder(this, m.menuItems.get(2));
			 //print("Give me the " + m.menuItems.get(2));
			 event = AgentEvent.ordered;
			 stateChanged();
		}
		else if(this.getName().equals("salad") && cash >= 5.99 ){
			 waiter.msgHereIsMyOrder(this, m.menuItems.get(0));
			 //print("Give me the " + m.menuItems.get(0));
			 event = AgentEvent.ordered;
			 stateChanged();
		}
		else if(this.getName().equals("pizza") && cash >= 8.99){
			 waiter.msgHereIsMyOrder(this, m.menuItems.get(1));
			 //print("Give me the " + m.menuItems.get(1));
			 event = AgentEvent.ordered;
			 stateChanged();
		}
		else{
			if(cash < 15.99){
				m.menuItems.remove("steak");
				if(cash < 10.99){
					m.menuItems.remove("chicken");
					if(cash < 8.99){
						m.menuItems.remove("pizza");
					}
				}
			}
			Random rand = new Random(System.currentTimeMillis());
			int selection = rand.nextInt(m.menuItems.size());
		 	if(selection == 3  && cash >= 15.99|| this.getName().equals("flake")){
		 		waiter.msgHereIsMyOrder(this, m.menuItems.get(selection));
		 		//print("Give me the " + m.menuItems.get(selection));
		 		event = AgentEvent.ordered;
		 		//state = AgentState.waitingForFood;
				customerGui.customerString = "";
				stateChanged();
		 	}
		 	
		 	else if(selection == 2 && cash >= 10.99 || this.getName().equals("flake")){
		 		waiter.msgHereIsMyOrder(this, m.menuItems.get(selection));
		 		 //print("Give me the " + m.menuItems.get(selection));
		 		event = AgentEvent.ordered;
		 		//state = AgentState.waitingForFood;
				customerGui.customerString = "";
				stateChanged();
		 	}

		 	
		 	else if(selection == 1 && cash >= 8.99 || this.getName().equals("flake")){
		 		waiter.msgHereIsMyOrder(this, m.menuItems.get(selection));
		 		 //print("Give me the " + m.menuItems.get(selection));
		 		event = AgentEvent.ordered;
		 		//state = AgentState.waitingForFood;
				customerGui.customerString = "";
				stateChanged();
		 	}	
		 	else if(selection == 0 && cash >= 5.99 || this.getName().equals("flake")){
		 		waiter.msgHereIsMyOrder(this, m.menuItems.get(selection));
		 		 //print("Give me the " + m.menuItems.get(selection));
		 		event = AgentEvent.ordered;
		 		 //state = AgentState.waitingForFood;
				customerGui.customerString = "";
				stateChanged();
		 	}
		 	else if(cash <= 5.99){ 
			  //print("I can't afford anything");
			  event = AgentEvent.recievedChange;
			  state = AgentState.paying;
			  waiter.msgHereIsMyOrder(this, "nothing");
			  customerGui.customerString = "I'm broke!";
			  stateChanged();
		 	}
		 	else{ givingOrder();}
			}
		customerGui.customerString = "";
	}
	
	public void preparingToEat(){
		state = AgentState.preparingToEat;
		stateChanged();
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public double getCash(){
		return cash;
	}
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
		
	}

	public CustomerGui getGui() {
		return customerGui;
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(2);
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

