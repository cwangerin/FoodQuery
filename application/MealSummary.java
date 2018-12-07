package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MealSummary {

	/**
	 * calcuates the mealSummary for a given foodItemList
	 * @return [calories, fat, carbohydrate, fiber, protein]
	 */
	public static Double[] calculateNutrients(List<FoodItem> foodItemList) {
		Double totalCalories = new Double(0);
		Double totalFat = new Double(0);
		Double totalCarbohydrate = new Double(0);
		Double totalFiber = new Double(0);
		Double totalProtein = new Double(0);
		
		
		for(FoodItem food : foodItemList) {
			totalCalories += food.getNutrientValue("calories");
			totalFat += food.getNutrientValue("fat");
			totalCarbohydrate += food.getNutrientValue("carbohydrate");
			totalFiber += food.getNutrientValue("fiber");
			totalProtein += food.getNutrientValue("protein");
		}
		
		Double[] totals = {totalCalories, totalFat, totalCarbohydrate, totalFiber, totalProtein};
		
		return totals;
	}
	
	public static List<FoodItem> intersectLists(List<List<FoodItem>> foodLists) {
		
		Iterator<List<FoodItem>> iterator = foodLists.iterator();
		List<FoodItem> intersectedList;
		if(iterator.hasNext()) {
			intersectedList = iterator.next();
		
			while(iterator.hasNext()) {
				List<FoodItem> curList = iterator.next();
				intersectedList.retainAll(curList);
			}
			return intersectedList;
		}
		
		return new ArrayList<FoodItem>();
	}
	
}
