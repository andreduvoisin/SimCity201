package restaurant.intermediate;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;

import market.MarketInvoice;
import market.MarketOrder;
import market.interfaces.MarketCashier;
import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.intermediate.interfaces.RestaurantCashierInterface;
import restaurant.restaurant_davidmca.gui.DavidAnimationPanel;
import restaurant.restaurant_davidmca.roles.DavidCashierRole;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_duvoisin.roles.AndreCashierRole;
import restaurant.restaurant_maggiyan.gui.MaggiyanAnimationPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanCashierRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import restaurant.restaurant_tranac.gui.TranacAnimationPanel;
import restaurant.restaurant_tranac.roles.TranacCashierRole;
import restaurant.restaurant_xurex.RexCashierRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.Item.EnumItemType;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;
import base.reference.ContactList;

public class RestaurantCashierRole extends BaseRole implements RestaurantCashierInterface, RestaurantBaseInterface {
	
	static int totalCashiers = 0;
	
	Role subRole = null;
	int mRestaurantID;

	public RestaurantCashierRole(Person person, int restaurantID){
		super(person); 
		this.mRestaurantID = restaurantID;
	}
	
	public void setPerson(Person person) {
		super.mPerson = person;
		switch(mRestaurantID){
			case 0: //andre
				subRole = new AndreCashierRole(super.mPerson);
				AndreRestaurantPanel.instance.cashier = (AndreCashierRole) subRole;
				break;
//			case 1: //chase
//				subRole = ((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).cashier;
//				subRole.setPerson(super.mPerson);
//				break;
//			case 2: //jerry
//				subRole = ((JerrywebRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(2)).cashier;
//				subRole.setPerson(super.mPerson);
//				break;
			case 3: //maggi
				subRole = new MaggiyanCashierRole(super.mPerson);
				MaggiyanAnimationPanel.addPerson((MaggiyanCashierRole) subRole);
				break;
			case 4: //david
				subRole = new DavidCashierRole(super.mPerson);
				DavidAnimationPanel.cashier = (DavidCashierRole) subRole;
				break;
			case 5: //shane
				subRole = new SmilehamCashierRole(super.mPerson);
				SmilehamAnimationPanel.addPerson((SmilehamCashierRole) subRole);
				break;
			case 6: //angelica
				subRole = new TranacCashierRole(mPerson);
				TranacAnimationPanel.addPerson((TranacCashierRole)subRole);
				break;
			case 7: //rex
				subRole =  new RexCashierRole(super.mPerson);
				RexAnimationPanel.addPerson((RexCashierRole) subRole);
				break;
		}
	}
	
	public boolean pickAndExecuteAnAction() {
		if(marketPickAndExecuteAnAction())
			return true;
		return subRole.pickAndExecuteAnAction();
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
	}


/** Market Data, Messages, Scheduler, and Actions */
/* Data */
	public List<MarketBill> mMarketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	enum EnumBillStatus {PLACED, PAYING};
	
/* Messages */
	public void msgPlacedMarketOrder(MarketOrder o, int n) {
		mMarketBills.add(new MarketBill(o, n));
	}
	
	public void msgInvoiceToPerson(Map<EnumItemType, Integer> cannotFulfill, MarketInvoice invoice) {
		synchronized(mMarketBills) {
		for(MarketBill b : mMarketBills) {
			if(b.mOrder == invoice.mOrder) {
				b.mInvoice = invoice;
				b.mCannotFulfill = cannotFulfill;
				stateChanged();
			}
		}
		}
	}

/* Scheduler */
	public boolean marketPickAndExecuteAnAction() {
		synchronized(mMarketBills) {
		for(MarketBill b : mMarketBills) {
			if(b.mStatus == EnumBillStatus.PAYING) {
				verifyAndPayMarketInvoice(b);
				return true;
			}
		}
		}
		return false;
	}
	
/* Actions */
	public void verifyAndPayMarketInvoice(MarketBill b) {
		MarketOrder o = b.mOrder;
		MarketInvoice i = b.mInvoice;
		Map<EnumItemType, Integer> cf = b.mCannotFulfill;
		MarketCashier cashier = null;
		
		//ANGELICA: hack for now; should we pass market cashier with order?
		if(b.mMarketNumber == 0) {
			//figure out where market info is going to be
		}
		else {
			//figure out where market info is going to be
		}
		
		for(EnumItemType type : o.mItems.keySet()) {
			int orderNum = o.mItems.get(type);
			int billNum = i.mOrder.mItems.get(type) + cf.get(type);
			if(orderNum != billNum) {
				//message market that invoice is invalid
				mMarketBills.remove(b);
				return;
			}
		}
		
		i.mPayment = i.mTotal;
		ContactList.SendPayment(getSSN(), i.mMarketBankNumber, i.mTotal);
		cashier.msgPayingForOrder(i);
		
		synchronized(mMarketBills) {
			mMarketBills.remove(b);
		}
	}
	
/* Classes */
	class MarketBill {
		MarketOrder mOrder;
		MarketInvoice mInvoice;
		int mMarketNumber;
		Map<EnumItemType, Integer> mCannotFulfill;
		EnumBillStatus mStatus;
		
		MarketBill(MarketOrder o, int n) {
			mOrder = o;
			mMarketNumber = n;
			mInvoice = null;
			mStatus = EnumBillStatus.PLACED;
		}
	}
}
