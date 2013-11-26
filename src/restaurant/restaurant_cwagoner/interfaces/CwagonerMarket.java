package restaurant.restaurant_cwagoner.interfaces;

import java.util.HashMap;

public interface CwagonerMarket {

        public abstract void msgNeedFood(CwagonerCook c, CwagonerCashier ca, HashMap<String, Integer> orderedStock);
        public abstract void msgPayment(CwagonerCashier ca, double payment);
        public abstract String getName();
        
}
