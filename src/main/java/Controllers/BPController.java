package Controllers;


import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import Client.Client;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.application.Platform;

public class BPController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane BasePane;

    @FXML
    private TextField loginField;

    @FXML
    private Button actionButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    void initialize() {
       actionButton.setOnAction(event -> {
           try {
               Client currentClient = new Client(loginField.getText(), passwordField.getText());
               System.out.println(currentClient.GetStatus());
               while (currentClient.GetStatus().equals("...")) {
                   Thread.sleep(200);
               }
               System.out.println(currentClient.GetStatus());
               if (currentClient.GetStatus().equals("/~status~/-Yes")) {
                   actionButton.getScene().getWindow().hide();
                   FXMLLoader loader = new FXMLLoader();
                   Parent root = loader.load(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("fxmlFiles/ChatPage.fxml")));
                   CPController current = loader.getController();
                   current.SetClient(currentClient);
                   current.SetLabel(loginField.getText());
                   Scene baseScene = new Scene(root);
                   Stage primaryStage = new Stage();
                   primaryStage.setScene(baseScene);
                   primaryStage.resizableProperty().setValue(false);
                   primaryStage.setTitle("SpamChat");
                   primaryStage.setWidth(600);
                   primaryStage.setHeight(400);
                   primaryStage.show();
                   // button exit
                   primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                       public void handle(WindowEvent we)  {
                           currentClient.End();
                           Platform.exit();
                       }
                   });
               } else {
                   loginField.setText("");
                   passwordField.setText("");
               }
           } catch (Exception e) {
                  System.out.println("Something do wrong:" + e);
              }
       });
    }
}
