package app;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class MengSeg_Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {		  
		  // 这里的root从FXML文件中加载进行初始化，这里FXMLLoader类用于加载FXML文件
          BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("mengseg_main.fxml"));
          Scene scene = new Scene(root, 600, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
