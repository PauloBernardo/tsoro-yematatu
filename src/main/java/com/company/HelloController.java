package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.rmi.ConnectException;
import java.util.Locale;
import java.util.ResourceBundle;

public class HelloController extends ResizableView {
    @FXML
    public Button buttonMain;

    ResourceBundle bundle = ResourceBundle.getBundle("com.company.i18n", new Locale("pt_br", "pt_BR"));


    @FXML
    public void switchToConnectServer(ActionEvent event) throws IOException {
        try {
            this.switchBetweenScreen(((Node) event.getSource()).getScene(), "connect-server.fxml");
        } catch (ConnectException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("hello.connectionErrorText"));
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}
