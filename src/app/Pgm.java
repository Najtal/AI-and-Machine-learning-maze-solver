package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Classe app.Pgm : Main program, main controller
 * @author Jean-Vital Durieu
 * @version 0.01
 */
public class Pgm extends Application {


	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		primaryStage.setTitle(AppContext.INSTANCE.getProperty("guiTitleMain"));
		primaryStage.setScene(new Scene(root, 1050, 600));
		primaryStage.setResizable(false);
		//primaryStage.setOnCloseRequest();
		// TODO : handle window close request
		primaryStage.show();
	}

	public static void create() {
		launch(null);
	}


}
