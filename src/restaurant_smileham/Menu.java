package restaurant_smileham;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant_smileham.Food.EnumFoodOptions;

public class Menu {
	private Map<EnumFoodOptions, Integer> mMenu;
	private List<EnumFoodOptions> mMenuOptions;
	
	//Constants
	public final static int cSTEAK_PRICE = 15;
	public final static int cCHICKEN_PRICE = 10;
	public final static int cSALAD_PRICE = 5;
	public final static int cPIZZA_PRICE = 8;
	public static final Map<EnumFoodOptions, Integer> cFOOD_PRICES;
    static {
        Map<EnumFoodOptions, Integer> map = new HashMap<EnumFoodOptions, Integer>();
        map.put(EnumFoodOptions.STEAK, cSTEAK_PRICE);
        map.put(EnumFoodOptions.CHICKEN, cCHICKEN_PRICE);
        map.put(EnumFoodOptions.SALAD, cSALAD_PRICE);
        map.put(EnumFoodOptions.PIZZA, cPIZZA_PRICE);
        cFOOD_PRICES = Collections.unmodifiableMap(map);
    }
	
	public Menu(){
		mMenu = cFOOD_PRICES;
		
		mMenuOptions = new ArrayList<>();
		for (EnumFoodOptions iFood : EnumFoodOptions.values()){
			mMenuOptions.add(iFood);
		}
	}
	
	public void removeChoice(EnumFoodOptions food){
		mMenuOptions.remove(food);
	}
	
	public int numChoices(){
		return mMenuOptions.size();
	}
	
	public Map<EnumFoodOptions, Integer> getMenu(){
		return mMenu;
	}
	
	public List<EnumFoodOptions> getMenuOptions(){
		return mMenuOptions;
	}
	
}
