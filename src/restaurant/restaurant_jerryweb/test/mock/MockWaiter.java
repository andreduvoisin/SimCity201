package restaurant.restaurant_jerryweb.test.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import restaurant.restaurant_jerryweb.WaiterRole.MyCustomer;
import restaurant.restaurant_jerryweb.gui.Menu;
import restaurant.restaurant_jerryweb.interfaces.Cashier;
import restaurant.restaurant_jerryweb.interfaces.Customer;
import restaurant.restaurant_jerryweb.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();

	public Cashier cashier;
	
	public MockWaiter(String name) {
		super(name);
	}
	
	public void GetBill(Customer customer){
		log.add(new LoggedEvent("The cashier has finished computing the bill for customer " + customer.getName()));
	}
	
	public void msgPleaseSitCustomer(Customer cust, int tablenum){
		log.add(new LoggedEvent("Host asked me to sit customer " + cust.getName() + " at table " + tablenum));
	}
	
	public void msgLeavingTable(Customer c) {
		log.add(new LoggedEvent("Customer " + c.getName() + " is leaving."));
	}
	public void msgCanGoOnBreak(){
		log.add(new LoggedEvent("Asked host to go on break"));
	}
	
	public void msgOutOfFood(String choice, int t){
		log.add(new LoggedEvent("Recieved the out of order choice from cook for order " + choice + " for customer at table " + t));
	}
	
	public void msgHereIsMyOrder(Customer customer, String order){
		log.add(new LoggedEvent("Customer " + customer.getName() + " has given me their order: " + order));
	}
	
	public void msgOrderReady(String meal, int t){
		log.add(new LoggedEvent("Cook has sent the order is ready message for order " + meal + " for table " + t));
	}
		
	public void msgRecievedFood (){
		log.add(new LoggedEvent("The customer has sent the recieved food message"));
	}
	
	public void msgHereIsBill(Customer customer, double check){
		log.add(new LoggedEvent("The cashier has finished computing the bill for customer " + customer.getName() + " for " + check));
	}
	
	public void msgReadyToOrder(Customer customer){
		log.add(new LoggedEvent("Customer " + customer.getName() + " is ready to order."));
	}
	
	public void msgServedFood(){
		log.add(new LoggedEvent("Recieved the served food message."));
	}

	@Override
	public void msgAskForBreak() {
		log.add(new LoggedEvent("Recieved the AskForBreak message."));
		
	}

	@Override
	public void msgAtTable() {
		log.add(new LoggedEvent("Recieved the AtTable message."));	
	}

	@Override
	public void msgAtCook() {
		log.add(new LoggedEvent("Recieved the AtCook message."));		
	}

	@Override
	public void msgAtCashier() {
		log.add(new LoggedEvent("Recieved the AtCashier message."));
	}

	@Override
	public void msgTakeFood(String choice, int table) {
		log.add(new LoggedEvent("Recieved the TakeFood message."));
	}
	
	private void seatCustomer(MyCustomer customer) {
		log.add(new LoggedEvent("Executing the seatCustomer for customer " + customer.getCustomer().getName()));
	}
	
	public void takeOrder(MyCustomer customer){
		log.add(new LoggedEvent("Executing the takeOrder function for customer " + customer.getCustomer().getName()));
	}
	
	public void sendOrder(MyCustomer customer){
		log.add(new LoggedEvent("Executing the sendOrder to cook function for customer " + customer.getCustomer().getName()));
	}
	
	public void pickUpFood(MyCustomer customer){
		log.add(new LoggedEvent("Executing the pickUpFood function for customer " + customer.getCustomer().getName())); 
	}
}
