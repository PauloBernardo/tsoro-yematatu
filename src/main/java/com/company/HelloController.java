package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.IOException;

public class HelloController extends ResizableView {
    @FXML
    public Button buttonMain;

    @FXML
    public void switchToConnectServer(ActionEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "connect-server.fxml");
    }
}
