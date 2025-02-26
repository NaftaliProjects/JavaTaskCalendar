/*Author : Naftali Davidov
 * Ver :    24/12/2024
 * 
 * Calendar class.
 * A GUI calendar 
 * with the option to save a task (text) on different dates by
 * clicking on a date, 
 * inputing a text 
 * clicking "ok" to save 
 */
 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Calendar extends Application{
	@Override
	public void start(Stage stage) throws Exception{
	Parent root = FXMLLoader.load(getClass().getResource("Calendar.fxml"));
	
	Scene scene = new Scene(root);
	scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
	stage.setTitle("Calendar");
	stage.setScene(scene);
	stage.show();
	
}
public static void main(String[] args) {
	launch(args);
	
		
	}
}