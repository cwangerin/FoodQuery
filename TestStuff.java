package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestStuff {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String filePath = "foodItems.txt";
		
		FoodData foodData = new FoodData();
		foodData.loadFoodItems(filePath);
		
		List<FoodItem> foodItemList = foodData.getAllFoodItems();
		/*
		for(FoodItem food : foodItemList) {
			System.out.println(food.getName());
		}
		*/
		System.out.println();
		
		List<String> rulesList = new ArrayList<String>();
		List<String> rulesList2 = new ArrayList<String>();
		List<String> rulesList3 = new ArrayList<String>();
		rulesList.add("calories >= 300");
		rulesList2.add("fat >= 20");
		rulesList3.add("calories <= 399");
		List<FoodItem> filteredList1 = foodData.filterByNutrients(rulesList);
		List<FoodItem> filteredList2 = foodData.filterByNutrients(rulesList2);
		List<FoodItem> filteredList3 = foodData.filterByNutrients(rulesList3);
		List<FoodItem> filteredByName = foodData.filterByName("piz");
		List<List<FoodItem>> filteredLists = new ArrayList<List<FoodItem>>();
		filteredLists.add(filteredList1);
		filteredLists.add(filteredList2);
		filteredLists.add(filteredList3);
		
		List<FoodItem> intersectedList = MealSummary.intersectLists(filteredLists);
		
		
		for(FoodItem food : intersectedList) {
			System.out.println(food.getName());
		}
		System.out.println();
		/*
		for(FoodItem food : filteredByName) {
			System.out.println(food.getName());
		}
		*/
		foodData.saveFoodItems("Trump.txt");
		System.out.println(Arrays.toString(MealSummary.calculateNutrients(intersectedList)));
		
	}

}
