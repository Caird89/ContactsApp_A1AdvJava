package Views;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent startUp = FXMLLoader.load(getClass().getResource("AllContacts.fxml"));
        primaryStage.setTitle("All Contacts");
        primaryStage.setScene(new Scene(startUp));
        primaryStage.show();
    }
}
