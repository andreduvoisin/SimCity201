package base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Item {
	
	public static final int cPRICE_STEAK = 4;
	public static final int cPRICE_CHICKEN = 2;
	public static final int cPRICE_SALAD = 1;
	public static final int cPRICE_PIZZA = 2;
	public static enum EnumMarketItemType {CHICKEN, PIZZA, SALAD, STEAK};
	public static Map<EnumMarketItemType, Integer> cMARKET_PRICES;
    static {
        Map<EnumMarketItemType, Integer> map = new HashMap<EnumMarketItemType, Integer>();
        map.put(EnumMarketItemType.STEAK, cPRICE_STEAK);
        map.put(EnumMarketItemType.CHICKEN, cPRICE_CHICKEN);
        map.put(EnumMarketItemType.SALAD, cPRICE_SALAD);
        map.put(EnumMarketItemType.PIZZA, cPRICE_PIZZA);
        cMARKET_PRICES = Collections.unmodifiableMap(map);
    }
	
	//REX: 2 Set prices

	EnumMarketItemType mItemType;
	
	public Item(EnumMarketItemType itemType){
		mItemType = itemType;
	}
}
