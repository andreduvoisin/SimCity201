package market.test.mock;

import java.util.Map;

import market.MarketInvoice;
import market.MarketOrder;
import market.interfaces.MarketCashier;
import restaurant.intermediate.interfaces.RestaurantCookInterface;
import test.mock.LoggedEvent;
import test.mock.Mock;
import base.Item.EnumItemType;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;

public class MockCookCustomer extends Mock implements RestaurantCookInterface, Role {

	public MockCookCustomer() {
		super();
	}
	
	public void msgInvoiceToPerson(Map<EnumItemType,Integer> cannotFulfill, MarketInvoice invoice) {
		log.add(new LoggedEvent("Received msgInvoiceToPerson."));
	}
	
	public void msgHereIsCookOrder(MarketOrder o) {
		log.add(new LoggedEvent("Received msgHereIsCookOrder."));
	}

	public void setMarketCashier(MarketCashier c) {
		log.add(new LoggedEvent("Set marketCashier to " + c));
	}
	
/*Role Actions*/
	public boolean pickAndExecuteAnAction() {
		return false;
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
		return null;
	}

	public boolean isRestaurantPerson() {
		return false;
	}

	@Override
	public void setActive() {
		// TODO Auto-generated method stub
		
	}

}
