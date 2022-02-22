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

public class MenuView implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
    @FXML
    public Label nameText;

    private Stage stage;
    private Scene scene;
    private Socket client;

    @FXML
    public void initialize() {
        initClient();
    }

    @FXML
    private void initClient(){
        try {
            Context context = Context.getInstance();
            client = context.getClient();
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
        Context.getInstance().removeListening(this);
        Parent rootMain = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("connect-server.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    public void onButtonClick(ActionEvent event, String type) throws IOException {
        Context.getInstance().removeListening(this);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("game-view.fxml")));
        Parent rootMain = fxmlLoader.load();
        GameView controller = fxmlLoader.getController();
        controller.setType(type);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(rootMain);
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
        Context.getInstance().removeListening(this);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("chooseGame.fxml")));
        Parent rootMain = fxmlLoader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleResponse(String message) {
        if (message.startsWith("getName:OK")) {
            nameText.setText(message.substring(11));
        }
    }

}
