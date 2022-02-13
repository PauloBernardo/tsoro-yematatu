package com.example.tsoroyematatu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloController {
    @FXML
    public Button buttonMain;
    @FXML
    public VBox root;
    @FXML
    private Label welcomeText;
    @FXML
    private Label titleText;

    private Stage stage;
    private Scene scene;
    private Parent rootMain;

    @FXML
    public void switchToConnectServer(ActionEvent event) throws IOException {
        Parent rootMain = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("connect-server.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome Tsoro Yematatu Application!");
        titleText.setText("Tsoro Yematatu!!");
    }
}
