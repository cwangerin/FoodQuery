
	
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
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
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
