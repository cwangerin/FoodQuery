
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	// Fields
	
	/**
	 * For building UI controls, layouts, scenes, stage, etc.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane base = new BorderPane();
			
			Scene scene = new Scene(base,1280,720); // Standard 16:9 aspect ratio
			
			// Sets and shows the stage. DO NOT EDIT UNLESS NECESSARY
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * For processing arguments
	 * 
	 * Used to read data file, set username, permissions, call init(agrs), etc.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
