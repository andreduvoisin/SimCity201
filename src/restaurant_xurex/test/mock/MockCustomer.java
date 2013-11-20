package restaurant_xurex.test.mock;


import java.util.Map;

import restaurant_xurex.interfaces.Cashier;
import restaurant_xurex.interfaces.Customer;
import restaurant_xurex.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	
	private String choice = null;

	public MockCustomer(String name) {
		super(name);
	}

	@Override
	public void HereIsFoodAndBill(float bill) {

	}


	@Override
	public void FullRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void FollowMe(Waiter waiter, Map<String, Integer> menu, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void PleaseReorder(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HereIsChange(float change) {
		log.add(new LoggedEvent("HereIsChange: "+change));
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getChoice() {
		return choice;
	}
	
	public String getName(){
		return name;
	}

	@Override
	public void SetChoice(String choice) {
		this.choice = choice;
	}

}
