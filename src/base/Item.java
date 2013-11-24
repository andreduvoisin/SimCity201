package base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Item {
	
	public static final double cPRICE_STEAK = 6;
	public static final double cPRICE_CHICKEN = 4;
	public static final double cPRICE_SALAD = 2;
	public static final double cPRICE_PIZZA = 5;
	public static final double cPRICE_CAR = 200;
	public static enum EnumMarketItemType {CHICKEN, PIZZA, SALAD, STEAK, CAR};
	
	public static Map<EnumMarketItemType, Double> cMARKET_PRICES;
    static {
        Map<EnumMarketItemType, Double> map = new HashMap<EnumMarketItemType, Double>();
        map.put(EnumMarketItemType.STEAK, cPRICE_STEAK);
        map.put(EnumMarketItemType.CHICKEN, cPRICE_CHICKEN);
        map.put(EnumMarketItemType.SALAD, cPRICE_SALAD);
        map.put(EnumMarketItemType.PIZZA, cPRICE_PIZZA);
        map.put(EnumMarketItemType.CAR, cPRICE_CAR);
        cMARKET_PRICES = Collections.unmodifiableMap(map);
    }
    
	EnumMarketItemType mItemType;
	
	public Item(EnumMarketItemType itemType){
		mItemType = itemType;
	}
}
