package market.test.mock;

import java.util.Map;

import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;
import base.Item.EnumItemType;
import market.*;
import market.gui.MarketCustomerGui;
import market.interfaces.MarketCustomer;
import test.mock.*;

public class MockCustomer extends Mock implements MarketCustomer, Role {
	MarketCustomerGui mCustomerGui;
	
	public MockCustomer() {
		super();
	}
	
	public void msgInvoiceToPerson(Map<EnumItemType, Integer> cannotFulfill, MarketInvoice invoice) {
		log.add(new LoggedEvent("Received msgInvoiceToPerson"));
	}
		
	public void msgHereIsCustomerOrder(MarketOrder order) {
		log.add(new LoggedEvent("Received msgHereIsCustomerOrder."));
	}
	
/** Animation Functions and Messages */
	public void msgAnimationAtMarket() {
		log.add(new LoggedEvent("Received msgAnimationAtMarket."));
		inTransit.release();
	}
	
	public void msgAnimationAtWaitingArea() {
		log.add(new LoggedEvent("Received msgAnimationAtWaitingArea."));
		inTransit.release();
	}
	
	public void msgAnimationLeftMarket() {
		log.add(new LoggedEvent("Received msgAnimationLeftMarket."));
		inTransit.release();
	}
	
	public void setGui(MarketCustomerGui g) {
		mCustomerGui = g;
	}
	
	public void DoGoToMarket() {
		mCustomerGui.DoGoToMarket();
	}
	
	public void DoWaitForOrder() {
		mCustomerGui.DoWaitForOrder();
	}
	
	public void DoLeaveMarket() {
		mCustomerGui.DoLeaveMarket();
	}
	
/*Role Actions*/
	public boolean pickAndExecuteAnAction() {
		return true;
	}

	public void setPerson(Person person) {
	}

	public PersonAgent getPersonAgent() {
		return null;
	}

	public boolean isActive() {
		return false;
	}

	public int getSSN() {
		return 0;
	}

	public Person getPerson() {
		Person temp = new PersonAgent();
		return temp;
	}

	public boolean isRestaurantPerson() {
		return false;
	}

	@Override
	public void setActive() {
		// TODO Auto-generated method stub
		
	}

}
