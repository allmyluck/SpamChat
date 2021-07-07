package Controllers;



import java.net.URL;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import Client.Client;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;


public class CPController {
    private Client client;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label logoLabel;

    @FXML
    private Label loginLabel;

    @FXML
    private TextField messageField;

    @FXML
    private TextArea messagesField;

    public void SetClient(Client client) {
        this.client = client;
    }

    public String Construct(String message) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        return (loginLabel.getText() + "-[" +
        time + "]-" + message);
    }

    public void SetLabel(String label) {
        loginLabel.setText(label);
    }

    @FXML
    void initialize() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        if(!client.GetMessages().isEmpty()) {
                            int size = client.GetMessages().size();
                            for (int i = 0; i < size;i++) {
                                messagesField.appendText(client.GetMessages().get(i) + "\n");
                                client.GetMessages().remove(i);
                                i--;
                                size--;
                            }
                        }
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                    Platform.runLater(updater);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        messageField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                if(!messageField.getText().isEmpty()) {
                    String message = Construct(messageField.getText());
                    client.SendMessage(message);
                    messageField.setText("");
                }
            }
        } );
    }
}
