package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;

public class Main extends Application {
	ListView<String> foodListView, mealListView;
	ArrayList<String> food;
	MenuBar dropMenu;
	VBox vBoxRight, vBoxLeft;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Food Query");
			BorderPane root = new BorderPane();
			vBoxLeft = new VBox();
			vBoxRight = new VBox();
			HBox hbox = new HBox();
			HBox hbox2 = new HBox();
			Scene scene = new Scene(root,1000,720);
			
			food = new ArrayList();
			foodListView = new ListView<>();
			
			// Right Vertical Box - Meal List
			
			Label mealListLabel = new Label("Meal List");
			mealListLabel.setStyle("-fx-font: 24 segoeui;");
			
			mealListView = new ListView<>();
			mealListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			mealListView.setPrefHeight(700);
			mealListView.setPrefWidth(350);
			
			Button delMealItemButton = new Button("Delete Food");
			Button calculateSummaryButton = new Button("Calculate Summary");
			
			Label mealSummaryLabel = new Label("Meal Summary");
			mealSummaryLabel.setStyle("-fx-font: 24 segoeui");
			
			Label totalCalorieLabel = new Label("Total Calories: ");
			Label totalFatLabel = new Label("Total Fat: ");
			Label totalCarbLabel = new Label("Total Carbs: ");
			Label totalFiberLabel = new Label("Total Fiber: ");
			Label totalProteinLabel = new Label("Total Protein: ");
			
			Label calorieSummaryLabel = new Label("500.0");
			Label fatSummaryLabel = new Label("15.0 g");
			Label carbSummaryLabel = new Label("10.0 g");
			Label fiberSummaryLabel = new Label("4.0 g");
			Label proteinSummaryLabel = new Label("2.0 g");
			
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
			
			vBoxRight.getChildren().addAll(mealListLabel,mealListView,delMealItemButton,
					mealSummaryLabel,calorieHBox,fatHBox,carbHBox,fiberHBox,proteinHBox,
					calculateSummaryButton);
			
			// Drop down menu - MenuBar
			FileChooser fileChooser = new FileChooser();
			dropMenu = new MenuBar();
			Menu menuFile = new Menu("Load Food Data");
			Menu menuHelp = new Menu("Help");
			menuFile.setOnShowing(e -> {  }); // TODO: Add events
			menuFile.setOnShown  (e -> {  });
			menuFile.setOnHiding (e -> {  });
			menuFile.setOnHidden (e -> {  });
			MenuItem menuFoodList = new MenuItem("Open FoodList File...");
			MenuItem menuHelpItem = new MenuItem("Show Instructions");
			menuFoodList.setOnAction(e -> {
			    fileChooser.showOpenDialog(primaryStage);
			});
			menuFile.getItems().add(menuFoodList);
			menuHelp.getItems().add(menuHelpItem);
			MenuItem menuMealList = new MenuItem("Load Meal List");
			menuFoodList.setOnAction(e -> {
			    File selectedFile = fileChooser.showOpenDialog(primaryStage);
				String filePath = selectedFile.getAbsolutePath();
				
				FoodData foodData = new FoodData();
				foodData.loadFoodItems(filePath);
				List<FoodItem> foodList = foodData.getAllFoodItems();
				for(FoodItem food : foodList) {
					System.out.println(food.getName());
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
			TextField foodInput = new TextField();
			TextField nameFilter = new TextField();
			foodInput.setPromptText("food");
			nameFilter.setPromptText("Enter name search");
			VBox addNewFoodSendToMeal = new VBox();
			Button addFood = new Button("Add New Food");
			Button sendToMeal = new Button("Send to Meal");
			Button deleteButton = new Button("Delete");
			Button nameFilterButton = new Button("Apply Name Filter");
			Button removeNameFilter = new Button("Remove Name Filter");
			
			addNewFoodSendToMeal.getChildren().addAll(addFood, sendToMeal);
			
			HBox listViewAddFoodHBox = new HBox();
			listViewAddFoodHBox.getChildren().addAll(foodListView, addNewFoodSendToMeal);
			hbox2.getChildren().addAll(nameFilter,nameFilterButton, removeNameFilter);
			addFood.setOnAction((ActionEvent e) -> {
				Stage addFoodWindow = new Stage();
				BorderPane bp2 = new BorderPane();
				VBox vbox2 = new VBox();
				TextField foodName = new TextField();
				TextField calorieCount = new TextField();
				TextField fatGrams = new TextField();
				TextField carbGrams = new TextField();
				TextField fiberGrams = new TextField();
				TextField proteinGrams = new TextField();
				Label title = new Label();
				title.setText("Add food item with its nutrients");
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
				Button submit = new Button("submit");
				submit.setOnAction(r -> addFoodWindow.close());
				vbox2.getChildren().addAll(title,foodName,calorieCount,fatGrams,
						carbGrams,fiberGrams,proteinGrams,submit);
				bp2.setCenter(vbox2);
				Scene popupScene = new Scene(bp2, 750, 450);
				addFoodWindow.setTitle("Add Food Item");
				addFoodWindow.setScene(popupScene);
				addFoodWindow.show();
			});
			
			deleteButton.setOnAction((ActionEvent e) -> {
				ObservableList<String> delete = foodListView.getSelectionModel().getSelectedItems();
				foodListView.getItems().removeAll(delete);
			});
			nameFilterButton.setOnAction((ActionEvent e) -> {
				Iterator iter = foodListView.getItems().iterator();
				while(iter.hasNext()) {
					String next = (String) iter.next();
					if(food.contains(next) == false) {
						food.add(next);
					}
					if(!next.contains(nameFilter.getText())) {
						iter.remove();
					}
				}
			});
			
			HBox nutrientQuery = new HBox();
			TextField nutrientQueryText = new TextField();
			nutrientQueryText.setPromptText("<nutrient> <comparator> <value>");
			nutrientQueryText.setPrefWidth(200);
			Button submitNutrientQuery = new Button("Apply Nutrient Query");
			Button clearNutrientQuery = new Button("Clear Nutrient Queries");
			nutrientQuery.getChildren().addAll(nutrientQueryText, submitNutrientQuery, clearNutrientQuery);
			
			
			Label foodListLabel = new Label("Food List");
			foodListLabel.setStyle("-fx-font: 24 segoeui;");
			vBoxLeft.getChildren().add(foodListLabel);
			vBoxLeft.getChildren().add(listViewAddFoodHBox);
		    vBoxLeft.getChildren().add(hbox2);
		    vBoxLeft.getChildren().add(nutrientQuery);
		    
		    //POPUP FOR INSTRUCTIONS
		    

			//spacing and padding start
			listViewAddFoodHBox.setPadding(new Insets(0,5,0,10));
			listViewAddFoodHBox.setSpacing(10);
			
			foodListLabel.setPadding(new Insets(10,0,0,10));
			
			hbox2.setPadding(new Insets(10,10,5,10));
			hbox2.setSpacing(10);
			
			addNewFoodSendToMeal.setPadding(new Insets(0,5,5,10));
			addNewFoodSendToMeal.setSpacing(10);
			
			vBoxLeft.setPadding(new Insets(5,5,5,5));
			
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
		    
		    //Hard coding lists for demonstration puroses
		    foodListView.getItems().addAll("Apple", "Banana", "Cake", "Muffin");
		    mealListView.getItems().addAll("Apple", "Muffin");
		    
		    //
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
	
	public HBox getData() {
		HBox hbox = new HBox();
		Button btn = new Button("Upload Data");
		TextField text = new TextField();
		text.setPromptText("Food input file");
		
		btn.setOnAction((ActionEvent e) -> {
			String filename = text.getText();
			File file = new File(filename);
			try {
				Scanner sc = new Scanner(file);
				while(sc.hasNextLine()) {
					String line = sc.nextLine();
					foodListView.getItems().add(line);;
					System.out.println(line);
				}
				sc.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		hbox.getChildren().add(new Label("Food List:"));
		hbox.getChildren().add(text);
	    hbox.getChildren().add(btn);
	    hbox.setSpacing(10);
	    return hbox;
	}
	public static void main(String[] args) {
		launch(args);
	}
}
