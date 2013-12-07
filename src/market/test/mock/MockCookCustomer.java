package market.test.mock;

import java.util.Map;

import market.MarketOrder;
import market.interfaces.MarketCashier;
import restaurant.intermediate.interfaces.RestaurantCookInterface;
import test.mock.LoggedEvent;
import test.mock.Mock;
import base.Item.EnumItemType;
import base.Location;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;

public class MockCookCustomer extends Mock implements RestaurantCookInterface, Role {

	public MockCookCustomer() {
		super();
	}
	
	public void msgCannotFulfillItems(MarketOrder o, Map<EnumItemType, Integer> cannotFulfill) {
		log.add(new LoggedEvent("Received msgCannotFulfillItems."));
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

	@Override
	public boolean hasPerson() {
		return true;
	}

	@Override
	public void GoToDestination(Location location) {
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public void setMarketCashier(int n) {

	}

}
