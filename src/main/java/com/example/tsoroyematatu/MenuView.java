package com.example.tsoroyematatu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;

public class MenuView extends ResizableView implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
    @FXML
    public Label nameText;

    @FXML
    public void initialize() {
        initClient();
    }

    @FXML
    private void initClient(){
        try {
            Context context = Context.getInstance();
            Socket client = context.getClient();
            context.addListening(this);
            connectionStatus.setText("Connection established!");
            PrintStream saida = new PrintStream(client.getOutputStream());
            saida.println("getName:");
        } catch (IOException ex) {
            connectionStatus.setText("Connection not established!");
        }
    }

    @FXML
    public void switchToConnectServer(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "connect-server.fxml");
    }

    public void onButtonClick(ActionEvent event, String type) throws IOException {
        Context.getInstance().removeListening(this);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("game-view.fxml")));
        Parent rootMain = fxmlLoader.load();
        GameView controller = fxmlLoader.getController();
        controller.setType(type);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        controller.setStage(stage);
        Scene scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onButtonRandomClick(ActionEvent event) throws IOException {
        this.onButtonClick(event, "random");
    }

    @FXML
    public void onButtonNewClick(ActionEvent event) throws IOException {
        this.onButtonClick(event, "new");
    }

    @FXML
    public void onButtonChooseClick(ActionEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "chooseGame.fxml");
    }

    @FXML
    public void onButtonHistoricClick(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "history-view.fxml");
    }

    @FXML
    public void handleResponse(String message) {
        if (message.startsWith("getName:OK")) {
            nameText.setText(message.substring(11));
        }
    }

}
