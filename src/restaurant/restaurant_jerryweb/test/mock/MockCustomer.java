package restaurant.restaurant_jerryweb.test.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import restaurant.restaurant_jerryweb.gui.Menu;
import restaurant.restaurant_jerryweb.interfaces.Cashier;
import restaurant.restaurant_jerryweb.interfaces.Customer;
import restaurant.restaurant_jerryweb.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {
	
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public double cash = 0;
	
	public MockCustomer(String name) {
		super(name);
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

	
	public void pay(){
		log.add(new LoggedEvent("Going to pay customer"));
		Paid();/*
		timer.schedule(new TimerTask() {
			public void run() {
				//print("What do I want ");
				
				
			}
		},
		1000);*/
	
	}
	
	public void Paid(){
		log.add(new LoggedEvent("Giving cashier cash"));
		cashier.msgPayment(this, 20);
		//hasCheck = false;
	}/*
	@Override
	public void HereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
	}*/
	@Override
	public void msgHereIsChange(double change) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ change));

	}


	/*public String getName() {
		log.add(new LoggedEvent("Getting my name"));
		return null;
	}*/
	@Override
	public void msgHereIsCheck() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Getting check from waiter"));
	}


	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Recieved food"));
	}
	
	 public void msgWaitInQue(int x, int y){
		 log.add(new LoggedEvent("Host told me to wait here"));
	 }

	@Override
	public void msgOrderNotAvailable(String choice) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("They are out of " + choice + ". I will order something else"));
	}


	@Override
	public void msgWhatDoYouWant() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("I was asked my order"));
	}


	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("" + this.getName() + "I am hungry now"));
	}


	@Override
	public void msgSitAtTable(int tableNumber, Menu m, Waiter w) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Recieved the sitAtTable message from waiter " + w + " to go to table " + tableNumber));
	}


	@Override
	public double getCash() {
		// TODO Auto-generated method stub
		//log.add(new LoggedEvent(
		return 0;
	}


	@Override
	public void msgWaitInQue(int x) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Host has told me to wait here"));
	}

}
