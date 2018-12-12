package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Main extends Application {
	ObservableList<FoodItem> foodObservableList; 
	ListView<FoodItem> foodListView;
	
	List<FoodItem> filteredByNutrientList;
	List<FoodItem> filteredByNameList;
	
	ObservableList<FoodItem> mealObservableList = FXCollections.observableArrayList();
	
	FoodData foodData;
	List<String> rulesList = new ArrayList<String>();
	
	ListView<FoodItem> mealListView;
	TextField foodInput,calorieInput, nameFilter;
	MenuBar dropMenu;
	VBox vBoxRight;
	Label foodCountLabel;
	Stage rulesStage;
	Stage addFoodWindow;
	String nameQuery;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Food Query");
			BorderPane root = new BorderPane();
			VBox vBoxLeft = new VBox();
			HBox hbox = new HBox();
			HBox hbox2 = new HBox();
			Scene scene = new Scene(root,1000,720);
			
			foodListView = new ListView<>();
			foodInput = new TextField();
			nameFilter = new TextField();
			
			// Left Vertical Box - Meal List
			vBoxRight = new VBox();

			Label mealListLabel = new Label("Meal List");
			mealListLabel.setStyle("-fx-font: 24 segoeui;");
			
			mealListView = new ListView<>();
			mealListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			mealListView.setPrefHeight(700);
			mealListView.setPrefWidth(350);
			mealListView.setItems(mealObservableList.sorted());
			
			Button delMealItemButton = new Button("Delete Food");
			Button calculateSummaryButton = new Button("Calculate Summary");
			
			delMealItemButton.setOnAction(e -> {
				ObservableList<FoodItem> delItems = mealListView.getSelectionModel().getSelectedItems();
				//mealObservableList.removeAll(delItems);
				//System.out.println(delItems.size());
				Object[] foodArray = delItems.toArray();
				
				for(Object food : foodArray) {
					mealObservableList.remove(food);
				}
				
			});
			
			Label mealSummaryLabel = new Label("Meal Summary");
			mealSummaryLabel.setStyle("-fx-font: 24 segoeui");
			
			Label calorieSummaryLabel = new Label();
			Label fatSummaryLabel = new Label();
			Label carbSummaryLabel = new Label();
			Label fiberSummaryLabel = new Label();
			Label proteinSummaryLabel = new Label();
			
			Label totalCalorieLabel = new Label("Total Calories: ");
			Label totalFatLabel = new Label("Total Fat: ");
			Label totalCarbLabel = new Label("Total Carbs: ");
			Label totalFiberLabel = new Label("Total Fiber: ");
			Label totalProteinLabel = new Label("Total Protein: ");
			
			calculateSummaryButton.setOnAction(e -> {
				List<FoodItem> calculateList = mealObservableList;
				
				Double[] results = MealSummary.calculateNutrients(calculateList);
				
				calorieSummaryLabel.setText(Double.toString(results[0]));
				fatSummaryLabel.setText(Double.toString(results[1]));
				carbSummaryLabel.setText(Double.toString(results[2]));
				fiberSummaryLabel.setText(Double.toString(results[3]));
				proteinSummaryLabel.setText(Double.toString(results[4]));
				
				
			});
			
		
			
			HBox calorieHBox = new HBox();
			HBox fatHBox = new HBox();
			HBox carbHBox = new HBox();
			HBox fiberHBox = new HBox();
			HBox proteinHBox = new HBox();
			
			calorieHBox.getChildren().add(totalCalorieLabel);
			calorieHBox.getChildren().add(calorieSummaryLabel);
			fatHBox.getChildren().add(totalFatLabel);
			fatHBox.getChildren().add(fatSummaryLabel);
			carbHBox.getChildren().add(totalCarbLabel);
			carbHBox.getChildren().add(carbSummaryLabel);
			fiberHBox.getChildren().add(totalFiberLabel);
			fiberHBox.getChildren().add(fiberSummaryLabel);
			proteinHBox.getChildren().add(totalProteinLabel);
			proteinHBox.getChildren().add(proteinSummaryLabel);
			
			vBoxRight.getChildren().add(mealListLabel);
			vBoxRight.getChildren().add(mealListView);
			vBoxRight.getChildren().add(delMealItemButton);
			vBoxRight.getChildren().add(mealSummaryLabel);
			vBoxRight.getChildren().add(calorieHBox);
			vBoxRight.getChildren().add(fatHBox);
			vBoxRight.getChildren().add(carbHBox);
			vBoxRight.getChildren().add(fiberHBox);
			vBoxRight.getChildren().add(proteinHBox);
			vBoxRight.getChildren().add(calculateSummaryButton);
			
			// Drop down menu - MenuBar
			FileChooser fileChooser = new FileChooser();
			dropMenu = new MenuBar();
			Menu menuFile = new Menu("File");
			Menu menuHelp = new Menu("Help");
			menuFile.setOnShowing(e -> {  }); // TODO: Add events
			menuFile.setOnShown  (e -> {  });
			menuFile.setOnHiding (e -> {  });
			menuFile.setOnHidden (e -> {  });
			MenuItem menuFoodList = new MenuItem("Open FoodList File...");
			MenuItem menuSaveList = new MenuItem("Save FoodList");
			MenuItem menuHelpItem = new MenuItem("Show Instructions");
			
			menuFile.getItems().add(menuFoodList);
			menuFile.getItems().add(menuSaveList);
			menuHelp.getItems().add(menuHelpItem);
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV File", "*.csv");
			FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("TXT File", "*.txt");
			fileChooser.getExtensionFilters().addAll(extFilter,extFilter2);
			
			menuSaveList.setOnAction(e ->  {
				File saveFile = fileChooser.showSaveDialog(primaryStage);
				
				if(saveFile == null) {
					return;
				}
				
				if(foodObservableList != null) {
					FoodData savedData = new FoodData();
					
					for(FoodItem food : foodObservableList) {
						savedData.addFoodItem(food);
					}
					
					savedData.saveFoodItems(saveFile.getAbsolutePath());
				}
				else {
					displayErrorMessage("There is no food to save!");
				}
			});
			
			menuFoodList.setOnAction(e -> {
			    File selectedFile = fileChooser.showOpenDialog(primaryStage);
				if(selectedFile != null) {
				    String filePath = selectedFile.getAbsolutePath();
					
					foodData = new FoodData();
					foodData.loadFoodItems(filePath);
					foodObservableList = FXCollections.observableArrayList(foodData.getAllFoodItems());
					filteredByNutrientList = foodData.getAllFoodItems();
					filteredByNameList = foodData.getAllFoodItems();
					foodListView.setItems(foodObservableList.sorted());
					foodCountLabel.textProperty().bind((Bindings.size(foodObservableList).asString()));
					rulesList.clear();
				}
			});
			
			menuHelpItem.setOnAction(e -> {
				Stage instructionStage = new Stage();
				BorderPane instructionPane = new BorderPane();
				Label instructionLabel = new Label();
				String instructions = "Load food items from a file by selecting that file from the browsing menu."
						+ " Add food items by clicking on the Add New Food button. You can filter the food items by entering "
						+ "a letter/phrase to search for. You can also filter food items by nutritional content. Enter a rule"
						+ " by entering three things separated by spaces: <nutrient name> <comparator> <value>. The comparators are ="
						+ ",<=, and >=. You can add as many rules as you want by entering each one, then pressing apply."
						+ " To clear the filters, press the corresponding clear button. To add food items to the meal list, select them and hit send to meal.";
				instructionLabel.setWrapText(true);
				instructionLabel.setText(instructions);
				instructionPane.setTop(instructionLabel);
				Scene instructionScene = new Scene(instructionPane, 350, 350);
				instructionStage.setTitle("Instructions");
				instructionStage.setScene(instructionScene);
				instructionStage.show();
			});
			
			dropMenu.getMenus().addAll(menuFile, menuHelp);
			HBox dropMenuPanel = new HBox(dropMenu);
			foodListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			foodListView.setPrefHeight(700);
			foodInput.setPromptText("food");
			nameFilter.setPromptText("Enter name search");
			VBox addNewFoodSendToMeal = new VBox();
			Button addFood = new Button("Add New Food");
			Button sendToMeal = new Button("Send to Meal");
			Button showRules = new Button("Show Rules");
			Button applyAllQueries = new Button("Apply All Rules");
			Button nameFilterButton = new Button("Apply Name Filter");
			Button removeNameFilterButton = new Button("Remove Name Filter");
			
			addNewFoodSendToMeal.getChildren().addAll(addFood, sendToMeal,showRules,applyAllQueries);
			
			applyAllQueries.setOnAction(e -> {
				if(foodData != null) {
					List<List<FoodItem>> listsToIntersect = new ArrayList<List<FoodItem>>();
					filteredByNutrientList = foodData.filterByNutrients(rulesList);
					if(filteredByNameList != null && filteredByNutrientList != null) {
						listsToIntersect.add(filteredByNameList);
						listsToIntersect.add(filteredByNutrientList);
						List<FoodItem> intersection = MealSummary.intersectLists(listsToIntersect);
						
						foodObservableList.setAll(intersection);
					}
				}
			});
			
			nameFilterButton.setOnAction(e -> {
				String inputText = nameFilter.getText();
				nameQuery = inputText;
				if(foodData != null) {
					filteredByNameList = foodData.filterByName(inputText);
					List<List<FoodItem>> listsToIntersect = new ArrayList<List<FoodItem>>();
					listsToIntersect.add(filteredByNameList);
					listsToIntersect.add(filteredByNutrientList);
					List<FoodItem> intersection = MealSummary.intersectLists(listsToIntersect);
					
					foodObservableList.setAll(intersection);
				}
				else {
					displayErrorMessage("There is no food to filter!");
				}
				
			});
			
			removeNameFilterButton.setOnAction(e -> {
				nameQuery = null;
				if(filteredByNameList != null && foodObservableList != null) {
					filteredByNameList = foodData.getAllFoodItems();
					foodObservableList.setAll(filteredByNutrientList);
				}
			});
			
			sendToMeal.setOnAction((ActionEvent e) -> {
				ObservableList<FoodItem> addItems = foodListView.getSelectionModel().getSelectedItems();
				
				mealObservableList.addAll(addItems);
				
			});
			showRules.setOnAction((ActionEvent e) ->{
				if(rulesStage != null) {
					rulesStage.close();
				}
				rulesStage = new Stage();
				BorderPane rulePane = new BorderPane();
				VBox vbox = new VBox();
				ObservableList<String> observableRules = FXCollections.observableList(rulesList);
				ListView<String> rulesView = new ListView<String>(observableRules);
				rulesView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				rulePane.setTop(vbox);
				Button deleteRule = new Button("Delete Rule");
				vbox.getChildren().addAll(rulesView,deleteRule);
				
				deleteRule.setOnAction(f -> {
					for(String rule : rulesList) {
						System.out.print(rule);
					}
					System.out.println();
					List<String> selectedRules = rulesView.getSelectionModel().getSelectedItems();
					observableRules.removeAll(selectedRules);
					
					for(String rule : rulesList) {
						System.out.print(rule);
					}
					System.out.println();
					
					/*
					if(foodData != null) {
						filteredByNutrientList = foodData.filterByNutrients(rulesList);
						
						List<List<FoodItem>> foodLists = new ArrayList<List<FoodItem>>();
						foodLists.add(filteredByNutrientList);
						foodLists.add(filteredByNameList);
						foodObservableList.setAll(FXCollections.observableList(MealSummary.intersectLists(foodLists)));
					}
					*/
					if(observableRules.isEmpty()) {
						rulesStage.close();
					}
				});
				
				Scene ruleScene = new Scene(rulePane, 350, 700);
				rulesStage.setTitle("Rules");
				rulesStage.setScene(ruleScene);
				rulesStage.show();
			});
			
			HBox listViewAddFoodHBox = new HBox();
			listViewAddFoodHBox.getChildren().addAll(foodListView, addNewFoodSendToMeal);
			hbox2.getChildren().addAll(nameFilter,nameFilterButton, removeNameFilterButton);
			addFood.setOnAction((ActionEvent e) -> {
				if(addFoodWindow != null) {
					addFoodWindow.close();
				}
				addFoodWindow = new Stage();
				BorderPane bp2 = new BorderPane();
				VBox vbox2 = new VBox();
				TextField foodName = new TextField();
				TextField foodID = new TextField();
				TextField calorieCount = new TextField();
				TextField fatGrams = new TextField();
				TextField carbGrams = new TextField();
				TextField fiberGrams = new TextField();
				TextField proteinGrams = new TextField();
				Label title = new Label();
				title.setText("Add food item with its nutrients");
				foodID.setPromptText("Enter a unique ID for this food");
				foodName.setPromptText("food name");
				calorieCount.setPromptText("calorie count");
				fatGrams.setPromptText("fat grams");
				carbGrams.setPromptText("carbohydrate grams");
				fiberGrams.setPromptText("fiber grams");
				proteinGrams.setPromptText("protein grams");
				foodName.setFocusTraversable(false);
				calorieCount.setFocusTraversable(false);
				fatGrams.setFocusTraversable(false);
				carbGrams.setFocusTraversable(false);
				fiberGrams.setFocusTraversable(false);
				proteinGrams.setFocusTraversable(false);
			    foodID.setFocusTraversable(false);
				Button submit = new Button("submit");
				submit.setOnAction(r -> {
					 try {
					      FoodItem foodItem = new FoodItem(foodID.getText(),foodName.getText());
					      System.out.println(foodItem.getID());
					      Double calories = Double.parseDouble(calorieCount.getText());
					      Double fat = Double.parseDouble(fatGrams.getText());
					      Double carbohydrate = Double.parseDouble(carbGrams.getText());
					      Double fiber = Double.parseDouble(fiberGrams.getText());
					      Double protein = Double.parseDouble(proteinGrams.getText());
					      if(calories < 0 || fat < 0 || carbohydrate < 0 || fiber < 0 || protein < 0) {
					    	  displayErrorMessage("Nutrients cannot have negative nutrition values");
					      }
					      else {
						      foodItem.addNutrient("calories", calories);
						      foodItem.addNutrient("fat", fat);
						      foodItem.addNutrient("carbohydrate", carbohydrate);
						      foodItem.addNutrient("fiber", fiber);
						      foodItem.addNutrient("protein", protein);
					      
						      if(foodData == null) {
						    	  foodData = new FoodData();
						    	  							
						      }
						      
						      if(foodObservableList == null) {
						    	  foodObservableList = FXCollections.observableArrayList();
						    	  foodCountLabel.textProperty().bind((Bindings.size(foodObservableList).asString()));
						    	  filteredByNutrientList = new ArrayList<FoodItem>();
								  filteredByNameList = new ArrayList<FoodItem>();
								  filteredByNutrientList.add(foodItem);
								  filteredByNameList.add(foodItem);
								  foodData.addFoodItem(foodItem);
							      foodObservableList.add(foodItem);
								  foodListView.setItems(foodObservableList.sorted());
						      }
						      else {
							      foodData.addFoodItem(foodItem);
							      foodObservableList.add(foodItem);
							      filteredByNutrientList = foodData.filterByNutrients(rulesList);
							      if(nameQuery != null) {
							    	  filteredByNameList = foodData.filterByName(nameQuery);
							      }
						      }
						      
						      addFoodWindow.close();
					      }
					      
					     }catch(NumberFormatException j){
					    	 displayErrorMessage("You must enter numeric nutrition values");
					     }
					 
				});
				vbox2.getChildren().addAll(title,foodID, foodName,calorieCount,fatGrams,
						carbGrams,fiberGrams,proteinGrams,submit);
				bp2.setCenter(vbox2);
				Scene popupScene = new Scene(bp2, 750, 450);
				addFoodWindow.setTitle("Add Food Item");
				addFoodWindow.setScene(popupScene);
				addFoodWindow.show();
			});
			
			
			HBox nutrientQuery = new HBox();
			TextField nutrientQueryText = new TextField();
			nutrientQueryText.setPromptText("<nutrient> <comparator> <value>");
			nutrientQueryText.setPrefWidth(200);
			Button submitNutrientQuery = new Button("Add Rule");
			Button clearNutrientQuery = new Button("Clear All Rules");
			nutrientQuery.getChildren().addAll(nutrientQueryText, submitNutrientQuery, clearNutrientQuery);
			
			submitNutrientQuery.setOnAction(e -> {
				
				if(foodData == null) {
					displayErrorMessage("There is no food to filter!");
					return;
				}
				
				String textInput = nutrientQueryText.getText();
				nutrientQueryText.clear();
				boolean validInput = true;
				try {
					String[] ruleArray = textInput.split(" ");
					if(ruleArray.length == 3) {
			    		String nutrient = ruleArray[0];
			    		String comparator = ruleArray[1];
			    		Double value = Double.parseDouble(ruleArray[2]);
			    		
			    		if(!(comparator.contentEquals("<=") || comparator.contentEquals("==") ||
			    				comparator.contentEquals(">="))) {
			    			displayErrorMessage("Enter a valid comparator");
			    			validInput = false;
			    		}
			    		else {
			    		
				    		String[] validOptions = {"calories", "fat", "carbohydrate", "fiber", "protein"};
				    		
				    		boolean validString = false;
				    		for(String s : validOptions) {
				    			if(nutrient.equalsIgnoreCase(s)) {
				    				validString = true;
				    				break;
				    			}
				    		}
				    		
				    		if(!validString) {
				    			validInput = false;
				    			displayErrorMessage("You must enter a valid nutrient name");
				    		}
			    		
			    		}
					}
					else {
						validInput = false;
						displayErrorMessage("Incorrect number of arguments. See instructions.");
					}
					
					
				}catch(NumberFormatException f) {
					validInput = false;
					displayErrorMessage("You must enter a number for the nutrient value");
				}
				
				if(validInput) {
					rulesList.add(textInput);
					/*
					filteredByNutrientList = foodData.filterByNutrients(rulesList);
					
					List<List<FoodItem>> foodLists = new ArrayList<List<FoodItem>>();
					foodLists.add(filteredByNutrientList);
					foodLists.add(filteredByNameList);
					foodObservableList.setAll(FXCollections.observableList(MealSummary.intersectLists(foodLists)));
				*/
				}
			});
			
			clearNutrientQuery.setOnAction(e -> {
				if(rulesStage != null) {
					rulesStage.close();
				}
				rulesList.clear();
				if(foodObservableList != null && filteredByNutrientList != null) {
					filteredByNutrientList = foodData.getAllFoodItems();
					foodObservableList.setAll(filteredByNameList);
					for(FoodItem food : filteredByNameList) {
						System.out.println(food.getID());
					}
				}
			});
			
			
			
			Label foodListLabel = new Label("Food List");
			foodCountLabel = new Label();
			foodCountLabel.setText("0");
			Label foodCountDescription = new Label("Food Count");
			HBox foodCountListLabels = new HBox();
			foodListLabel.setStyle("-fx-font: 24 segoeui;");
			foodCountListLabels.getChildren().add(foodCountDescription);
			foodCountListLabels.getChildren().add(foodCountLabel);
			vBoxLeft.getChildren().add(foodListLabel);
			vBoxLeft.getChildren().add(listViewAddFoodHBox);
		    vBoxLeft.getChildren().add(hbox2);
		    vBoxLeft.getChildren().add(nutrientQuery);
		    
		    addNewFoodSendToMeal.getChildren().add(foodCountListLabels);
		    
		    //POPUP FOR INSTRUCTIONS
		    

			//spacing and padding start
		    
		    foodCountListLabels.setPadding(new Insets(10,0,0,10));
		    foodCountListLabels.setSpacing(10);
			listViewAddFoodHBox.setPadding(new Insets(0,5,0,10));
			listViewAddFoodHBox.setSpacing(10);
			
			foodListLabel.setPadding(new Insets(10,0,0,10));
			
			hbox2.setPadding(new Insets(10,10,5,10));
			hbox2.setSpacing(10);
			
			addNewFoodSendToMeal.setPadding(new Insets(0,5,5,10));
			addNewFoodSendToMeal.setSpacing(10);
			
			
			nutrientQuery.setPadding(new Insets(10,5,5,10));
			nutrientQuery.setSpacing(10);;
			
			
			vBoxRight.setPadding(new Insets(5,5,5,5));
			vBoxRight.setSpacing(5);
			//spacing and padding end

			//command sets color of list view
			foodListView.setStyle("-fx-control-inner-background: #DCF3FF");	
			 //sets style for background for the entire box
		    vBoxLeft.setStyle("-fx-background-color: #7aadff");
		    dropMenuPanel.setStyle("-fx-background-color: #7aadff");
		    vBoxRight.setStyle("-fx-background-color: #7aadff");
		    mealListView.setStyle("-fx-control-inner-background: #DCF3FF");
		    
		    
		    root.setTop(dropMenuPanel);
		    root.setLeft(vBoxLeft);
		    root.setRight(vBoxRight);
		    

		    HBox centerBox = new HBox();
		    centerBox.setStyle("-fx-background-color: #7aadff");
		    root.setCenter(centerBox);
		    
		    // Setting scene to stage and displaying stage.
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		    primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void displayErrorMessage(String errorText) {
		
		Alert errorAlert = new Alert(AlertType.ERROR);
		
		errorAlert.setHeaderText("Error");
		errorAlert.setContentText(errorText);
		errorAlert.showAndWait();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}