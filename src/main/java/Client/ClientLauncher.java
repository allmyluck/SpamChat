package Client;

//import com.sun.glass.ui.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;


public class ClientLauncher extends Application {

    public static void main(String[] argc) {
        Application.launch(argc);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxmlFiles/BasePage.fxml")));
        Scene baseScene = new Scene(root);
        primaryStage.setScene(baseScene);
        primaryStage.resizableProperty().setValue(false);
        primaryStage.setTitle("SpamChat");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.show();
    }
}
